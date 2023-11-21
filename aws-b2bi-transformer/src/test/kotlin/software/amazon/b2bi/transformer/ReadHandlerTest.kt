package software.amazon.b2bi.transformer

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import software.amazon.awssdk.core.SdkClient
import software.amazon.cloudformation.proxy.*
import java.time.Duration
import java.util.function.Supplier

@ExtendWith(MockitoExtension::class)
class ReadHandlerTest : AbstractTestBase() {
    @Mock
    private lateinit var proxy: AmazonWebServicesClientProxy

    @Mock
    private var proxyClient: ProxyClient<SdkClient>? = null

    @Mock
    lateinit var sdkClient: SdkClient
    @BeforeEach
    fun setup() {
        proxy = AmazonWebServicesClientProxy(
            logger,
            MOCK_CREDENTIALS,
            Supplier<Long> { Duration.ofSeconds(600).toMillis() })
        sdkClient = mock(SdkClient::class.java)
        proxyClient = MOCK_PROXY(proxy, sdkClient)
    }

    @AfterEach
    fun tear_down() {
        verify(sdkClient, atLeastOnce())?.serviceName()
        verifyNoMoreInteractions(sdkClient)
    }

    @Test
    fun handleRequest_SimpleSuccess() {
        val handler = ReadHandler()
        val model: ResourceModel = ResourceModel.builder().build()
        val request: ResourceHandlerRequest<ResourceModel> = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(model)
            .build()
        val response: ProgressEvent<ResourceModel, CallbackContext?> =
            handler.handleRequest(proxy, request, CallbackContext(), proxyClient!!, logger)
        assertThat<ProgressEvent<ResourceModel, CallbackContext?>>(response).isNotNull()
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS)
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0)
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState())
        assertThat<List<ResourceModel>>(response.getResourceModels()).isNull()
        assertThat(response.getMessage()).isNull()
        assertThat(response.getErrorCode()).isNull()
    }
}
