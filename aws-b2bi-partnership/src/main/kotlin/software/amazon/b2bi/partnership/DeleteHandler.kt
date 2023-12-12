package software.amazon.b2bi.partnership

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.DeletePartnershipRequest
import software.amazon.awssdk.services.b2bi.model.DeletePartnershipResponse
import software.amazon.b2bi.partnership.Translator.toCfnException
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

class DeleteHandler : BaseHandlerStd() {
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
                    .translateToServiceRequest(Translator::translateToDeleteRequest)
                    .makeServiceCall { awsRequest, client -> deletePartnership(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { _ -> ProgressEvent.defaultSuccessHandler(null) }
    }

    private fun deletePartnership(
        request: DeletePartnershipRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): DeletePartnershipResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::deletePartnership)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        logger.log("Successfully deleted ${ResourceModel.TYPE_NAME} ${resourceModel.partnershipId}")
        return response
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Partnership::Delete"
    }
}