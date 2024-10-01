package software.amazon.b2bi.transformer

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.GetTransformerRequest
import software.amazon.awssdk.services.b2bi.model.GetTransformerResponse
import software.amazon.b2bi.transformer.Translator.toCfnException
import software.amazon.cloudformation.proxy.*

class ReadHandler : BaseHandlerStd() {
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
                    .translateToServiceRequest(Translator::translateToReadRequest)
                    .makeServiceCall { awsRequest, client -> readTransformer(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }

    }

    private fun readTransformer(
        request: GetTransformerRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): GetTransformerResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::getTransformer)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        updateResourceModel(resourceModel, response, proxyClient)
        logger.log("Successfully read ${ResourceModel.TYPE_NAME} ${resourceModel.transformerId}")
        return response
    }

    private fun updateResourceModel(
        resourceModel: ResourceModel,
        getTransformerResponse: GetTransformerResponse,
        proxyClient: ProxyClient<B2BiClient>
    ) {
        val readResponseResourceModel = Translator.translateFromReadResponse(getTransformerResponse)
        resourceModel.apply {
            transformerArn = readResponseResourceModel.transformerArn
            name = readResponseResourceModel.name
            fileFormat = readResponseResourceModel.fileFormat
            mappingTemplate = readResponseResourceModel.mappingTemplate
            status =  readResponseResourceModel.status
            ediType = readResponseResourceModel.ediType
            sampleDocument = readResponseResourceModel.sampleDocument
            inputConversion = readResponseResourceModel.inputConversion
            outputConversion = readResponseResourceModel.outputConversion
            mapping = readResponseResourceModel.mapping
            sampleDocuments = readResponseResourceModel.sampleDocuments
            createdAt = readResponseResourceModel.createdAt
            modifiedAt = readResponseResourceModel.modifiedAt
        }
        resourceModel.tags = TagHelper.listTagsForResource(getTransformerResponse.transformerArn(), proxyClient)
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Transformer::Read"
    }
}
