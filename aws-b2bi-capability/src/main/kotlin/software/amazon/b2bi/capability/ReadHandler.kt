package software.amazon.b2bi.capability

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.GetCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.GetCapabilityResponse
import software.amazon.b2bi.capability.Translator.toCfnException
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
                    .makeServiceCall { awsRequest, client -> readCapability(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }

    private fun readCapability(
        request: GetCapabilityRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): GetCapabilityResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::getCapability)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        updateResourceModel(resourceModel, response, proxyClient)
        logger.log("Successfully read ${ResourceModel.TYPE_NAME} ${resourceModel.capabilityId}")
        return response
    }

    private fun updateResourceModel(
        resourceModel: ResourceModel,
        getCapabilityResponse: GetCapabilityResponse,
        proxyClient: ProxyClient<B2BiClient>
    ) {
        val readResponseResourceModel = Translator.translateFromReadResponse(getCapabilityResponse)
        resourceModel.apply {
            capabilityArn = readResponseResourceModel.capabilityArn
            name = readResponseResourceModel.name
            type = readResponseResourceModel.type
            configuration = readResponseResourceModel.configuration
            instructionsDocuments = readResponseResourceModel.instructionsDocuments
            createdAt = readResponseResourceModel.createdAt
            modifiedAt = readResponseResourceModel.modifiedAt
        }
        resourceModel.tags = TagHelper.listTagsForResource(getCapabilityResponse.capabilityArn(), proxyClient)
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Capability::Read"
    }
}