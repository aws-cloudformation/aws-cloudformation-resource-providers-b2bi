package software.amazon.b2bi.profile

import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.mock

@ExtendWith(MockitoExtension::class)
class ListHandlerTest {
    @Mock
    private lateinit var proxy: AmazonWebServicesClientProxy
    @Mock
    private lateinit var logger: Logger

    @BeforeEach
    fun setup() {
        proxy = mock(AmazonWebServicesClientProxy::class.java)
        logger = mock(Logger::class.java)
    }

    @Test
    fun handleRequest_SimpleSuccess() {
        val handler = ListHandler()
        // val model: ResourceModel = ResourceModel.builder().build()
        val model: ResourceModel = ResourceModel()
        val request: ResourceHandlerRequest<ResourceModel> =
            ResourceHandlerRequest.builder<ResourceModel>()
                .desiredResourceState(model)
                .build()
        val response: ProgressEvent<ResourceModel, CallbackContext?> =
            handler.handleRequest(proxy, request, null, logger)
        assertThat(response).isNotNull()
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS)
        assertThat(response.getCallbackContext()).isNull()
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0)
        assertThat(response.getResourceModel()).isNull()
        assertThat(response.getResourceModels()).isNotNull()
        assertThat(response.getMessage()).isNull()
        assertThat(response.getErrorCode()).isNull()
    }
}
