package software.amazon.b2bi.profile

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.GetProfileRequest
import software.amazon.awssdk.services.b2bi.model.GetProfileResponse
import software.amazon.b2bi.profile.Translator.toCfnException
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

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
                    .makeServiceCall { awsRequest, client -> readProfile(awsRequest, client, resourceModel) }
                    .progress()
            }
            .then { progress -> ProgressEvent.defaultSuccessHandler(progress.resourceModel) }
    }

    private fun readProfile(
        request: GetProfileRequest,
        proxyClient: ProxyClient<B2BiClient>,
        resourceModel: ResourceModel
    ): GetProfileResponse {
        val response = try {
            proxyClient.injectCredentialsAndInvokeV2(request, proxyClient.client()::getProfile)
        } catch (e: AwsServiceException) {
            throw e.toCfnException()
        }
        updateResourceModel(resourceModel, response, proxyClient)
        logger.log("Successfully read ${ResourceModel.TYPE_NAME} ${resourceModel.profileId}")
        return response
    }

    private fun updateResourceModel(
        resourceModel: ResourceModel,
        getProfileResponse: GetProfileResponse,
        proxyClient: ProxyClient<B2BiClient>
    ) {
        val readResponseResourceModel = Translator.translateFromReadResponse(getProfileResponse)
        resourceModel.apply {
            profileArn = readResponseResourceModel.profileArn
            name = readResponseResourceModel.name
            email = readResponseResourceModel.email
            phone = readResponseResourceModel.phone
            businessName = readResponseResourceModel.businessName
            logging = readResponseResourceModel.logging
            logGroupName = readResponseResourceModel.logGroupName
            createdAt = readResponseResourceModel.createdAt
            modifiedAt = readResponseResourceModel.modifiedAt
        }
        resourceModel.tags = TagHelper.listTagsForResource(getProfileResponse.profileArn(), proxyClient)
    }

    companion object {
        private const val OPERATION = "AWS-B2BI-Profile::Read"
    }
}
