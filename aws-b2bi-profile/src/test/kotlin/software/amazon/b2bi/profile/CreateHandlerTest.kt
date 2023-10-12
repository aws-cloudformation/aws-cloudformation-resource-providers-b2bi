package software.amazon.b2bi.profile

import java.time.Duration
import software.amazon.awssdk.core.SdkClient
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

@ExtendWith(MockitoExtension::class)
class CreateHandlerTest : AbstractTestBase() {
    @Mock
    private lateinit var proxy: AmazonWebServicesClientProxy
    @Mock
    private lateinit var proxyClient: ProxyClient<SdkClient>
    @Mock
    private lateinit var sdkClient: SdkClient

    @BeforeEach
    fun setup() {
        proxy = AmazonWebServicesClientProxy(logger, MOCK_CREDENTIALS) { Duration.ofSeconds(600).toMillis() }
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
        val handler = CreateHandler()
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
        assertThat(response.getResourceModel()).isEqualTo(request.getDesiredResourceState())
        assertThat(response.getResourceModels()).isNull()
        assertThat(response.getMessage()).isNull()
        assertThat(response.getErrorCode()).isNull()
    }
}
