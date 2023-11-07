package software.amazon.b2bi.profile

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.UpdateProfileRequest
import software.amazon.awssdk.services.b2bi.model.UpdateProfileResponse
import software.amazon.b2bi.profile.TagHelper.convertToList
import software.amazon.b2bi.profile.TagHelper.generateTagsToAdd
import software.amazon.b2bi.profile.TagHelper.generateTagsToRemove
import software.amazon.b2bi.profile.TagHelper.getNewDesiredTags
import software.amazon.b2bi.profile.TagHelper.getPreviouslyAttachedTags
import software.amazon.b2bi.profile.TagHelper.shouldUpdateTags
import software.amazon.b2bi.profile.TagHelper.tagResource
import software.amazon.b2bi.profile.TagHelper.untagResource
import software.amazon.b2bi.profile.Translator.toCfnException
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

class UpdateHandler : BaseHandlerStd() {
    private lateinit var logger: Logger

    override fun handleRequest(
        proxy: AmazonWebServicesClientProxy,
        request: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext?,
        proxyClient: ProxyClient<B2BiClient>,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        this.logger = logger

        val resourceModel = request.desiredResourceState
        resourceModel.tags = convertToList(getNewDesiredTags(request))
        return ProgressEvent.progress(resourceModel, callbackContext)
            .then { progress ->
                proxy.initiate(OPERATION, proxyClient, progress.resourceModel, progress.callbackContext)
                    .translateToServiceRequest(Translator::translateToUpdateRequest)
                    .makeServiceCall { awsRequest, client -> updateProfile(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> updateTags(proxy, proxyClient, progress.callbackContext, request) }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }

    private fun updateProfile(
        request: UpdateProfileRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): UpdateProfileResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::updateProfile)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        resourceModel.apply {
            profileArn = response.profileArn()
        }
        logger.log("Successfully updated ${ResourceModel.TYPE_NAME} ${resourceModel.profileId}")
        return response
    }

    private fun updateTags(
        proxy: AmazonWebServicesClientProxy,
        proxyClient: ProxyClient<B2BiClient>,
        callbackContext: CallbackContext?,
        request: ResourceHandlerRequest<ResourceModel>,
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        val resourceModel = request.desiredResourceState
        if (!shouldUpdateTags(request)) {
            return ProgressEvent.progress(resourceModel, callbackContext)
        }

        val previousTags = getPreviouslyAttachedTags(request)
        val desiredTags = getNewDesiredTags(request)
        val tagsToAdd = generateTagsToAdd(previousTags, desiredTags)
        val tagsToRemove = generateTagsToRemove(previousTags, desiredTags)

        return untagResource(proxy, proxyClient, resourceModel, callbackContext, tagsToRemove, logger)
            .then { _ -> tagResource(proxy, proxyClient, resourceModel, callbackContext, tagsToAdd, logger) }
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Profile::Update"
    }
}
