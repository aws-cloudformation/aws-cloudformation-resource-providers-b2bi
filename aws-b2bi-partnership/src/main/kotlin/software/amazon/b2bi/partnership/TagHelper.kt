package software.amazon.b2bi.partnership

import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceRequest
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import software.amazon.awssdk.services.b2bi.model.Tag as SdkTag
import software.amazon.b2bi.partnership.Tag as ResourceTag

object TagHelper {
    private const val TAG_OPERATION = "AWS-B2BI-Partnership::TagResource"
    private const val UNTAG_OPERATION = "AWS-B2BI-Partnership::UntagResource"

    /**
     * shouldUpdateTags
     *
     * Determines whether user defined tags have been changed during update.
     */
    fun shouldUpdateTags(handlerRequest: ResourceHandlerRequest<ResourceModel>): Boolean {
        val previousTags = getPreviouslyAttachedTags(handlerRequest)
        val desiredTags = getNewDesiredTags(handlerRequest)
        return previousTags != desiredTags
    }

    /**
     * getPreviouslyAttachedTags
     *
     * If stack tags and resource tags are not merged together in Configuration class,
     * we will get previously attached system (with `aws:cloudformation` prefix) and user defined tags from
     * handlerRequest.getPreviousSystemTags() (system tags),
     * handlerRequest.getPreviousResourceTags() (stack tags),
     * handlerRequest.getPreviousResourceState().getTags() (resource tags).
     *
     * System tags are an optional feature. Merge them to your tags if you have enabled them for your resource.
     * System tags can change on resource update if the resource is imported to the stack.
     */
    fun getPreviouslyAttachedTags(handlerRequest: ResourceHandlerRequest<ResourceModel>): Map<String, String> {
        return getTags(
            handlerRequest.previousResourceTags,
            convertToMap(handlerRequest.previousResourceState.tags)
        )
    }

    /**
     * getNewDesiredTags
     *
     * If stack tags and resource tags are not merged together in Configuration class,
     * we will get new desired system (with `aws:cloudformation` prefix) and user defined tags from
     * handlerRequest.getSystemTags() (system tags),
     * handlerRequest.getDesiredResourceTags() (stack tags),
     * handlerRequest.getDesiredResourceState().getTags() (resource tags).
     *
     * System tags are an optional feature. Merge them to your tags if you have enabled them for your resource.
     * System tags can change on resource update if the resource is imported to the stack.
     */
    fun getNewDesiredTags(handlerRequest: ResourceHandlerRequest<ResourceModel>): Map<String, String> {
        return getTags(
            handlerRequest.desiredResourceTags,
            convertToMap(handlerRequest.desiredResourceState.tags)
        )
    }

    private fun getTags(vararg tags: Map<String, String>?): Map<String, String> {
        return tags
            .filterNotNull()
            .flatMap { it.entries }
            .associate { it.key to it.value }
    }

    /**
     * generateTagsToAdd
     *
     * Determines the tags the customer desired to define or redefine.
     */
    fun generateTagsToAdd(previousTags: Map<String, String>, desiredTags: Map<String, String>): Map<String, String> {
        return desiredTags.entries
            .filter { !previousTags.containsKey(it.key) || previousTags[it.key] != it.value }
            .associate { it.key to it.value }
    }

    /**
     * getTagsToRemove
     *
     * Determines the tags the customer desired to remove from the function.
     */
    fun generateTagsToRemove(previousTags: Map<String, String>, desiredTags: Map<String, String>): Set<String> {
        val desiredTagNames = desiredTags.keys
        return previousTags.keys
            .filter { tagName -> !desiredTagNames.contains(tagName) }
            .toSet()
    }

    /**
     * tagResource during update
     *
     * Calls the service:TagResource API.
     */
    fun tagResource(
        proxy: AmazonWebServicesClientProxy,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel,
        callbackContext: CallbackContext?,
        addedTags: Map<String, String>,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        return proxy.initiate(TAG_OPERATION, proxyClient, resourceModel, callbackContext)
            .translateToServiceRequest { model -> Translator.translateToTagResourceRequest(model, addedTags) }
            .makeServiceCall { request, client ->
                val response = proxy.injectCredentialsAndInvokeV2(request, client.client()::tagResource)
                logger.log("Successfully tagged ${ResourceModel.TYPE_NAME} ${resourceModel.partnershipId}")
                response
            }
            .progress()
    }

    /**
     * untagResource during update
     *
     * Calls the service:UntagResource API.
     */
    fun untagResource(
        proxy: AmazonWebServicesClientProxy,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel,
        callbackContext: CallbackContext?,
        removedTags: Set<String>,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        return proxy.initiate(UNTAG_OPERATION, proxyClient, resourceModel, callbackContext)
            .translateToServiceRequest { model -> Translator.translateToUntagResourceRequest(model, removedTags) }
            .makeServiceCall { request, client ->
                val response = proxy.injectCredentialsAndInvokeV2(request, client.client()::untagResource)
                logger.log("Successfully untagged ${ResourceModel.TYPE_NAME} ${resourceModel.partnershipId}")
                response
            }
            .progress()
    }

    /**
     * List tags for resource
     *
     * Calls the service:ListTagsForResource API.
     */
    fun listTagsForResource(
        resourceArn: String,
        proxyClient: ProxyClient<B2BiClient>
    ): List<ResourceTag> {
        val request = ListTagsForResourceRequest.builder()
            .resourceARN(resourceArn)
            .build()
        val response = proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::listTagsForResource)
        return response.tags().map { it.toResourceTag() }
    }

    /**
     * convertToMap
     *
     * Converts a collection of ResourceTag objects to a tag-name -> tag-value map.
     *
     * @param tags Collection of tags to convert
     * @return Converted Map of tags
     */
    fun convertToMap(tags: List<ResourceTag>?): Map<String, String> {
        if (tags.isNullOrEmpty()) {
            return emptyMap()
        }
        return tags.associate { it.key to it.value }
    }

    /**
     * convertToSet
     *
     * Converts a tag map to a set of ResourceTag objects.
     *
     * @param tagMap Map of tags to convert
     * @return Set of ResourceTag objects
     */
    fun convertToSet(tagMap: Map<String, String>): Set<ResourceTag> {
        if (tagMap.isEmpty()) {
            return emptySet()
        }
        return tagMap.map {
            ResourceTag.builder()
                .key(it.key)
                .value(it.value)
                .build()
        }.toSet()
    }

    /**
     * convertToList
     *
     * Converts a tag map to a list of ResourceTag objects.
     *
     * @param tagMap Map of tags to convert
     * @return List of ResourceTag objects
     */
    fun convertToList(tagMap: Map<String, String>): List<ResourceTag> {
        if (tagMap.isEmpty()) {
            return emptyList()
        }
        return tagMap.map {
            ResourceTag.builder()
                .key(it.key)
                .value(it.value)
                .build()
        }
    }

    fun ResourceTag.toSdkTag(): SdkTag {
        return SdkTag.builder().key(this.key).value(this.value).build()
    }

    fun SdkTag.toResourceTag(): ResourceTag {
        return ResourceTag.builder().key(this.key()).value(this.value()).build()
    }
}