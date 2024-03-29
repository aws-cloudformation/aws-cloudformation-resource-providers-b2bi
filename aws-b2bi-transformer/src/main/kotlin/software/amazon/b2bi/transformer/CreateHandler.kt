package software.amazon.b2bi.transformer

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.cloudformation.proxy.*
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.CreateTransformerRequest
import software.amazon.awssdk.services.b2bi.model.CreateTransformerResponse
import software.amazon.awssdk.services.b2bi.model.TransformerStatus
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerRequest
import software.amazon.b2bi.transformer.TagHelper.convertToList
import software.amazon.b2bi.transformer.TagHelper.getNewDesiredTags
import software.amazon.b2bi.transformer.Translator.toCfnException

class CreateHandler : BaseHandlerStd() {
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
        val newDesiredTags = getNewDesiredTags(request)
        resourceModel.tags = convertToList(newDesiredTags)

        return ProgressEvent.progress(resourceModel, callbackContext)
            .then { progress ->
                proxy.initiate(OPERATION, proxyClient, progress.resourceModel, progress.callbackContext)
                    .translateToServiceRequest(Translator::translateToCreateRequest)
                    .makeServiceCall { awsRequest, client -> createTransformer(awsRequest, client, resourceModel) }
                    .stabilize { _, _, client, resourceModel, _ -> stabilizeTransformer(resourceModel, client) }
                    .progress()
            }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }

    private fun createTransformer(
        request: CreateTransformerRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): CreateTransformerResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::createTransformer)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        resourceModel.apply {
            transformerId = response.transformerId()
            transformerArn = response.transformerArn()
            createdAt = response.createdAt().toString()
        }
        logger.log("Successfully created ${ResourceModel.TYPE_NAME} ${resourceModel.transformerId}")
        return response
    }

    private fun stabilizeTransformer(
        resourceModel: ResourceModel,
        proxyClient: ProxyClient<B2BiClient>,
    ): Boolean {
        val requestedTransformerStatus = resourceModel.status
        if (requestedTransformerStatus != TransformerStatus.ACTIVE.toString()) {
            resourceModel.apply {
                status = TransformerStatus.INACTIVE.toString()
            }
            return true
        }

        val updateTransformerRequest = Translator.translateToUpdateRequest(resourceModel)
        val updateTransformerResponse = try {
            proxyClient.injectCredentialsAndInvokeV2(updateTransformerRequest, proxyClient.client()::updateTransformer)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        resourceModel.apply {
            status = updateTransformerResponse.statusAsString()
        }
        return true
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Transformer::Create"
    }
}
