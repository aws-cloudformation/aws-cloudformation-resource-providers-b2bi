package software.amazon.b2bi.profile

import software.amazon.awssdk.core.SdkClient
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

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

    internal abstract fun handleRequest(
        proxy: AmazonWebServicesClientProxy,
        request: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext?,
        proxyClient: ProxyClient<SdkClient>,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext?>
}
