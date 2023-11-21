package software.amazon.b2bi.transformer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent

@ExtendWith(MockitoExtension::class)
class ListHandlerTest {
    @Mock
    private var proxy: AmazonWebServicesClientProxy? = null

    @Mock
    private var logger: Logger? = null
    @BeforeEach
    fun setup() {
        proxy = mock(AmazonWebServicesClientProxy::class.java)
        logger = mock(Logger::class.java)
    }

    @Test
    fun handleRequest_SimpleSuccess() {
        val handler = ListHandler()
        val model: ResourceModel = ResourceModel.builder().build()
        val request: ResourceHandlerRequest<ResourceModel> = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(model)
            .build()
        val response: ProgressEvent<ResourceModel, CallbackContext?> =
            handler.handleRequest(proxy, request, null, logger)
        Assertions.assertThat<ProgressEvent<ResourceModel, CallbackContext?>>(response).isNotNull()
        Assertions.assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS)
        Assertions.assertThat(response.getCallbackContext()).isNull()
        Assertions.assertThat(response.getCallbackDelaySeconds()).isEqualTo(0)
        assertThat(response.getResourceModel()).isNull()
        Assertions.assertThat<List<ResourceModel>>(response.getResourceModels()).isNotNull()
        Assertions.assertThat(response.getMessage()).isNull()
        Assertions.assertThat(response.getErrorCode()).isNull()
    }
}
