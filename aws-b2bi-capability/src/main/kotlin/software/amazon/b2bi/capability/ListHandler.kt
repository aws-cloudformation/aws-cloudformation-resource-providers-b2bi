package software.amazon.b2bi.capability

import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

class ListHandler : BaseHandler<CallbackContext?>() {
    override fun handleRequest(
        proxy: AmazonWebServicesClientProxy,
        request: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext?,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        val listCapabilitiesRequest = Translator.translateToListRequest(request.nextToken)
        val listCapabilitiesResponse = proxy.injectCredentialsAndInvokeV2(
            listCapabilitiesRequest,
            ClientBuilder.getClient()::listCapabilities
        )
        logger.log("Successfully listed ${ResourceModel.TYPE_NAME}")
        val resourceModels = Translator.translateFromListResponse(listCapabilitiesResponse)
        return ProgressEvent.builder<ResourceModel, CallbackContext>()
            .resourceModels(resourceModels)
            .nextToken(listCapabilitiesResponse.nextToken())
            .status(OperationStatus.SUCCESS)
            .build()
    }
}