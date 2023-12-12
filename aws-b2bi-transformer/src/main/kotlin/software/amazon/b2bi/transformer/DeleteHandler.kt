package software.amazon.b2bi.transformer

import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.DeleteTransformerRequest
import software.amazon.awssdk.services.b2bi.model.DeleteTransformerResponse
import software.amazon.b2bi.transformer.Translator.toCfnException
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.proxy.*

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
        val newDesiredTags = TagHelper.getNewDesiredTags(request)
        resourceModel.tags = TagHelper.convertToList(newDesiredTags)

        return ProgressEvent.progress(resourceModel, callbackContext)
            .then { progress ->
                proxy.initiate(OPERATION, proxyClient, progress.resourceModel, progress.callbackContext)
                    .translateToServiceRequest(Translator::translateToDeleteRequest)
                    .makeServiceCall { awsRequest, client -> deleteTransformer(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }
    private fun deleteTransformer(
        request: DeleteTransformerRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): DeleteTransformerResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::deleteTransformer)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        logger.log("Successfully deleted ${ResourceModel.TYPE_NAME} ${resourceModel.transformerId}")
        return response
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Transformer::Delete"
    }
}
