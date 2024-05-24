package software.amazon.b2bi.capability

import com.google.common.collect.Lists
import software.amazon.awssdk.awscore.AwsRequest
import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.model.AccessDeniedException
import software.amazon.awssdk.services.b2bi.model.ConflictException
import software.amazon.awssdk.services.b2bi.model.CreateCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.DeleteCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.GetCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.GetCapabilityResponse
import software.amazon.awssdk.services.b2bi.model.InternalServerException
import software.amazon.awssdk.services.b2bi.model.ListCapabilitiesRequest
import software.amazon.awssdk.services.b2bi.model.ListCapabilitiesResponse
import software.amazon.awssdk.services.b2bi.model.ResourceNotFoundException
import software.amazon.awssdk.services.b2bi.model.ServiceQuotaExceededException
import software.amazon.awssdk.services.b2bi.model.TagResourceRequest
import software.amazon.awssdk.services.b2bi.model.ThrottlingException
import software.amazon.awssdk.services.b2bi.model.UntagResourceRequest
import software.amazon.awssdk.services.b2bi.model.UpdateCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.ValidationException
import software.amazon.b2bi.capability.TagHelper.toSdkTag
import software.amazon.cloudformation.exceptions.BaseHandlerException
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException
import software.amazon.cloudformation.exceptions.CfnAlreadyExistsException
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException
import software.amazon.cloudformation.exceptions.CfnNotFoundException
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException
import software.amazon.cloudformation.exceptions.CfnServiceLimitExceededException
import software.amazon.cloudformation.exceptions.CfnThrottlingException
import java.time.Instant
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import software.amazon.awssdk.services.b2bi.model.S3Location as SdkS3Location
import software.amazon.b2bi.capability.S3Location as ResourceS3Location
import software.amazon.awssdk.services.b2bi.model.CapabilityConfiguration as SdkCapabilityConfiguration
import software.amazon.b2bi.capability.CapabilityConfiguration as ResourceCapabilityConfiguration
import software.amazon.awssdk.services.b2bi.model.EdiConfiguration as SdkEdiConfiguration
import software.amazon.b2bi.capability.EdiConfiguration as ResourceEdiConfiguration
import software.amazon.awssdk.services.b2bi.model.EdiType as SdkEdiType
import software.amazon.b2bi.capability.EdiType as ResourceEdiType
import software.amazon.awssdk.services.b2bi.model.X12Details as SdkX12Details
import software.amazon.b2bi.capability.X12Details as ResourceX12Details

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
     * @return createProfileRequest the aws service request to create a resource
     */
    fun translateToCreateRequest(model: ResourceModel): CreateCapabilityRequest {
        return CreateCapabilityRequest.builder()
            .name(model.name)
            .type(model.type)
            .instructionsDocuments(model.instructionsDocuments?.map { it.toSdkS3Location() })
            .configuration(model.configuration.toSdkCapabilityConfiguration())
            .tags(model.tags.map { it.toSdkTag() })
            .build()
    }

    /**
     * Request to read a resource
     * @param model resource model
     * @return getProfileRequest the aws service request to describe a resource
     */
    fun translateToReadRequest(model: ResourceModel): GetCapabilityRequest {
        return GetCapabilityRequest.builder()
            .capabilityId(model.capabilityId)
            .build()
    }

    /**
     * Translates resource object from sdk into a resource model
     * @param response the aws service describe resource response
     * @return model resource model
     */
    fun translateFromReadResponse(response: GetCapabilityResponse): ResourceModel {
        return ResourceModel.builder()
            .capabilityId(response.capabilityId().emptyToNull())
            .capabilityArn(response.capabilityArn().emptyToNull())
            .name(response.name().emptyToNull())
            .type(response.typeAsString().emptyToNull())
            .configuration(response.configuration()?.toResourceCapabilityConfiguration())
            .instructionsDocuments(response.instructionsDocuments()?.map { it.toResourceS3Location() })
            .createdAt(response.createdAt().emptyToNull())
            .modifiedAt(response.modifiedAt().emptyToNull())
            .build()
    }

    private fun String?.emptyToNull() = if (this.isNullOrEmpty()) null else this

    private fun Instant?.emptyToNull() = if (this == null) null else this.toString()

    /**
     * Request to delete a resource
     * @param model resource model
     * @return deleteCapabilityRequest the aws service request to delete a resource
     */
    fun translateToDeleteRequest(model: ResourceModel): DeleteCapabilityRequest {
        return DeleteCapabilityRequest.builder()
            .capabilityId(model.capabilityId)
            .build()
    }

    /**
     * Request to update properties of a previously created resource
     * @param model resource model
     * @return updateCapabilityRequest the aws service request to modify a resource
     */
    fun translateToUpdateRequest(model: ResourceModel): UpdateCapabilityRequest {
        return UpdateCapabilityRequest.builder()
            .capabilityId(model.capabilityId)
            .name(model.name)
            .configuration(model.configuration.toSdkCapabilityConfiguration())
            .instructionsDocuments(model.instructionsDocuments.map { it.toSdkS3Location() })
            .build()
    }

    /**
     * Request to list resources
     * @param nextToken token passed to the aws service list resources request
     * @return listCapabilityRequest the aws service request to list resources within aws account
     */
    fun translateToListRequest(nextToken: String?): ListCapabilitiesRequest {
        return ListCapabilitiesRequest.builder()
            .apply {
                nextToken?.let { nextToken(it) }
            }
            .build()
    }

    /**
     * Translates resource objects from sdk into a resource model (primary identifier only)
     * @param response the capability list resource response
     * @return list of resource models
     */
    fun translateFromListResponse(response: ListCapabilitiesResponse): List<ResourceModel> {
        return response.capabilities().map {
            ResourceModel.builder()
                .capabilityId(it.capabilityId())
                .name(it.name())
                .type(it.typeAsString())
                .createdAt(it.createdAt().toString())
                .modifiedAt(it.modifiedAt().toString())
                .build()
        }
    }

    /**
     * Request to add tags to a resource
     * @param model resource model
     * @param addedTags tags to add
     * @return tagResourceRequest the aws service request to create a resource
     */
    fun translateToTagResourceRequest(model: ResourceModel, addedTags: Map<String, String>): TagResourceRequest {
        return TagResourceRequest.builder()
            .resourceARN(model.capabilityArn)
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
            .resourceARN(model.capabilityArn)
            .tagKeys(removedTags)
            .build()
    }

    /**
     * Throws a CloudFormation exception for the corresponding B2BI exception
     *
     * While the handler contract states that the handler must always return a progress event,
     * you may throw any instance of BaseHandlerException, as the wrapper map it to a progress event.
     * Each BaseHandlerException maps to a specific error code, and you should map service exceptions as closely as
     * possible to more specific error codes
     */
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

    fun ResourceS3Location.toSdkS3Location(): SdkS3Location {
        return SdkS3Location.builder().bucketName(this.bucketName).key(this.key).build()
    }

    fun SdkS3Location.toResourceS3Location(): ResourceS3Location {
        return ResourceS3Location.builder().bucketName(this.bucketName()).key(this.key()).build()
    }

    fun ResourceCapabilityConfiguration.toSdkCapabilityConfiguration(): SdkCapabilityConfiguration {
        return SdkCapabilityConfiguration.builder().edi(this.edi.toSdkEdiConfiguration()).build()
    }

    fun ResourceEdiConfiguration.toSdkEdiConfiguration(): SdkEdiConfiguration {
        return SdkEdiConfiguration.builder()
            .type(this.type.toSdkEdiType())
            .inputLocation(this.inputLocation.toSdkS3Location())
            .outputLocation(this.outputLocation.toSdkS3Location())
            .transformerId(this.transformerId)
            .build()
    }

    fun ResourceEdiType.toSdkEdiType(): SdkEdiType {
        return SdkEdiType.builder().x12Details(this.x12Details.toSdkX12Details()).build()
    }

    fun ResourceX12Details.toSdkX12Details(): SdkX12Details {
        return SdkX12Details.builder().transactionSet(this.transactionSet).version(this.version).build()
    }

    fun SdkCapabilityConfiguration.toResourceCapabilityConfiguration(): ResourceCapabilityConfiguration {
        return ResourceCapabilityConfiguration.builder().edi(this.edi().toResourceEdiConfiguration()).build()
    }

    fun SdkEdiConfiguration.toResourceEdiConfiguration(): ResourceEdiConfiguration {
        return ResourceEdiConfiguration.builder()
            .type(this.type().toResourceEdiType())
            .inputLocation(this.inputLocation().toResourceS3Location())
            .outputLocation(this.outputLocation().toResourceS3Location())
            .transformerId(this.transformerId())
            .build()
    }

    fun SdkEdiType.toResourceEdiType(): ResourceEdiType {
        return ResourceEdiType.builder().x12Details(this.x12Details().toResourceX12Details()).build()
    }

    fun SdkX12Details.toResourceX12Details(): ResourceX12Details {
        return ResourceX12Details.builder().transactionSet(this.transactionSetAsString()).version(this.versionAsString()).build()
    }
}
