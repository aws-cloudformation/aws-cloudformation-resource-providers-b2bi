package software.amazon.b2bi.transformer

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
        val listTransformersRequest = Translator.translateToListRequest(request.nextToken)
        val listTransformersResponse = proxy.injectCredentialsAndInvokeV2(
            listTransformersRequest,
            ClientBuilder.getClient()::listTransformers
        )
        logger.log("Successfully listed ${ResourceModel.TYPE_NAME}")
        val resourceModels = Translator.translateFromListResponse(listTransformersResponse)
        return ProgressEvent.builder<ResourceModel, CallbackContext>()
            .resourceModels(resourceModels)
            .nextToken(listTransformersResponse.nextToken())
            .status(OperationStatus.SUCCESS)
            .build()
    }
}
