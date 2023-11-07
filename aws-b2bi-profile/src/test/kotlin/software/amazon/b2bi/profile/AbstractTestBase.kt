package software.amazon.b2bi.profile

import software.amazon.awssdk.awscore.AwsRequest
import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.core.ResponseBytes
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.pagination.sync.SdkIterable
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.B2BiResponse
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Credentials
import software.amazon.cloudformation.proxy.LoggerProxy
import software.amazon.cloudformation.proxy.ProxyClient
import java.util.concurrent.CompletableFuture
import java.util.function.Function

abstract class AbstractTestBase {
    internal val mockCredentials: Credentials = Credentials("accessKey", "secretKey", "token")
    internal val logger: LoggerProxy = LoggerProxy()

    fun mockProxy(
        proxy: AmazonWebServicesClientProxy,
        b2BiClient: B2BiClient
    ): ProxyClient<B2BiClient> {
        return object : ProxyClient<B2BiClient> {
            override fun <RequestT: AwsRequest?, ResponseT: AwsResponse?> injectCredentialsAndInvokeV2(
                request: RequestT,
                requestFunction: Function<RequestT, ResponseT>?
            ): ResponseT {
                return proxy.injectCredentialsAndInvokeV2(request, requestFunction)
            }

            override fun <RequestT: AwsRequest?, ResponseT: AwsResponse?> injectCredentialsAndInvokeV2Async(
                request: RequestT,
                requestFunction: Function<RequestT, CompletableFuture<ResponseT>?>?
            ): CompletableFuture<ResponseT> {
                throw UnsupportedOperationException()
            }

            override fun <RequestT: AwsRequest?, ResponseT: AwsResponse?, IterableT: SdkIterable<ResponseT>?> injectCredentialsAndInvokeIterableV2(
                request: RequestT,
                requestFunction: Function<RequestT, IterableT>?
            ): IterableT {
                return proxy.injectCredentialsAndInvokeIterableV2(request, requestFunction)
            }

            override fun <RequestT: AwsRequest?, ResponseT: AwsResponse?> injectCredentialsAndInvokeV2InputStream(
                requestT: RequestT,
                function: Function<RequestT, ResponseInputStream<ResponseT>?>?
            ): ResponseInputStream<ResponseT> {
                throw UnsupportedOperationException()
            }

            override fun <RequestT: AwsRequest?, ResponseT: AwsResponse?> injectCredentialsAndInvokeV2Bytes(
                requestT: RequestT,
                function: Function<RequestT, ResponseBytes<ResponseT>?>?
            ): ResponseBytes<ResponseT> {
                throw UnsupportedOperationException()
            }

            override fun client(): B2BiClient {
                return b2BiClient
            }
        }
    }
}
