package software.amazon.b2bi.capability

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
import software.amazon.awssdk.services.b2bi.model.CreateCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.CreateCapabilityResponse
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

    @ParameterizedTest
    @MethodSource("createHandlerSuccessTestData")
    fun handleRequest(testArgs: TestArgs) {
        every { b2BiClient.createCapability(any<CreateCapabilityRequest>()) } returns testArgs.apiResponse

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
            proxyClient.client().createCapability(any<CreateCapabilityRequest>())
        } throws AwsServiceException.builder().build()

        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(TEST_CREATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS)
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
        val apiResponse: CreateCapabilityResponse,
        val expectedResourceModel: ResourceModel
    )

    companion object {
        @JvmStatic
        fun createHandlerSuccessTestData() = listOf(
            TestArgs(
                testName = "Create capability with all fields.",
                requestResourceModel = TEST_CREATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS,
                apiResponse = TEST_CREATE_CAPABILITY_RESPONSE_WITH_ALL_FIELDS,
                expectedResourceModel = TEST_CREATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS.toBuilder()
                    .capabilityId(TEST_CAPABILITY_ID)
                    .capabilityArn(TEST_CAPABILITY_ARN)
                    .createdAt(TEST_INSTANT.toString())
                    .build()
            ),
            TestArgs(
                testName = "Create capability with only required fields.",
                requestResourceModel = TEST_CREATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS,
                apiResponse = TEST_CREATE_CAPABILITY_RESPONSE_WITH_REQUIRED_FIELDS,
                expectedResourceModel = TEST_CREATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS.toBuilder()
                    .capabilityId(TEST_CAPABILITY_ID)
                    .capabilityArn(TEST_CAPABILITY_ARN)
                    .instructionsDocuments(emptyList())
                    .createdAt(TEST_INSTANT.toString())
                    .build()
            ),
        )
    }
}