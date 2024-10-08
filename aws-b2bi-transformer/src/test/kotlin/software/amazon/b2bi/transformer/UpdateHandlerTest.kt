package software.amazon.b2bi.transformer

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerRequest
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerResponse
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import java.time.Duration

@TestInstance(Lifecycle.PER_CLASS)
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

    private fun handleRequest(testArgs: TestArgs) {
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

    @ParameterizedTest
    @MethodSource("updateHandlerSuccessLegacyTransformerTestData")
    fun handleLegacyRequest(testArgs: TestArgs) = handleRequest(testArgs)

    @ParameterizedTest
    @MethodSource("updateHandlerSuccessInboundTransformerTestData")
    fun handleInboundRequest(testArgs: TestArgs) = handleRequest(testArgs)

    @ParameterizedTest
    @MethodSource("updateHandlerSuccessOutboundTransformerTestData")
    fun handleOutboundRequest(testArgs: TestArgs) = handleRequest(testArgs)

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
        fun updateHandlerSuccessLegacyTransformerTestData() = listOf(
            TestArgs(
                testName = "Update transformer with all updatable legacy fields.",
                requestDesiredResourceModel = TEST_UPDATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS,
                requestPreviousResourceModel = ResourceModel(),
                requestDesiredResourceTags = emptyMap(),
                requestPreviousResourceTags = emptyMap(),
                apiResponse = TEST_UPDATE_TRANSFORMER_RESPONSE_WITH_ALL_LEGACY_FIELDS,
                expectedResourceModel = TEST_UPDATE_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS
            )
        )

        @JvmStatic
        fun updateHandlerSuccessInboundTransformerTestData() = listOf(
            TestArgs(
                testName = "Update transformer with all updatable inbound fields.",
                requestDesiredResourceModel = TEST_UPDATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS,
                requestPreviousResourceModel = ResourceModel(),
                requestDesiredResourceTags = emptyMap(),
                requestPreviousResourceTags = emptyMap(),
                apiResponse = TEST_UPDATE_TRANSFORMER_RESPONSE_WITH_ALL_INBOUND_FIELDS,
                expectedResourceModel = TEST_UPDATE_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS
            )
        )

        @JvmStatic
        fun updateHandlerSuccessOutboundTransformerTestData() = listOf(
            TestArgs(
                testName = "Update transformer with all updatable outbound fields.",
                requestDesiredResourceModel = TEST_UPDATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS,
                requestPreviousResourceModel = ResourceModel(),
                requestDesiredResourceTags = emptyMap(),
                requestPreviousResourceTags = emptyMap(),
                apiResponse = TEST_UPDATE_TRANSFORMER_RESPONSE_WITH_ALL_OUTBOUND_FIELDS,
                expectedResourceModel = TEST_UPDATE_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS
            )
        )
    }
}
