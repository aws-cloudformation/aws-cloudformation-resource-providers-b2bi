package software.amazon.b2bi.profile

import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.model.AccessDeniedException
import software.amazon.awssdk.services.b2bi.model.ConflictException
import software.amazon.awssdk.services.b2bi.model.CreateProfileRequest
import software.amazon.awssdk.services.b2bi.model.DeleteProfileRequest
import software.amazon.awssdk.services.b2bi.model.GetProfileRequest
import software.amazon.awssdk.services.b2bi.model.GetProfileResponse
import software.amazon.awssdk.services.b2bi.model.InternalServerException
import software.amazon.awssdk.services.b2bi.model.ListProfilesRequest
import software.amazon.awssdk.services.b2bi.model.ListProfilesResponse
import software.amazon.awssdk.services.b2bi.model.ResourceNotFoundException
import software.amazon.awssdk.services.b2bi.model.ServiceQuotaExceededException
import software.amazon.awssdk.services.b2bi.model.TagResourceRequest
import software.amazon.awssdk.services.b2bi.model.ThrottlingException
import software.amazon.awssdk.services.b2bi.model.UntagResourceRequest
import software.amazon.awssdk.services.b2bi.model.UpdateProfileRequest
import software.amazon.awssdk.services.b2bi.model.ValidationException
import software.amazon.b2bi.profile.TagHelper.toSdkTag
import software.amazon.cloudformation.exceptions.BaseHandlerException
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException
import software.amazon.cloudformation.exceptions.CfnAlreadyExistsException
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException
import software.amazon.cloudformation.exceptions.CfnNotFoundException
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException
import software.amazon.cloudformation.exceptions.CfnServiceLimitExceededException
import software.amazon.cloudformation.exceptions.CfnThrottlingException

/**
 * This class is a centralized placeholder for
 * - api request construction
 * - object translation to/from aws sdk
 * - resource model construction for read/list handlers
 * - exception mapping between model and CloudFormation
 */
object Translator {
    /**
     * Request to create a resource
     * @param model resource model
     * @return createProfileRequest the aws service request to create a resource
     */
    fun translateToCreateRequest(model: ResourceModel): CreateProfileRequest {
        return CreateProfileRequest.builder()
            .name(model.name)
            .email(model.email)
            .phone(model.phone)
            .businessName(model.businessName)
            .logging(model.logging)
            .tags(model.tags.map { it.toSdkTag() })
            .build()
    }

    /**
     * Request to read a resource
     * @param model resource model
     * @return getProfileRequest the aws service request to describe a resource
     */
    fun translateToReadRequest(model: ResourceModel): GetProfileRequest {
        return GetProfileRequest.builder()
            .profileId(model.profileId)
            .build()
    }

    /**
     * Translates resource object from sdk into a resource model
     * @param response the aws service describe resource response
     * @return model resource model
     */
    fun translateFromReadResponse(response: GetProfileResponse): ResourceModel {
        return ResourceModel.builder()
            .profileId(response.profileId().ifEmpty { null })
            .profileArn(response.profileArn().ifEmpty { null })
            .name(response.name().ifEmpty { null })
            .email(response.email().ifEmpty { null })
            .phone(response.phone().ifEmpty { null })
            .businessName(response.businessName().ifEmpty { null })
            .logging(response.loggingAsString().ifEmpty { null })
            .logGroupName(response.logGroupName().ifEmpty { null })
            .createdAt(response.createdAt().toString().ifEmpty { null })
            .modifiedAt(response.modifiedAt().toString().ifEmpty { null })
            .build()
    }

    /**
     * Request to delete a resource
     * @param model resource model
     * @return deleteProfileRequest the aws service request to delete a resource
     */
    fun translateToDeleteRequest(model: ResourceModel): DeleteProfileRequest {
        return DeleteProfileRequest.builder()
            .profileId(model.profileId)
            .build()
    }

    /**
     * Request to update properties of a previously created resource
     * @param model resource model
     * @return updateProfileRequest the aws service request to modify a resource
     */
    fun translateToUpdateRequest(model: ResourceModel): UpdateProfileRequest {
        return UpdateProfileRequest.builder()
            .profileId(model.profileId)
            .name(model.name)
            .email(model.email)
            .phone(model.phone)
            .businessName(model.businessName)
            .build()
    }

    /**
     * Request to list resources
     * @param nextToken token passed to the aws service list resources request
     * @return listProfileRequest the aws service request to list resources within aws account
     */
    fun translateToListRequest(nextToken: String?): ListProfilesRequest {
        return ListProfilesRequest.builder()
            .apply {
                nextToken?.let { nextToken(it) }
            }
            .build()
    }

    /**
     * Translates resource objects from sdk into a resource model (primary identifier only)
     * @param response the profile list resource response
     * @return list of resource models
     */
    fun translateFromListResponse(response: ListProfilesResponse): List<ResourceModel> {
        return response.profiles().map {
            ResourceModel.builder()
                .profileId(it.profileId())
                .name(it.name())
                .businessName(it.businessName())
                .logging(it.loggingAsString())
                .logGroupName(it.logGroupName())
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
            .resourceARN(model.profileArn)
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
            .resourceARN(model.profileArn)
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
}
