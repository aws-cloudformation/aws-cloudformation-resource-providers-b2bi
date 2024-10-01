package software.amazon.b2bi.partnership

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.UpdateCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.UpdateCapabilityResponse
import software.amazon.awssdk.services.b2bi.model.UpdatePartnershipRequest
import software.amazon.awssdk.services.b2bi.model.UpdatePartnershipResponse
import software.amazon.b2bi.partnership.TagHelper.convertToList
import software.amazon.b2bi.partnership.TagHelper.generateTagsToAdd
import software.amazon.b2bi.partnership.TagHelper.generateTagsToRemove
import software.amazon.b2bi.partnership.TagHelper.getNewDesiredTags
import software.amazon.b2bi.partnership.TagHelper.getPreviouslyAttachedTags
import software.amazon.b2bi.partnership.TagHelper.shouldUpdateTags
import software.amazon.b2bi.partnership.TagHelper.tagResource
import software.amazon.b2bi.partnership.TagHelper.untagResource
import software.amazon.b2bi.partnership.Translator.toCfnException
import software.amazon.b2bi.partnership.Translator.translateToResourceCapabilityOptions
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
                    .makeServiceCall { awsRequest, client -> updatePartnership(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> updateTags(proxy, proxyClient, progress.callbackContext, request) }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }

    private fun updatePartnership(
        request: UpdatePartnershipRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): UpdatePartnershipResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::updatePartnership)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        resourceModel.apply {
            profileId = response.profileId()
            partnershipArn = response.partnershipArn()
            name = response.name()
            email = response.email()
            phone = response.phone()
            capabilities = response.capabilities()
            capabilityOptions = response.capabilityOptions()?.translateToResourceCapabilityOptions()
            tradingPartnerId = response.tradingPartnerId()
            createdAt = response.createdAt().toString()
            modifiedAt = response.modifiedAt().toString()
        }
        logger.log("Successfully updated ${ResourceModel.TYPE_NAME} ${resourceModel.partnershipId}")
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
        private const val OPERATION = "AWS-B2BI-Partnership::Update"
    }
}