package software.amazon.b2bi.profile

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.awssdk.services.b2bi.model.ListProfilesRequest
import software.amazon.awssdk.services.b2bi.model.ListProfilesResponse
import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy
import software.amazon.cloudformation.proxy.Logger
import software.amazon.cloudformation.proxy.OperationStatus
import software.amazon.cloudformation.proxy.ProgressEvent
import software.amazon.cloudformation.proxy.ResourceHandlerRequest
import java.util.function.Function

@TestInstance(Lifecycle.PER_CLASS)
class ListHandlerTest {
    @MockK
    private lateinit var proxy: AmazonWebServicesClientProxy
    @MockK
    private lateinit var logger: Logger
    @MockK
    private lateinit var b2BiClient: B2BiClient
    private var handler = ListHandler()

    @BeforeAll
    fun setupOnce() {
        MockKAnnotations.init(this, relaxed = true)
        mockkObject(ClientBuilder)
        every { ClientBuilder.getClient() } returns b2BiClient
    }

    @AfterAll
    fun teardown() {
        unmockkAll()
    }

    @ParameterizedTest
    @MethodSource("listHandlerSuccessTestData")
    fun handleRequest(testArgs: TestArgs) {
        every {
            proxy.injectCredentialsAndInvokeV2(
                any<ListProfilesRequest>(),
                any<Function<ListProfilesRequest, ListProfilesResponse>>()
            )
        } returns testArgs.apiResponse

        val request = ResourceHandlerRequest.builder<ResourceModel>()
            .nextToken(testArgs.requestNextToken)
            .build()
        val response = handler.handleRequest(request)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(OperationStatus.SUCCESS)
        assertThat(response.callbackContext).isNull()
        assertThat(response.callbackDelaySeconds).isEqualTo(0)
        assertThat(response.resourceModel).isNull()
        assertThat(response.resourceModels).isEqualTo(testArgs.expectedResourceModels)
        assertThat(response.nextToken).isNull()
        assertThat(response.message).isNull()
        assertThat(response.errorCode).isNull()
    }

    private fun ListHandler.handleRequest(
        request: ResourceHandlerRequest<ResourceModel>
    ): ProgressEvent<ResourceModel, CallbackContext?> {
        return this.handleRequest(proxy, request, CallbackContext(), logger)
    }

    data class TestArgs(
        val testName: String,
        val requestNextToken: String?,
        val apiResponse: ListProfilesResponse,
        val expectedResourceModels: List<ResourceModel>
    )

    companion object {
        @JvmStatic
        fun listHandlerSuccessTestData() = listOf(
            TestArgs(
                testName = "List profiles with no nextToken and single existing profile returns one profile.",
                requestNextToken = null,
                apiResponse = TEST_LIST_PROFILES_RESPONSE_WITH_ONE_PROFILE_WITH_ALL_FIELDS,
                expectedResourceModels = listOf(TEST_LIST_PROFILES_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS)
            )
        )
    }
}
