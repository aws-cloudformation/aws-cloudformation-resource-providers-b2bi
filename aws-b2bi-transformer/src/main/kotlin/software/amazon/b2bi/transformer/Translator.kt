package software.amazon.b2bi.transformer

import com.google.common.collect.Lists
import software.amazon.awssdk.awscore.AwsRequest
import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.model.*
import software.amazon.awssdk.services.b2bi.model.ResourceNotFoundException
import software.amazon.awssdk.services.b2bi.model.X12Details
import software.amazon.b2bi.transformer.TagHelper.toSdkTag
import software.amazon.cloudformation.exceptions.*
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import software.amazon.b2bi.transformer.EdiType as ResourceEdi
import software.amazon.awssdk.services.b2bi.model.EdiType as SdkEdi
import software.amazon.b2bi.transformer.X12Details as ResourceX12
import software.amazon.awssdk.services.b2bi.model.X12Details as SdkX12

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
     * @return createTransformerRequest the aws service request to create a resource
     */
    fun translateToCreateRequest(model: ResourceModel): CreateTransformerRequest {
        return CreateTransformerRequest.builder()
            .name(model.name)
            .fileFormat(model.fileFormat)
            .mappingTemplate(model.mappingTemplate)
            .sampleDocument(model.sampleDocument)
            .tags(model.tags.map { it.toSdkTag() })
            .ediType(model.ediType.translateToSdkEdi())
            .build()
    }

    /**
     * Request to read a resource
     * @param model resource model
     * @return GetTransformerRequest the aws service request to describe a resource
     */
    fun translateToReadRequest(model: ResourceModel): GetTransformerRequest {
        return GetTransformerRequest.builder()
            .transformerId(model.transformerId)
            .build()
    }

    /**
     * Translates resource object from sdk into a resource model
     * @param awsResponse the aws service describe resource response
     * @return model resource model
     */
    fun translateFromReadResponse(response: GetTransformerResponse): ResourceModel {
        return ResourceModel.builder()
            .transformerId(response.transformerId().ifEmpty { null })
            .transformerArn(response.transformerArn().ifEmpty { null })
            .name(response.name().ifEmpty { null })
            .fileFormat(response.fileFormat().toString().ifEmpty { null })
            .mappingTemplate(response.mappingTemplate().ifEmpty { null })
            .sampleDocument(response.sampleDocument().ifEmpty { null })
            .ediType(response.ediType().translateToResourceEdi())
            .status(response.status().toString().ifEmpty { null })
            .createdAt(response.createdAt().toString().ifEmpty { null })
            .modifiedAt(if (response.modifiedAt() != null) response.modifiedAt().toString() else null)
            .build()
    }

    /**
     * Request to delete a resource
     * @param model resource model
     * @return awsRequest the aws service request to delete a resource
     */
    fun translateToDeleteRequest(model: ResourceModel): DeleteTransformerRequest {
        return DeleteTransformerRequest.builder()
            .transformerId(model.transformerId)
            .build()
    }

    /**
     * Request to update properties of a previously created resource
     * @param model resource model
     * @return awsRequest the aws service request to modify a resource
     */
    fun translateToUpdateRequest(model: ResourceModel): UpdateTransformerRequest {
        return UpdateTransformerRequest.builder()
            .transformerId(model.transformerId)
            .name(model.name)
            .ediType(model.ediType.translateToSdkEdi())
            .fileFormat(model.fileFormat)
            .mappingTemplate(model.mappingTemplate)
            .sampleDocument(model.sampleDocument)
            .status(model.status)
            .build()
    }

    /**
     * Request to list resources
     * @param nextToken token passed to the aws service list resources request
     * @return awsRequest the aws service request to list resources within aws account
     */
    fun translateToListRequest(nextToken: String?): ListTransformersRequest {
        return ListTransformersRequest.builder()
            .apply {
                nextToken?.let { nextToken(it) }
            }
            .build()
    }

    /**
     * Translates resource objects from sdk into a resource model (primary identifier only)
     * @param awsResponse the aws service describe resource response
     * @return list of resource models
     */
    fun translateFromListResponse(response: ListTransformersResponse): List<ResourceModel> {
        return response.transformers().map {
            ResourceModel.builder()
                .transformerId(it.transformerId())
                .name(it.name())
                .fileFormat(it.fileFormatAsString())
                .mappingTemplate(it.mappingTemplate())
                .sampleDocument(it.sampleDocument())
                .status(it.statusAsString())
                .createdAt(it.createdAt().toString())
                .modifiedAt(if (it.modifiedAt() != null) it.modifiedAt().toString() else null)
                .build()
        }
    }

    /**
     * Request to add tags to a resource
     * @param model resource model
     * @return awsRequest the aws service request to create a resource
     */
    fun translateToTagResourceRequest(model: ResourceModel, addedTags: Map<String, String>): TagResourceRequest {
        return TagResourceRequest.builder()
            .resourceARN(model.transformerArn)
            .tags(TagHelper.convertToList(addedTags).map { it.toSdkTag() })
            .build()
    }

    /**
     * Request to add tags to a resource
     * @param model resource model
     * @param removedTags tags to remove
     * @return untagResourceRequest the aws service request to create a resource
     */
    fun translateToUntagResourceRequest(model: ResourceModel, removedTags: Set<String>): UntagResourceRequest {
        return UntagResourceRequest.builder()
            .resourceARN(model.transformerArn)
            .tagKeys(removedTags)
            .build()
    }

    fun ResourceEdi.translateToSdkEdi(): SdkEdi {
        val x12 : SdkX12 = SdkX12.builder().transactionSet(this.x12Details.transactionSet).version(this.x12Details.version).build()
        return SdkEdi.builder().x12Details(x12).build()
    }

    fun SdkEdi.translateToResourceEdi(): ResourceEdi {
        val x12 : ResourceX12 = ResourceX12.builder().transactionSet(this.x12Details().transactionSetAsString()).version(this.x12Details().versionAsString()).build()
        return ResourceEdi.builder().x12Details(x12).build()
    }

    fun AwsServiceException.toCfnException(): BaseHandlerException = when (this) {
        is AccessDeniedException -> CfnAccessDeniedException(this)
        is ConflictException -> CfnAlreadyExistsException(this)
        is InternalServerException -> CfnServiceInternalErrorException(this)
        is ResourceNotFoundException -> CfnNotFoundException(this)
        is ServiceQuotaExceededException -> CfnServiceLimitExceededException(this)
        is ThrottlingException -> CfnThrottlingException(this)
        is ValidationException -> CfnInvalidRequestException(this)
        else -> CfnGeneralServiceException(this)
    }
}
