package software.amazon.b2bi.profile

import com.google.common.collect.Lists
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.collections.Collection
import software.amazon.awssdk.awscore.AwsRequest
import software.amazon.awssdk.awscore.AwsResponse

/**
 * This class is a centralized placeholder for
 * - api request construction
 * - object translation to/from aws sdk
 * - resource model construction for read/list handlers
 */
object Translator {
    /**
     * Request to create a resource
     * @param model resource model
     * @return awsRequest the aws service request to create a resource
     */
    fun translateToCreateRequest(model: ResourceModel?): AwsRequest? {
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L39-L43
        return null
    }

    /**
     * Request to read a resource
     * @param model resource model
     * @return awsRequest the aws service request to describe a resource
     */
    fun translateToReadRequest(model: ResourceModel?): AwsRequest? {
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L20-L24
        return null
    }

    /**
     * Translates resource object from sdk into a resource model
     * @param awsResponse the aws service describe resource response
     * @return model resource model
     */
    fun translateFromReadResponse(awsResponse: AwsResponse?): ResourceModel {
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L58-L73
        return ResourceModel()
            // ResourceModel.builder() //.someProperty(response.property())
            // .build()
    }

    /**
     * Request to delete a resource
     * @param model resource model
     * @return awsRequest the aws service request to delete a resource
     */
    fun translateToDeleteRequest(model: ResourceModel?): AwsRequest? {
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L33-L37
        return null
    }

    /**
     * Request to update properties of a previously created resource
     * @param model resource model
     * @return awsRequest the aws service request to modify a resource
     */
    fun translateToFirstUpdateRequest(model: ResourceModel?): AwsRequest? {
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L45-L50
        return null
    }

    /**
     * Request to update some other properties that could not be provisioned through first update request
     * @param model resource model
     * @return awsRequest the aws service request to modify a resource
     */
    fun translateToSecondUpdateRequest(model: ResourceModel?): AwsRequest? {
        // TODO: construct a request
        return null
    }

    /**
     * Request to list resources
     * @param nextToken token passed to the aws service list resources request
     * @return awsRequest the aws service request to list resources within aws account
     */
    fun translateToListRequest(nextToken: String?): AwsRequest? {
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L26-L31
        return null
    }

    /**
     * Translates resource objects from sdk into a resource model (primary identifier only)
     * @param awsResponse the aws service describe resource response
     * @return list of resource models
     */
    fun translateFromListRequest(awsResponse: AwsResponse?): List<ResourceModel> {
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L75-L82
        return streamOfOrEmpty(Lists.newArrayList<AwsResponse>())
            .map { resource ->
                ResourceModel()
                // ResourceModel.builder() // include only primary identifier
                //     .build()
            }
            .collect(Collectors.toList())
    }

    private fun <T> streamOfOrEmpty(collection: Collection<T>?): Stream<T> {
        return collection?.stream() ?: Stream.empty()
    }

    /**
     * Request to add tags to a resource
     * @param model resource model
     * @return awsRequest the aws service request to create a resource
     */
    fun tagResourceRequest(model: ResourceModel, addedTags: Map<String, String>): AwsRequest? {
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L39-L43
        return null
    }

    /**
     * Request to add tags to a resource
     * @param model resource model
     * @return awsRequest the aws service request to create a resource
     */
    fun untagResourceRequest(model: ResourceModel?, removedTags: Set<String?>?): AwsRequest? {
        // TODO: construct a request
        // e.g. https://github.com/aws-cloudformation/aws-cloudformation-resource-providers-logs/blob/2077c92299aeb9a68ae8f4418b5e932b12a8b186/aws-logs-loggroup/src/main/java/com/aws/logs/loggroup/Translator.java#L39-L43
        return null
    }
}
