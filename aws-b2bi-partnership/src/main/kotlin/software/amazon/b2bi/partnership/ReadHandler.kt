package software.amazon.b2bi.partnership

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.GetPartnershipRequest
import software.amazon.awssdk.services.b2bi.model.GetPartnershipResponse
import software.amazon.b2bi.partnership.Translator.toCfnException
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

class ReadHandler : BaseHandlerStd() {
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
        return ProgressEvent.progress(resourceModel, callbackContext)
            .then { progress ->
                proxy.initiate(OPERATION, proxyClient, progress.resourceModel, progress.callbackContext)
                    .translateToServiceRequest(Translator::translateToReadRequest)
                    .makeServiceCall { awsRequest, client -> readPartnership(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }

    private fun readPartnership(
        request: GetPartnershipRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): GetPartnershipResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::getPartnership)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        updateResourceModel(resourceModel, response, proxyClient)
        logger.log("Successfully read ${ResourceModel.TYPE_NAME} ${resourceModel.partnershipId}")
        return response
    }

    private fun updateResourceModel(
        resourceModel: ResourceModel,
        getPartnershipResponse: GetPartnershipResponse,
        proxyClient: ProxyClient<B2BiClient>
    ) {
        val readResponseResourceModel = Translator.translateFromReadResponse(getPartnershipResponse)
        resourceModel.apply {
            profileId = readResponseResourceModel.profileId
            partnershipArn = readResponseResourceModel.partnershipArn
            name = readResponseResourceModel.name
            email = readResponseResourceModel.email
            phone = readResponseResourceModel.phone
            capabilities = readResponseResourceModel.capabilities
            capabilityOptions = readResponseResourceModel.capabilityOptions
            tradingPartnerId = readResponseResourceModel.tradingPartnerId
            createdAt = readResponseResourceModel.createdAt
            modifiedAt = readResponseResourceModel.modifiedAt
        }
        resourceModel.tags = TagHelper.listTagsForResource(getPartnershipResponse.partnershipArn(), proxyClient)
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Partnership::Read"
    }
}