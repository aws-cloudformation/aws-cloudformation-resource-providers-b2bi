package software.amazon.b2bi.partnership

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import java.time.Duration

@ExtendWith(MockitoExtension::class)
class ReadHandlerTest : AbstractTestBase() {
    @Mock
    private lateinit var proxy: AmazonWebServicesClientProxy

    @Mock
    private lateinit var proxyClient: ProxyClient<B2BiClient>

    @Mock
    private lateinit var sdkClient: B2BiClient
    @BeforeEach
    fun setup() {
        proxy = AmazonWebServicesClientProxy(logger, MOCK_CREDENTIALS) { Duration.ofSeconds(600).toMillis() }
        sdkClient = Mockito.mock(B2BiClient::class.java)
        proxyClient = MOCK_PROXY(proxy!!, sdkClient)
    }

    @AfterEach
    fun tear_down() {
        Mockito.verify(sdkClient, Mockito.atLeastOnce()).serviceName()
        Mockito.verifyNoMoreInteractions(sdkClient)
    }

    @Test
    fun handleRequest_SimpleSuccess() {
        val handler = ReadHandler()
        val model = ResourceModel.builder().build()
        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(model)
            .build()
        val response = handler.handleRequest(proxy, request, CallbackContext(), proxyClient, logger)
        Assertions.assertThat(response).isNotNull
        Assertions.assertThat(response.status).isEqualTo(OperationStatus.SUCCESS)
        Assertions.assertThat(response.callbackDelaySeconds).isEqualTo(0)
        Assertions.assertThat(response.resourceModel).isEqualTo(request.desiredResourceState)
        Assertions.assertThat(response.resourceModels).isNull()
        Assertions.assertThat(response.message).isNull()
        Assertions.assertThat(response.errorCode).isNull()
    }
}