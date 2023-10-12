package software.amazon.b2bi.profile

import software.amazon.awssdk.awscore.AwsRequest
import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

class ListHandler : BaseHandler<CallbackContext?>() {
    override fun handleRequest(
        proxy: AmazonWebServicesClientProxy?,
        request: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext?,
        logger: Logger?
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        val models = listOf<ResourceModel>()

        // STEP 1 [TODO: construct a body of a request]
        val awsRequest: AwsRequest? = Translator.translateToListRequest(request.getNextToken())

        // STEP 2 [TODO: make an api call]
        val awsResponse: AwsResponse? = null // proxy.injectCredentialsAndInvokeV2(awsRequest, ClientBuilder.getClient()::describeLogGroups);

        // STEP 3 [TODO: get a token for the next page]
        val nextToken: String? = null

        // STEP 4 [TODO: construct resource models]
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/master/aws-logs-loggroup/src/main/java/software/amazon/logs/loggroup/ListHandler.java#L19-L21
        return ProgressEvent.builder<ResourceModel, CallbackContext>()
            .resourceModels(models)
                .nextToken(nextToken)
                .status(OperationStatus.SUCCESS)
                .build()
    }
}
