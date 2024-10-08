package software.amazon.b2bi.transformer

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerRequest
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerResponse
import software.amazon.b2bi.transformer.TagHelper.convertToList
import software.amazon.b2bi.transformer.TagHelper.generateTagsToAdd
import software.amazon.b2bi.transformer.TagHelper.generateTagsToRemove
import software.amazon.b2bi.transformer.TagHelper.getNewDesiredTags
import software.amazon.b2bi.transformer.TagHelper.getPreviouslyAttachedTags
import software.amazon.b2bi.transformer.TagHelper.shouldUpdateTags
import software.amazon.b2bi.transformer.TagHelper.tagResource
import software.amazon.b2bi.transformer.TagHelper.untagResource
import software.amazon.b2bi.transformer.Translator.toCfnException
import software.amazon.b2bi.transformer.Translator.translateToResourceEdi
import software.amazon.b2bi.transformer.Translator.translateToResourceInputConversion
import software.amazon.b2bi.transformer.Translator.translateToResourceMapping
import software.amazon.b2bi.transformer.Translator.translateToResourceOutputConversion
import software.amazon.b2bi.transformer.Translator.translateToResourceSampleDocuments
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

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
            name = response.name()
            createdAt = response.createdAt().toString()
            status = response.status().toString()
            modifiedAt = response.modifiedAt().toString()

            // old fields
            mappingTemplate = response.mappingTemplate()
            fileFormat = response.fileFormatAsString()
            ediType = response.ediType()?.translateToResourceEdi()
            sampleDocument = response.sampleDocument()

            // new fields
            inputConversion = response.inputConversion()?.translateToResourceInputConversion()
            outputConversion = response.outputConversion()?.translateToResourceOutputConversion()
            mapping = response.mapping()?.translateToResourceMapping()
            sampleDocuments = response.sampleDocuments()?.translateToResourceSampleDocuments()
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
