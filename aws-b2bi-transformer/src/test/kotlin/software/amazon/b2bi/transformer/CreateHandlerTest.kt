package software.amazon.b2bi.transformer

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.CreateTransformerRequest
import software.amazon.awssdk.services.b2bi.model.CreateTransformerResponse
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import java.time.Duration

@TestInstance(Lifecycle.PER_CLASS)
class CreateHandlerTest : AbstractTestBase() {
    private lateinit var proxy: AmazonWebServicesClientProxy
    private lateinit var b2BiClient: B2BiClient
    private lateinit var proxyClient: ProxyClient<B2BiClient>
    private val handler = CreateHandler()

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
        every { b2BiClient.createTransformer(any<CreateTransformerRequest>()) } returns testArgs.apiResponse

        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(testArgs.requestResourceModel)
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
    @MethodSource("createHandlerSuccessLegacyTransformerTestData")
    fun handleLegacyRequest(testArgs: TestArgs) = handleRequest(testArgs)

    @ParameterizedTest
    @MethodSource("createHandlerSuccessInboundTransformerTestData")
    fun handleInboundRequest(testArgs: TestArgs) = handleRequest(testArgs)

    @ParameterizedTest
    @MethodSource("createHandlerSuccessOutboundTransformerTestData")
    fun handleOutboundRequest(testArgs: TestArgs) = handleRequest(testArgs)

    @Test
    fun handleRequest_throwsException() {
        every {
            proxyClient.client().createTransformer(any<CreateTransformerRequest>())
        } throws AwsServiceException.builder().build()

        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS)
            .build()
        assertThatThrownBy { handler.handleRequest(request) }
            .isInstanceOf(CfnGeneralServiceException::class.java)
    }

    private fun CreateHandler.handleRequest(
        request: ResourceHandlerRequest<ResourceModel>
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        return this.handleRequest(proxy, request, CallbackContext(), proxyClient, logger)
    }

    data class TestArgs(
        val testName: String,
        val requestResourceModel: ResourceModel,
        val apiResponse: CreateTransformerResponse,
        val expectedResourceModel: ResourceModel
    )

    companion object {
        @JvmStatic
        fun createHandlerSuccessLegacyTransformerTestData() = listOf(
            TestArgs(
                testName = "Create transformer with all legacy fields.",
                requestResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS,
                apiResponse = TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_LEGACY_FIELDS,
                expectedResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS.toBuilder()
                    .transformerId(TEST_TRANSFORMER_ID)
                    .transformerArn(TEST_TRANSFORMER_ARN)
                    .status(TEST_TRANSFORMER_STATUS)
                    .build()
            ),
            TestArgs(
                testName = "Create transformer with only required legacy fields.",
                requestResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_LEGACY_FIELDS,
                apiResponse = TEST_CREATE_TRANSFORMER_RESPONSE_WITH_REQUIRED_LEGACY_FIELDS,
                expectedResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_LEGACY_FIELDS.toBuilder()
                    .transformerId(TEST_TRANSFORMER_ID)
                    .transformerArn(TEST_TRANSFORMER_ARN)
                    .status(TEST_TRANSFORMER_STATUS)
                    .build()
            ),
        )

        @JvmStatic
        fun createHandlerSuccessInboundTransformerTestData() = listOf(
            TestArgs(
                testName = "Create transformer with all inbound fields.",
                requestResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS,
                apiResponse = TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_INBOUND_FIELDS,
                expectedResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS.toBuilder()
                    .transformerId(TEST_TRANSFORMER_ID)
                    .transformerArn(TEST_TRANSFORMER_ARN)
                    .status(TEST_TRANSFORMER_STATUS)
                    .build()
            ),
            TestArgs(
                testName = "Create transformer with only required inbound fields.",
                requestResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_INBOUND_FIELDS,
                apiResponse = TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_INBOUND_FIELDS,
                expectedResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_INBOUND_FIELDS.toBuilder()
                    .transformerId(TEST_TRANSFORMER_ID)
                    .transformerArn(TEST_TRANSFORMER_ARN)
                    .status(TEST_TRANSFORMER_STATUS)
                    .build()
            ),
        )

        @JvmStatic
        fun createHandlerSuccessOutboundTransformerTestData() = listOf(
            TestArgs(
                testName = "Create transformer with all outbound fields.",
                requestResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS,
                apiResponse = TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_OUTBOUND_FIELDS,
                expectedResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS.toBuilder()
                    .transformerId(TEST_TRANSFORMER_ID)
                    .transformerArn(TEST_TRANSFORMER_ARN)
                    .status(TEST_TRANSFORMER_STATUS)
                    .build()
            ),
            TestArgs(
                testName = "Create transformer with only required outbound fields.",
                requestResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_OUTBOUND_FIELDS,
                apiResponse = TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_OUTBOUND_FIELDS,
                expectedResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_OUTBOUND_FIELDS.toBuilder()
                    .transformerId(TEST_TRANSFORMER_ID)
                    .transformerArn(TEST_TRANSFORMER_ARN)
                    .status(TEST_TRANSFORMER_STATUS)
                    .build()
            ),
        )
    }
}
