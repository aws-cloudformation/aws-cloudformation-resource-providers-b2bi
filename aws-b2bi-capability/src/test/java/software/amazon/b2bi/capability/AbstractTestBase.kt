package software.amazon.b2bi.capability

import software.amazon.awssdk.awscore.AwsRequest
import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.core.ResponseBytes
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.SdkClient
import software.amazon.awssdk.core.pagination.sync.SdkIterable
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Credentials
import software.amazon.cloudformation.proxy.LoggerProxy
import software.amazon.cloudformation.proxy.ProxyClient
import java.util.concurrent.CompletableFuture
import java.util.function.Function

open class AbstractTestBase {
    companion object {
        protected val MOCK_CREDENTIALS: Credentials? = null
        protected val logger: LoggerProxy? = null

        init {
            MOCK_CREDENTIALS = Credentials("accessKey", "secretKey", "token")
            logger = LoggerProxy()
        }

        fun MOCK_PROXY(
            proxy: AmazonWebServicesClientProxy,
            sdkClient: SdkClient?
        ): ProxyClient<SdkClient> {
            return object : ProxyClient<SdkClient?> {
                override fun <RequestT : AwsRequest?, ResponseT : AwsResponse?> injectCredentialsAndInvokeV2(
                    request: RequestT,
                    requestFunction: Function<RequestT, ResponseT>
                ): ResponseT {
                    return proxy.injectCredentialsAndInvokeV2(request, requestFunction)
                }

                override fun <RequestT : AwsRequest?, ResponseT : AwsResponse?> injectCredentialsAndInvokeV2Async(
                    request: RequestT,
                    requestFunction: Function<RequestT, CompletableFuture<ResponseT>>
                ): CompletableFuture<ResponseT> {
                    throw UnsupportedOperationException()
                }

                override fun <RequestT : AwsRequest?, ResponseT : AwsResponse?, IterableT : SdkIterable<ResponseT>?> injectCredentialsAndInvokeIterableV2(
                    request: RequestT,
                    requestFunction: Function<RequestT, IterableT>
                ): IterableT {
                    return proxy.injectCredentialsAndInvokeIterableV2(request, requestFunction)
                }

                override fun <RequestT : AwsRequest?, ResponseT : AwsResponse?> injectCredentialsAndInvokeV2InputStream(
                    requestT: RequestT,
                    function: Function<RequestT, ResponseInputStream<ResponseT>>
                ): ResponseInputStream<ResponseT> {
                    throw UnsupportedOperationException()
                }

                override fun <RequestT : AwsRequest?, ResponseT : AwsResponse?> injectCredentialsAndInvokeV2Bytes(
                    requestT: RequestT,
                    function: Function<RequestT, ResponseBytes<ResponseT>>
                ): ResponseBytes<ResponseT> {
                    throw UnsupportedOperationException()
                }

                override fun client(): SdkClient? {
                    return sdkClient
                }
            }
        }
    }
}
