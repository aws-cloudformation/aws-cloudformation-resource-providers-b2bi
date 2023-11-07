package software.amazon.b2bi.profile

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
import software.amazon.awssdk.services.b2bi.model.GetProfileRequest
import software.amazon.awssdk.services.b2bi.model.GetProfileResponse
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceRequest
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceResponse
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ProxyClient
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import java.time.Duration

@TestInstance(Lifecycle.PER_CLASS)
class ReadHandlerTest : AbstractTestBase() {
    private lateinit var proxy: AmazonWebServicesClientProxy
    private lateinit var b2BiClient: B2BiClient
    private lateinit var proxyClient: ProxyClient<B2BiClient>
    private val handler = ReadHandler()

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
    @MethodSource("readHandlerSuccessTestData")
    fun handleRequest(testArgs: TestArgs) {
        every { b2BiClient.getProfile(any<GetProfileRequest>()) } returns testArgs.apiResponse
        every { b2BiClient.listTagsForResource(any<ListTagsForResourceRequest>()) } returns testArgs.apiTags

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
            proxyClient.client().getProfile(any<GetProfileRequest>())
        } throws AwsServiceException.builder().build()

        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .desiredResourceState(TEST_GET_PROFILE_REQUEST_RESOURCE_MODEL)
            .build()
        Assertions.assertThatThrownBy { handler.handleRequest(request) }
            .isInstanceOf(CfnGeneralServiceException::class.java)
    }

    private fun ReadHandler.handleRequest(
        request: ResourceHandlerRequest<ResourceModel>
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        return this.handleRequest(proxy, request, CallbackContext(), proxyClient, logger)
    }

    data class TestArgs(
        val testName: String,
        val requestResourceModel: ResourceModel,
        val apiResponse: GetProfileResponse,
        val apiTags: ListTagsForResourceResponse,
        val expectedResourceModel: ResourceModel
    )

    companion object {
        @JvmStatic
        fun readHandlerSuccessTestData() = listOf(
            TestArgs(
                testName = "Read profile with all fields.",
                requestResourceModel = TEST_GET_PROFILE_REQUEST_RESOURCE_MODEL,
                apiResponse = TEST_GET_PROFILE_RESPONSE_WITH_ALL_FIELDS,
                apiTags = TEST_LIST_TAGS_FOR_RESOURCE_RESPONSE,
                expectedResourceModel = TEST_GET_PROFILE_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS
            )
        )
    }
}
