package software.amazon.b2bi.capability

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.CreateCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.CreateCapabilityResponse
import software.amazon.b2bi.capability.TagHelper.convertToList
import software.amazon.b2bi.capability.TagHelper.getNewDesiredTags
import software.amazon.b2bi.capability.Translator.toCfnException
import software.amazon.b2bi.capability.Translator.toResourceCapabilityConfiguration
import software.amazon.b2bi.capability.Translator.toResourceS3Location
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

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
                    .makeServiceCall { awsRequest, client -> createCapability(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }

    private fun createCapability(
        request: CreateCapabilityRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): CreateCapabilityResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::createCapability)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        resourceModel.apply {
            capabilityId = response.capabilityId()
            capabilityArn = response.capabilityArn()
            createdAt = response.createdAt().toString()
        }
        logger.log("Successfully created ${ResourceModel.TYPE_NAME} ${resourceModel.capabilityId}")
        return response
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Capability::Create"
    }
}
