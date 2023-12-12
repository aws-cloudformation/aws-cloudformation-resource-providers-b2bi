package software.amazon.b2bi.capability

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
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
import software.amazon.awssdk.services.b2bi.model.UpdateCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.UpdateCapabilityResponse
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
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

    @ParameterizedTest
    @MethodSource("updateHandlerSuccessTestData")
    fun handleRequest(testArgs: TestArgs) {
        every { b2BiClient.updateCapability(any<UpdateCapabilityRequest>()) } returns testArgs.apiResponse

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

    @Test
    fun handleRequest_throwsException() {
        every {
            proxyClient.client().updateCapability(any<UpdateCapabilityRequest>())
        } throws AwsServiceException.builder().build()

        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(TEST_UPDATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS)
            .build()
        Assertions.assertThatThrownBy { handler.handleRequest(request) }
            .isInstanceOf(CfnGeneralServiceException::class.java)
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
        val apiResponse: UpdateCapabilityResponse,
        val expectedResourceModel: ResourceModel
    )

    companion object {
        @JvmStatic
        fun updateHandlerSuccessTestData() = listOf(
            TestArgs(
                testName = "Update capability with all updatable fields.",
                requestDesiredResourceModel = TEST_UPDATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS,
                requestPreviousResourceModel = ResourceModel(),
                requestDesiredResourceTags = emptyMap(),
                requestPreviousResourceTags = emptyMap(),
                apiResponse = TEST_UPDATE_CAPABILITY_RESPONSE_WITH_ALL_FIELDS,
                expectedResourceModel = TEST_UPDATE_CAPABILITY_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS
            )
        )
    }
}