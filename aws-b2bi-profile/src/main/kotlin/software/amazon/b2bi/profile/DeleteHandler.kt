package software.amazon.b2bi.profile

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.DeleteProfileRequest
import software.amazon.awssdk.services.b2bi.model.DeleteProfileResponse
import software.amazon.b2bi.profile.Translator.toCfnException
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
                    .makeServiceCall { awsRequest, client -> deleteProfile(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { _ -> ProgressEvent.defaultSuccessHandler(null) }
    }

    private fun deleteProfile(
        request: DeleteProfileRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): DeleteProfileResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::deleteProfile)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        logger.log("Successfully deleted ${ResourceModel.TYPE_NAME} ${resourceModel.profileId}")
        return response
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Profile::Delete"
    }
}
