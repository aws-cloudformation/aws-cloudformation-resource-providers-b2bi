package software.amazon.b2bi.profile
// TODO: Critical! Please replace the CloudFormation Tag model below with your service's own SDK Tag model
import java.util.Collections
import java.util.HashMap
import java.util.HashSet
import java.util.Objects
import java.util.stream.Collectors
import com.google.common.collect.Sets
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.collections.MapUtils
import org.apache.commons.lang3.ObjectUtils
import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.core.SdkClient
import software.amazon.awssdk.services.cloudformation.model.Tag
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

class TagHelper {
    /**
     * shouldUpdateTags
     *
     * Determines whether user defined tags have been changed during update.
     */
    fun shouldUpdateTags(
        resourceModel: ResourceModel?,
        handlerRequest: ResourceHandlerRequest<ResourceModel?>
    ): Boolean {
        val previousTags = getPreviouslyAttachedTags(handlerRequest)
        val desiredTags = getNewDesiredTags(handlerRequest)
        return ObjectUtils.notEqual(previousTags, desiredTags)
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
    fun getPreviouslyAttachedTags(handlerRequest: ResourceHandlerRequest<ResourceModel?>): Map<String, String> {
        val previousTags = mutableMapOf<String, String>()

        // TODO: get previous system tags if your service supports CloudFormation system tags
        // if (handlerRequest.getPreviousSystemTags() != null) {
        //     previousTags.putAll(handlerRequest.getPreviousSystemTags());
        // }

        // get previous stack level tags from handlerRequest
        if (handlerRequest.getPreviousResourceTags() != null) {
            previousTags.putAll(handlerRequest.getPreviousResourceTags())
        }

        // TODO: get resource level tags from previous resource state based on your tag property name
        // TODO: previousTags.putAll(handlerRequest.getPreviousResourceState().getTags()); // if tags are not null
        return previousTags
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
    fun getNewDesiredTags(handlerRequest: ResourceHandlerRequest<ResourceModel?>): Map<String, String> {
        val desiredTags = mutableMapOf<String, String>()

        // TODO: merge system tags with desired resource tags if your service supports CloudFormation system tags
        // if (handlerRequest.getSystemTags() != null) {
        //     desiredTags.putAll(handlerRequest.getSystemTags());
        // }

        // get desired stack level tags from handlerRequest
        if (handlerRequest.getDesiredResourceTags() != null) {
            desiredTags.putAll(handlerRequest.getDesiredResourceTags())
        }

        // TODO: get resource level tags from resource model based on your tag property name
        // TODO: desiredTags.putAll(convertToMap(handlerRequest.getDesiredResourceState().getTags())); // if tags are not null
        return desiredTags
    }

    /**
     * generateTagsToAdd
     *
     * Determines the tags the customer desired to define or redefine.
     */
    fun generateTagsToAdd(
        previousTags: Map<String, String>,
        desiredTags: Map<String, String>
    ): Map<String, String> {
        return desiredTags.entries
            .filter { e -> !previousTags.containsKey(e.key) || previousTags[e.key] != e.value }
            .associate { it.key to it.value }
    }

    /**
     * getTagsToRemove
     *
     * Determines the tags the customer desired to remove from the function.
     */
    fun generateTagsToRemove(previousTags: Map<String, String>, desiredTags: Map<String, String>): Set<String> {
        val desiredTagNames = desiredTags.keys
        return previousTags.keys.stream()
            .filter { tagName -> !desiredTagNames.contains(tagName) }
            .collect(Collectors.toSet())
    }

    /**
     * generateTagsToAdd
     *
     * Determines the tags the customer desired to define or redefine.
     */
    fun generateTagsToAdd(previousTags: Set<Tag>, desiredTags: Set<Tag>): Set<Tag> {
        return Sets.difference(HashSet(desiredTags), HashSet(previousTags))
    }

    /**
     * getTagsToRemove
     *
     * Determines the tags the customer desired to remove from the function.
     */
    fun generateTagsToRemove(previousTags: Set<Tag>, desiredTags: Set<Tag>): Set<Tag> {
        return Sets.difference(HashSet(previousTags), HashSet(desiredTags))
    }

    /**
     * tagResource during update
     *
     * Calls the service:TagResource API.
     */
    private fun tagResource(
        proxy: AmazonWebServicesClientProxy,
        serviceClient: ProxyClient<SdkClient>,
        resourceModel: ResourceModel,
        handlerRequest: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext,
        addedTags: Map<String, String>,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext> {
        // TODO: add log for adding tags to resources during update
        // e.g. logger.log(String.format("[UPDATE][IN PROGRESS] Going to add tags for ... resource: %s with AccountId: %s",
        // resourceModel.getResourceName(), handlerRequest.getAwsAccountId()));

        // TODO: change untagResource in the method to your service API according to your SDK
        return proxy.initiate("AWS-B2BI-Profile::TagOps", serviceClient, resourceModel, callbackContext)
            .translateToServiceRequest { model -> Translator.tagResourceRequest(model, addedTags) }
            .makeServiceCall { request, client -> null as AwsResponse? }
            .progress()
    }

    /**
     * untagResource during update
     *
     * Calls the service:UntagResource API.
     */
    private fun untagResource(
        proxy: AmazonWebServicesClientProxy,
        serviceClient: ProxyClient<SdkClient>,
        resourceModel: ResourceModel,
        handlerRequest: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext,
        removedTags: Set<String>,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext> {
        // TODO: add log for removing tags from resources during update
        // e.g. logger.log(String.format("[UPDATE][IN PROGRESS] Going to remove tags for ... resource: %s with AccountId: %s",
        // resourceModel.getResourceName(), handlerRequest.getAwsAccountId()));

        // TODO: change untagResource in the method to your service API according to your SDK
        return proxy.initiate("AWS-B2BI-Profile::TagOps", serviceClient, resourceModel, callbackContext)
            .translateToServiceRequest { model -> Translator.untagResourceRequest(model, removedTags) }
            .makeServiceCall { request, client -> null as AwsResponse? }
            .progress()
    }

    companion object {
        /**
         * convertToMap
         *
         * Converts a collection of Tag objects to a tag-name -> tag-value map.
         *
         * Note: Tag objects with null tag values will not be included in the output
         * map.
         *
         * @param tags Collection of tags to convert
         * @return Converted Map of tags
         */
        fun convertToMap(tags: Collection<Tag>): Map<String, String> {
            if (tags.isEmpty()) {
                return Collections.emptyMap()
            }
            return tags.stream()
                .filter { it.value() != null }
                .collect(Collectors.toMap(
                    Tag::key,
                    Tag::value,
                    { oldValue, newValue -> newValue }
                ))
        }

        /**
         * convertToSet
         *
         * Converts a tag map to a set of Tag objects.
         *
         * Note: Like convertToMap, convertToSet filters out value-less tag entries.
         *
         * @param tagMap Map of tags to convert
         * @return Set of Tag objects
         */
        fun convertToSet(tagMap: Map<String, String?>): Set<Tag> {
            if (tagMap.isEmpty()) {
                return Collections.emptySet()
            }
            return tagMap.entries.stream()
                .filter { it.value != null }
                .map { tag ->
                    Tag.builder()
                        .key(tag.key)
                        .value(tag.value)
                        .build()
                }
                .collect(Collectors.toSet())
        }
    }
}
