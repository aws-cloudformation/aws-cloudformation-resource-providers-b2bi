package software.amazon.b2bi.capability

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import software.amazon.awssdk.core.SdkClient
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import java.time.Duration

@ExtendWith(MockitoExtension::class)
class UpdateHandlerTest : AbstractTestBase() {
    @Mock
    private var proxy: AmazonWebServicesClientProxy? = null

    @Mock
    private var proxyClient: ProxyClient<SdkClient>? = null

    @Mock
    var sdkClient: SdkClient? = null
    @BeforeEach
    fun setup() {
        proxy = AmazonWebServicesClientProxy(
            AbstractTestBase.Companion.logger,
            AbstractTestBase.Companion.MOCK_CREDENTIALS
        ) { Duration.ofSeconds(600).toMillis() }
        sdkClient = Mockito.mock(SdkClient::class.java)
        proxyClient = AbstractTestBase.Companion.MOCK_PROXY(proxy!!, sdkClient)
    }

    @AfterEach
    fun tear_down() {
        Mockito.verify(sdkClient, Mockito.atLeastOnce()).serviceName()
        Mockito.verifyNoMoreInteractions(sdkClient)
    }

    @Test
    fun handleRequest_SimpleSuccess() {
        val handler = UpdateHandler()
        val model = ResourceModel.builder().build()
        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(model)
            .build()
        val response =
            handler.handleRequest(proxy, request, CallbackContext(), proxyClient, AbstractTestBase.Companion.logger)
        Assertions.assertThat(response).isNotNull()
        Assertions.assertThat(response.status).isEqualTo(OperationStatus.SUCCESS)
        Assertions.assertThat(response.callbackDelaySeconds).isEqualTo(0)
        Assertions.assertThat(response.resourceModel).isEqualTo(request.desiredResourceState)
        Assertions.assertThat(response.resourceModels).isNull()
        Assertions.assertThat(response.message).isNull()
        Assertions.assertThat(response.errorCode).isNull()
    }
}
