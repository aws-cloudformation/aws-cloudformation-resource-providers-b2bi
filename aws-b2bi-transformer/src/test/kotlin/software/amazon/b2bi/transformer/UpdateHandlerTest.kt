package software.amazon.b2bi.transformer

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import software.amazon.awssdk.core.SdkClient
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerRequest
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerResponse
import software.amazon.cloudformation.proxy.*
import java.time.Duration
import java.util.function.Supplier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateHandlerTest : AbstractTestBase() {
    private lateinit var proxy: AmazonWebServicesClientProxy
    private lateinit var b2BiClient: B2BiClient
    private lateinit var proxyClient: ProxyClient<B2BiClient>
    private val handler = UpdateHandler()

    @BeforeAll
    fun setupOnce() {
        proxy = AmazonWebServicesClientProxy(logger, mockCredentials) { Duration.ofSeconds(600).toMillis() }
        b2BiClient = mockk(relaxed = true)
        proxyClient = mockProxy(proxy, b2BiClient)
    }

    @AfterEach
    fun reset() {
        clearAllMocks()
    }

    @AfterAll
    fun teardown() {
        unmockkAll()
    }

    @ParameterizedTest
    @MethodSource("updateHandlerSuccessTestData")
    fun handleRequest(testArgs: TestArgs) {
        every { b2BiClient.updateTransformer(any<UpdateTransformerRequest>()) } returns testArgs.apiResponse

        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(testArgs.requestDesiredResourceModel)
            .previousResourceState(testArgs.requestPreviousResourceModel)
            .desiredResourceTags(testArgs.requestDesiredResourceTags)
            .previousResourceTags(testArgs.requestPreviousResourceTags)
            .build()
        val response = handler.handleRequest(request)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(OperationStatus.SUCCESS)
        assertThat(response.callbackDelaySeconds).isEqualTo(0)
        assertThat(response.resourceModel).isEqualTo(testArgs.expectedResourceModel)
        assertThat(response.resourceModels).isNull()
        assertThat(response.message).isNull()
        assertThat(response.errorCode).isNull()
    }

    private fun UpdateHandler.handleRequest(
        request: ResourceHandlerRequest<ResourceModel>
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        return this.handleRequest(proxy, request, CallbackContext(), proxyClient, logger)
    }

    data class TestArgs(
        val testName: String,
        val requestDesiredResourceModel: ResourceModel,
        val requestPreviousResourceModel: ResourceModel,
        val requestDesiredResourceTags: Map<String, String>,
        val requestPreviousResourceTags: Map<String, String>,
        val apiResponse: UpdateTransformerResponse,
        val expectedResourceModel: ResourceModel
    )

    companion object {
        @JvmStatic
        fun updateHandlerSuccessTestData() = listOf(
            TestArgs(
                testName = "Update transformer with all updatable fields.",
                requestDesiredResourceModel = TEST_UPDATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS,
                requestPreviousResourceModel = ResourceModel(),
                requestDesiredResourceTags = emptyMap(),
                requestPreviousResourceTags = emptyMap(),
                apiResponse = TEST_UPDATE_TRANSFORMER_RESPONSE_WITH_ALL_FIELDS,
                expectedResourceModel = TEST_UPDATE_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS
            )
        )
    }
}
