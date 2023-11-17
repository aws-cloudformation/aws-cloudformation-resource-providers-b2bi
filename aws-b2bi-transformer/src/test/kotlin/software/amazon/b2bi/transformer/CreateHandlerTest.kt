package software.amazon.b2bi.transformer

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.core.SdkClient
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.CreateTransformerRequest
import software.amazon.awssdk.services.b2bi.model.CreateTransformerResponse
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.proxy.*
import java.time.Duration
import java.util.function.Supplier

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

    fun reset() {
        clearAllMocks()
    }

    @AfterAll
    fun teardown() {
        unmockkAll()
    }

    @ParameterizedTest
    @MethodSource("createHandlerSuccessTestData")
    fun handleRequest(testArgs: TestArgs) {
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

    @Test
    fun handleRequest_throwsException() {
        every {
            proxyClient.client().createTransformer(any<CreateTransformerRequest>())
        } throws AwsServiceException.builder().build()

        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS)
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
        fun createHandlerSuccessTestData() = listOf(
            TestArgs(
                testName = "Create transformer with all fields.",
                requestResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS,
                apiResponse = TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_FIELDS,
                expectedResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS.toBuilder()
                    .transformerId(TEST_TRANSFORMER_ID)
                    .transformerArn(TEST_TRANSFORMER_ARN)
                    .status(TEST_TRANSFORMER_STATUS)
                    .build()
            ),
            TestArgs(
                testName = "Create transformer with only required fields.",
                requestResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS,
                apiResponse = TEST_CREATE_TRANSFORMER_RESPONSE_WITH_REQUIRED_FIELDS,
                expectedResourceModel = TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS.toBuilder()
                    .transformerId(TEST_TRANSFORMER_ID)
                    .transformerArn(TEST_TRANSFORMER_ARN)
                    .status(TEST_TRANSFORMER_STATUS)
                    .build()
            ),
        )
    }
}
