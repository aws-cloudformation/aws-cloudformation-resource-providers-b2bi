package software.amazon.b2bi.profile

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
        val listProfilesRequest = Translator.translateToListRequest(request.nextToken)
        val listProfilesResponse = proxy.injectCredentialsAndInvokeV2(
            listProfilesRequest,
            ClientBuilder.getClient()::listProfiles
        )
        logger.log("Successfully listed ${ResourceModel.TYPE_NAME}")
        val resourceModels = Translator.translateFromListResponse(listProfilesResponse)
        return ProgressEvent.builder<ResourceModel, CallbackContext>()
            .resourceModels(resourceModels)
            .nextToken(listProfilesResponse.nextToken())
            .status(OperationStatus.SUCCESS)
            .build()
    }
}
