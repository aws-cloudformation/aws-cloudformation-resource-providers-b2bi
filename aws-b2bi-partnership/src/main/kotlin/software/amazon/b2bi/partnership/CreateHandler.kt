package software.amazon.b2bi.partnership

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.CreatePartnershipRequest
import software.amazon.awssdk.services.b2bi.model.CreatePartnershipResponse
import software.amazon.b2bi.partnership.TagHelper.convertToList
import software.amazon.b2bi.partnership.TagHelper.getNewDesiredTags
import software.amazon.b2bi.partnership.Translator.toCfnException
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
                    .makeServiceCall { awsRequest, client -> createPartnership(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }

    private fun createPartnership(
        request: CreatePartnershipRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): CreatePartnershipResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::createPartnership)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        resourceModel.apply {
            partnershipId = response.partnershipId()
            partnershipArn = response.partnershipArn()
            createdAt = response.createdAt().toString()
        }
        logger.log("Successfully created ${ResourceModel.TYPE_NAME} ${resourceModel.partnershipId}")
        return response
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Partnership::Create"
    }
}