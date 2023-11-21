package software.amazon.b2bi.capability

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ResourceHandlerRequest

@ExtendWith(MockitoExtension::class)
class ListHandlerTest {
    @Mock
    private var proxy: AmazonWebServicesClientProxy? = null

    @Mock
    private var logger: Logger? = null
    @BeforeEach
    fun setup() {
        proxy = Mockito.mock(AmazonWebServicesClientProxy::class.java)
        logger = Mockito.mock(Logger::class.java)
    }

    @Test
    fun handleRequest_SimpleSuccess() {
        val handler = ListHandler()
        val model = ResourceModel.builder().build()
        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(model)
            .build()
        val response = handler.handleRequest(proxy, request, null, logger)
        Assertions.assertThat(response).isNotNull()
        Assertions.assertThat(response.status).isEqualTo(OperationStatus.SUCCESS)
        Assertions.assertThat(response.callbackContext).isNull()
        Assertions.assertThat(response.callbackDelaySeconds).isEqualTo(0)
        Assertions.assertThat(response.resourceModel).isNull()
        Assertions.assertThat(response.resourceModels).isNotNull()
        Assertions.assertThat(response.message).isNull()
        Assertions.assertThat(response.errorCode).isNull()
    }
}
