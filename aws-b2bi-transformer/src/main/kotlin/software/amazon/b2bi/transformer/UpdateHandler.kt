package software.amazon.b2bi.transformer

import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.core.SdkClient
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerRequest
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerResponse
import software.amazon.b2bi.transformer.EdiHelper.translateToResourceEdi
import software.amazon.b2bi.transformer.TagHelper.convertToList
import software.amazon.b2bi.transformer.TagHelper.generateTagsToAdd
import software.amazon.b2bi.transformer.TagHelper.generateTagsToRemove
import software.amazon.b2bi.transformer.TagHelper.getNewDesiredTags
import software.amazon.b2bi.transformer.TagHelper.getPreviouslyAttachedTags
import software.amazon.b2bi.transformer.TagHelper.shouldUpdateTags
import software.amazon.b2bi.transformer.TagHelper.tagResource
import software.amazon.b2bi.transformer.TagHelper.untagResource
import software.amazon.b2bi.transformer.Translator.toCfnException
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.proxy.*

class UpdateHandler : BaseHandlerStd() {
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
        resourceModel.tags = convertToList(getNewDesiredTags(request))
        return ProgressEvent.progress(resourceModel, callbackContext)
            .then { progress ->
                proxy.initiate(OPERATION, proxyClient, progress.resourceModel, progress.callbackContext)
                    .translateToServiceRequest(Translator::translateToUpdateRequest)
                    .makeServiceCall { awsRequest, client -> updateTransformer(awsRequest, client, resourceModel) }
                    .progress()

            }
            .then { progress -> updateTags(proxy, proxyClient, progress.callbackContext, request) }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }

    }

    private fun updateTransformer(
        request: UpdateTransformerRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): UpdateTransformerResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::updateTransformer)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        resourceModel.apply {
            transformerArn = response.transformerArn()
            mappingTemplate = response.mappingTemplate()
            fileFormat = response.fileFormat().toString()
            ediType = response.ediType().translateToResourceEdi()
            sampleDocument = response.sampleDocument()
            name = response.name()
            createdAt = response.createdAt().toString()
            status = response.status().toString()
            modifiedAt = response.modifiedAt().toString()
        }
        logger.log("Successfully updated ${ResourceModel.TYPE_NAME} ${resourceModel.transformerId}")
        return response
    }

    private fun updateTags(
        proxy: AmazonWebServicesClientProxy,
        proxyClient: ProxyClient<B2BiClient>,
        callbackContext: CallbackContext?,
        request: ResourceHandlerRequest<ResourceModel>,
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        val resourceModel = request.desiredResourceState
        if (!shouldUpdateTags(request)) {
            return ProgressEvent.progress(resourceModel, callbackContext)
        }

        val previousTags = getPreviouslyAttachedTags(request)
        val desiredTags = getNewDesiredTags(request)
        val tagsToAdd = generateTagsToAdd(previousTags, desiredTags)
        val tagsToRemove = generateTagsToRemove(previousTags, desiredTags)

        return untagResource(proxy, proxyClient, resourceModel, callbackContext, tagsToRemove, logger)
            .then { _ -> tagResource(proxy, proxyClient, resourceModel, callbackContext, tagsToAdd, logger) }
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Transformer::Update"
    }

}
