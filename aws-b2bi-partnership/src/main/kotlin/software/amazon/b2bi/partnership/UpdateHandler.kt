package software.amazon.b2bi.partnership

import software.amazon.awssdk.awscore.AwsRequest
import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.awscore.exception.AwsServiceException

import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import java.util.function.Function

// TODO: replace all usage of B2BiClient with your service client type, e.g; YourServiceAsyncClient
// import software.amazon.awssdk.services.yourservice.YourServiceAsyncClient;
class UpdateHandler : BaseHandlerStd() {
    private var logger: Logger? = null
    public override fun handleRequest(
        proxy: AmazonWebServicesClientProxy,
        request: ResourceHandlerRequest<ResourceModel>,
        callbackContext: CallbackContext?,
        proxyClient: ProxyClient<B2BiClient>,
        logger: Logger
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        this.logger = logger

        // TODO: Adjust Progress Chain according to your implementation
        // https://github.com/aws-cloudformation/cloudformation-cli-java-plugin/blob/master/src/main/java/software/amazon/cloudformation/proxy/CallChain.java
        return ProgressEvent.progress<ResourceModel, CallbackContext?>(
            request!!.desiredResourceState, callbackContext
        ) // STEP 1 [check if resource already exists]
            // for more information -> https://docs.aws.amazon.com/cloudformation-cli/latest/userguide/resource-type-test-contract.html
            // if target API does not support 'ResourceNotFoundException' then following check is required
            .then { progress: ProgressEvent<ResourceModel, CallbackContext?> ->  // STEP 1.0 [initialize a proxy context]
                // If your service API does not return ResourceNotFoundException on update requests against some identifier (e.g; resource Name)
                // and instead returns a 200 even though a resource does not exist, you must first check if the resource exists here
                // NOTE: If your service API throws 'ResourceNotFoundException' for update requests this method is not necessary
                proxy!!.initiate<B2BiClient, ResourceModel, CallbackContext?>(
                    "AWS-B2BI-Partnership::Update::PreUpdateCheck",
                    proxyClient,
                    progress.resourceModel,
                    progress.callbackContext
                ) // STEP 1.1 [initialize a proxy context]
                    .translateToServiceRequest{ obj: ResourceModel -> Translator.translateToReadRequest(obj) } // STEP 1.2 [TODO: make an api call]
                    .makeServiceCall<AwsResponse?> { awsRequest: AwsRequest?, client: ProxyClient<B2BiClient>? ->
                        val awsResponse: AwsResponse? = null

                        // TODO: add custom read resource logic
                        // If describe request does not return ResourceNotFoundException, you must throw ResourceNotFoundException based on
                        // awsResponse values
                        logger!!.log(String.format("%s has successfully been read.", ResourceModel.TYPE_NAME))
                        awsResponse
                    }
                    .progress()
            } // STEP 2 [first update/stabilize progress chain - required for resource update]
            .then { progress: ProgressEvent<ResourceModel, CallbackContext?> ->  // STEP 2.0 [initialize a proxy context]
                // Implement client invocation of the update request through the proxyClient, which is already initialised with
                // caller credentials, correct region and retry settings
                proxy!!.initiate<B2BiClient, ResourceModel, CallbackContext?>(
                    "AWS-B2BI-Partnership::Update::first",
                    proxyClient,
                    progress.resourceModel,
                    progress.callbackContext
                ) // STEP 2.1 [TODO: construct a body of a request]
                    .translateToServiceRequest{ obj: ResourceModel -> Translator.translateToFirstUpdateRequest(obj) } // STEP 2.2 [TODO: make an api call]
                    .makeServiceCall<AwsResponse?> { awsRequest: AwsRequest?, client: ProxyClient<B2BiClient>? ->
                        val awsResponse: AwsResponse? = null
                        try {

                            // TODO: put your update resource code here
                        } catch (e: AwsServiceException) {
                            /*
                            * While the handler contract states that the handler must always return a progress event,
                            * you may throw any instance of BaseHandlerException, as the wrapper map it to a progress event.
                            * Each BaseHandlerException maps to a specific error code, and you should map service exceptions as closely as possible
                            * to more specific error codes
                            */
                            throw CfnGeneralServiceException(ResourceModel.TYPE_NAME, e)
                        }
                        logger!!.log(String.format("%s has successfully been updated.", ResourceModel.TYPE_NAME))
                        awsResponse
                    } // STEP 2.3 [TODO: stabilize step is not necessarily required but typically involves describing the resource until it is in a certain status, though it can take many forms]
                    // stabilization step may or may not be needed after each API call
                    // for more information -> https://docs.aws.amazon.com/cloudformation-cli/latest/userguide/resource-type-test-contract.html
                    .stabilize { awsRequest: AwsRequest?, awsResponse: AwsResponse?, client: ProxyClient<B2BiClient>?, model: ResourceModel, context: CallbackContext? ->
                        // TODO: put your stabilization code here
                        val stabilized = true
                        logger!!.log(
                            String.format(
                                "%s [%s] update has stabilized: %s",
                                ResourceModel.TYPE_NAME,
                                model.primaryIdentifier,
                                stabilized
                            )
                        )
                        stabilized
                    }
                    .progress()
            } // If your resource is provisioned through multiple API calls, then the following pattern is required (and might take as many postUpdate callbacks as necessary)
            // STEP 3 [second update/stabilize progress chain]
            .then { progress: ProgressEvent<ResourceModel, CallbackContext?> ->  // STEP 3.0 [initialize a proxy context]
                // If your resource is provisioned through multiple API calls, you will need to apply each subsequent update
                // step in a discrete call/stabilize chain to ensure the entire resource is provisioned as intended.
                proxy!!.initiate<B2BiClient, ResourceModel, CallbackContext?>(
                    "AWS-B2BI-Partnership::Update::second",
                    proxyClient,
                    progress.resourceModel,
                    progress.callbackContext
                ) // STEP 3.1 [TODO: construct a body of a request]
                    .translateToServiceRequest{ obj: ResourceModel -> Translator.translateToSecondUpdateRequest(obj) } // STEP 3.2 [TODO: make an api call]
                    .makeServiceCall<AwsResponse?> { awsRequest: AwsRequest?, client: ProxyClient<B2BiClient>? ->
                        val awsResponse: AwsResponse? = null
                        try {

                            // TODO: put your post update resource code here
                        } catch (e: AwsServiceException) {
                            /*
                            * While the handler contract states that the handler must always return a progress event,
                            * you may throw any instance of BaseHandlerException, as the wrapper map it to a progress event.
                            * Each BaseHandlerException maps to a specific error code, and you should map service exceptions as closely as possible
                            * to more specific error codes
                            */
                            throw CfnGeneralServiceException(ResourceModel.TYPE_NAME, e)
                        }
                        logger!!.log(String.format("%s has successfully been updated.", ResourceModel.TYPE_NAME))
                        awsResponse
                    }
                    .progress()
            } // STEP 4 [TODO: describe call/chain to return the resource model]
            .then { progress: ProgressEvent<ResourceModel, CallbackContext?>? ->
                ReadHandler().handleRequest(
                    proxy,
                    request,
                    callbackContext,
                    proxyClient,
                    logger
                )
            }
    }
}