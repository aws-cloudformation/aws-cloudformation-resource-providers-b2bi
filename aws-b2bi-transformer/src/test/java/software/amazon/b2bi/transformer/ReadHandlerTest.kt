package software.amazon.b2bi.transformer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import java.time.Duration
import java.util.function.Supplier

@ExtendWith(MockitoExtension::class)
class ReadHandlerTest : AbstractTestBase() {
    @Mock
    private var proxy: AmazonWebServicesClientProxy? = null

    @Mock
    private var proxyClient: ProxyClient<SdkClient>? = null

    @Mock
    var sdkClient: SdkClient? = null
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
        verify(sdkClient, atLeastOnce()).serviceName()
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
        Assertions.assertThat<ProgressEvent<ResourceModel, CallbackContext?>>(response).isNotNull()
        Assertions.assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS)
        Assertions.assertThat(response.getCallbackDelaySeconds()).isEqualTo(0)
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState())
        Assertions.assertThat<List<ResourceModel>>(response.getResourceModels()).isNull()
        Assertions.assertThat(response.getMessage()).isNull()
        Assertions.assertThat(response.getErrorCode()).isNull()
    }
}
