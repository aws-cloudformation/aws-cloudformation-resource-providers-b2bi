package software.amazon.b2bi.capability

import java.time.Duration
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import software.amazon.awssdk.services.b2bi.B2BiClient

@ExtendWith(MockitoExtension::class)
class DeleteHandlerTest : AbstractTestBase() {
    @Mock
    private lateinit var proxy: AmazonWebServicesClientProxy
    @Mock
    private lateinit var proxyClient: ProxyClient<B2BiClient>
    @Mock
    private lateinit var sdkClient: B2BiClient

    @BeforeEach
    fun setup() {
        proxy = AmazonWebServicesClientProxy(logger, MOCK_CREDENTIALS) { Duration.ofSeconds(600).toMillis() }
        sdkClient = mock(B2BiClient::class.java)
        proxyClient = MOCK_PROXY(proxy, sdkClient)
    }

    @AfterEach
    fun tear_down() {
        verify(sdkClient, atLeastOnce()).serviceName()
        verifyNoMoreInteractions(sdkClient)
    }

    @Test
    fun handleRequest_SimpleSuccess() {
        val handler = DeleteHandler()
        // val model: ResourceModel = ResourceModel.builder().build()
        val model: ResourceModel = ResourceModel()
        val request: ResourceHandlerRequest<ResourceModel> =
            ResourceHandlerRequest.builder<ResourceModel>()
                .desiredResourceState(model)
                .build()
        val response: ProgressEvent<ResourceModel, CallbackContext?> =
            handler.handleRequest(proxy, request, CallbackContext(), proxyClient, logger)
        assertThat(response).isNotNull()
        assertThat(response.getStatus()).isEqualTo(OperationStatus.SUCCESS)
        assertThat(response.getCallbackDelaySeconds()).isEqualTo(0)
        assertThat(response.getResourceModel()).isNull()
        assertThat(response.getResourceModels()).isNull()
        assertThat(response.getMessage()).isNull()
        assertThat(response.getErrorCode()).isNull()
    }
}