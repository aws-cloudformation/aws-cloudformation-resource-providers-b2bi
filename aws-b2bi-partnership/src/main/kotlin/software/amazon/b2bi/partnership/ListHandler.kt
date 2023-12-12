package software.amazon.b2bi.partnership

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
        val listPartnershipsRequest = Translator.translateToListRequest(request.nextToken)
        val listPartnershipsResponse = proxy.injectCredentialsAndInvokeV2(
            listPartnershipsRequest,
            ClientBuilder.getClient()::listPartnerships
        )
        logger.log("Successfully listed ${ResourceModel.TYPE_NAME}")
        val resourceModels = Translator.translateFromListResponse(listPartnershipsResponse)
        return ProgressEvent.builder<ResourceModel, CallbackContext>()
            .resourceModels(resourceModels)
            .nextToken(listPartnershipsResponse.nextToken())
            .status(OperationStatus.SUCCESS)
            .build()
    }
}