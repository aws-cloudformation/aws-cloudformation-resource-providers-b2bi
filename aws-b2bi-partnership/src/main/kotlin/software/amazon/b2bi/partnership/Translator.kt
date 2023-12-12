package software.amazon.b2bi.partnership

import com.google.common.collect.Lists
import software.amazon.awssdk.awscore.AwsRequest
import software.amazon.awssdk.awscore.AwsResponse
import software.amazon.awssdk.awscore.exception.AwsServiceException
import software.amazon.awssdk.services.b2bi.model.AccessDeniedException
import software.amazon.awssdk.services.b2bi.model.ConflictException
import software.amazon.awssdk.services.b2bi.model.CreatePartnershipRequest
import software.amazon.awssdk.services.b2bi.model.DeleteCapabilityRequest
import software.amazon.awssdk.services.b2bi.model.DeletePartnershipRequest
import software.amazon.awssdk.services.b2bi.model.GetPartnershipRequest
import software.amazon.awssdk.services.b2bi.model.GetPartnershipResponse
import software.amazon.awssdk.services.b2bi.model.InternalServerException
import software.amazon.awssdk.services.b2bi.model.ListPartnershipsRequest
import software.amazon.awssdk.services.b2bi.model.ListPartnershipsResponse
import software.amazon.awssdk.services.b2bi.model.ResourceNotFoundException
import software.amazon.awssdk.services.b2bi.model.ServiceQuotaExceededException
import software.amazon.awssdk.services.b2bi.model.TagResourceRequest
import software.amazon.awssdk.services.b2bi.model.ThrottlingException
import software.amazon.awssdk.services.b2bi.model.UntagResourceRequest
import software.amazon.awssdk.services.b2bi.model.UpdatePartnershipRequest
import software.amazon.awssdk.services.b2bi.model.ValidationException
import software.amazon.b2bi.partnership.TagHelper.toSdkTag
import software.amazon.cloudformation.exceptions.BaseHandlerException
import software.amazon.cloudformation.exceptions.CfnAccessDeniedException
import software.amazon.cloudformation.exceptions.CfnAlreadyExistsException
import software.amazon.cloudformation.exceptions.CfnGeneralServiceException
import software.amazon.cloudformation.exceptions.CfnInvalidRequestException
import software.amazon.cloudformation.exceptions.CfnNotFoundException
import software.amazon.cloudformation.exceptions.CfnServiceInternalErrorException
import software.amazon.cloudformation.exceptions.CfnServiceLimitExceededException
import software.amazon.cloudformation.exceptions.CfnThrottlingException
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

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
    fun translateToCreateRequest(model: ResourceModel): CreatePartnershipRequest {
        return CreatePartnershipRequest.builder()
            .profileId(model.profileId)
            .name(model.name)
            .email(model.email)
            .phone(model.phone)
            .capabilities(model.capabilities)
            .tags(model.tags.map { it.toSdkTag() })
            .build()
    }

    /**
     * Request to read a resource
     * @param model resource model
     * @return awsRequest the aws service request to describe a resource
     */
    @JvmStatic
    fun translateToReadRequest(model: ResourceModel): GetPartnershipRequest {
        return GetPartnershipRequest.builder()
            .partnershipId(model.partnershipId)
            .build()
    }

    /**
     * Translates resource object from sdk into a resource model
     * @param awsResponse the aws service describe resource response
     * @return model resource model
     */
    fun translateFromReadResponse(response: GetPartnershipResponse): ResourceModel {
        return ResourceModel.builder()
            .profileId(response.profileId().ifEmpty { null })
            .partnershipId(response.partnershipId().ifEmpty { null })
            .partnershipArn(response.partnershipArn().ifEmpty { null })
            .name(response.name().ifEmpty { null })
            .email(response.email().ifEmpty { null })
            .phone(response.phone().ifEmpty { null })
            .capabilities(response.capabilities())
            .tradingPartnerId(response.tradingPartnerId().ifEmpty { null })
            .createdAt(response.createdAt().toString().ifEmpty { null })
            .modifiedAt(response.modifiedAt().toString().ifEmpty { null })
            .build()
    }

    /**
     * Request to delete a resource
     * @param model resource model
     * @return awsRequest the aws service request to delete a resource
     */
    fun translateToDeleteRequest(model: ResourceModel): DeletePartnershipRequest {
        return DeletePartnershipRequest.builder()
            .partnershipId(model.partnershipId)
            .build()
    }

    /**
     * Request to update properties of a previously created resource
     * @param model resource model
     * @return updateCapabilityRequest the aws service request to modify a resource
     */
    fun translateToUpdateRequest(model: ResourceModel): UpdatePartnershipRequest {
        return UpdatePartnershipRequest.builder()
            .partnershipId(model.partnershipId)
            .name(model.name)
            .capabilities(model.capabilities)
            .build()
    }

    /**
     * Request to list resources
     * @param nextToken token passed to the aws service list resources request
     * @return listCapabilityRequest the aws service request to list resources within aws account
     */
    fun translateToListRequest(nextToken: String?): ListPartnershipsRequest {
        return ListPartnershipsRequest.builder()
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
    fun translateFromListResponse(response: ListPartnershipsResponse): List<ResourceModel> {
        return response.partnerships().map {
            ResourceModel.builder()
                .profileId(it.profileId())
                .partnershipId(it.partnershipId())
                .name(it.name())
                .capabilities(it.capabilities())
                .tradingPartnerId(it.tradingPartnerId())
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
            .resourceARN(model.partnershipArn)
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
            .resourceARN(model.partnershipArn)
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