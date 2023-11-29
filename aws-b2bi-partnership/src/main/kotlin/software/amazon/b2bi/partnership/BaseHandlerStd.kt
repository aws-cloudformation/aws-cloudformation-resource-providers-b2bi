package software.amazon.b2bi.partnership


import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.b2bi.profile.ClientBuilder
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

// Placeholder for the functionality that could be shared across Create/Read/Update/Delete/List Handlers
abstract class BaseHandlerStd : BaseHandler<CallbackContext?>() {
    override fun handleRequest(
        proxy: AmazonWebServicesClientProxy,
        request: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext?,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        return handleRequest(
            proxy,
            request,
            callbackContext ?: CallbackContext(),
            proxy.newProxy(ClientBuilder::getClient),
            logger
        )
    }

    protected abstract fun handleRequest(
        proxy: AmazonWebServicesClientProxy,
        request: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext?,
        proxyClient: ProxyClient<B2BiClient>,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext?>
}