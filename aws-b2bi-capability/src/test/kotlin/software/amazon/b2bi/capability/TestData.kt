package software.amazon.b2bi.capability

import software.amazon.awssdk.services.b2bi.model.CapabilitySummary
import software.amazon.awssdk.services.b2bi.model.CreateCapabilityResponse
import software.amazon.awssdk.services.b2bi.model.GetCapabilityResponse
import software.amazon.awssdk.services.b2bi.model.ListCapabilitiesResponse
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceResponse
import software.amazon.awssdk.services.b2bi.model.UpdateCapabilityResponse
import software.amazon.b2bi.capability.TagHelper.toSdkTag
import software.amazon.b2bi.capability.Translator.toSdkCapabilityConfiguration
import software.amazon.b2bi.capability.Translator.toSdkS3Location
import java.time.Instant

const val TEST_LOG_GROUP_NAME = "Test log group name"
const val TEST_NAME = "Test name"
const val TEST_CAPABILITY_ID = "ca-12345678901234567"
const val TEST_CAPABILITY_ARN = "arn:aws:b2bi:us-east-1:123456789012:capability/$TEST_CAPABILITY_ID"

val TEST_INSTANT = Instant.now()

val TEST_TYPE = "edi"

val TEST_INPUT_S3_LOCATION = S3Location.builder().bucketName("TEST_BUCKET_INPUT").key("TEST_KEY").build()
val TEST_OUTPUT_S3_LOCATION = S3Location.builder().bucketName("TEST_BUCKET_OUTPUT").key("TEST_KEY").build()

val TEST_INSTRUCTION_S3_LOCATION = S3Location.builder().bucketName("TEST_BUCKET_INSTRUCTION").key("TEST_KEY").build()

val TEST_X12_DETAILS = X12Details.builder().transactionSet("X12_110").version("VERSION_4010").build()
val TEST_EDI_TYPE = EdiType.builder().x12Details(TEST_X12_DETAILS).build()
val TEST_EDI_CONFIGURATION = EdiConfiguration.builder()
    .type(TEST_EDI_TYPE)
    .inputLocation(TEST_INPUT_S3_LOCATION)
    .outputLocation(TEST_OUTPUT_S3_LOCATION)
    .transformerId("tr-12345678901234567")
    .build()

val TEST_CAPABILITY_CONFIGURATION = CapabilityConfiguration.builder().edi(TEST_EDI_CONFIGURATION).build()

// Tags
val TEST_RESOURCE_TAG = Tag.builder()
    .key("testKey")
    .value("testValue")
    .build()

val TEST_LIST_TAGS_FOR_RESOURCE_RESPONSE = ListTagsForResourceResponse.builder()
    .tags(TEST_RESOURCE_TAG.toSdkTag())
    .build()

// Create
val TEST_CREATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS = ResourceModel.builder()
    .name(TEST_NAME)
    .type(TEST_TYPE)
    .configuration(TEST_CAPABILITY_CONFIGURATION)
    .instructionsDocuments(emptyList())
    .tags(emptyList())
    .build()

val TEST_CREATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS =
    TEST_CREATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS.toBuilder()
        .instructionsDocuments(listOf(TEST_INSTRUCTION_S3_LOCATION))
        .tags(listOf(TEST_RESOURCE_TAG))
        .build()

val TEST_CREATE_CAPABILITY_RESPONSE_WITH_REQUIRED_FIELDS = CreateCapabilityResponse.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .capabilityArn(TEST_CAPABILITY_ARN)
    .name(TEST_NAME)
    .type(TEST_TYPE)
    .configuration(TEST_CAPABILITY_CONFIGURATION.toSdkCapabilityConfiguration())
    .createdAt(TEST_INSTANT)
    .build()

val TEST_CREATE_CAPABILITY_RESPONSE_WITH_ALL_FIELDS =
    TEST_CREATE_CAPABILITY_RESPONSE_WITH_REQUIRED_FIELDS.toBuilder()
        .instructionsDocuments(listOf(TEST_INSTRUCTION_S3_LOCATION).map { it.toSdkS3Location() })
        .build()

// Delete
val TEST_DELETE_CAPABILITY_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .build()

// List

val TEST_CAPABILITY_SUMMARY_WITH_ALL_FIELDS = CapabilitySummary.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .name(TEST_NAME)
    .type(TEST_TYPE)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_LIST_CAPABILITIES_RESPONSE_WITH_ONE_CAPABILITY_WITH_ALL_FIELDS = ListCapabilitiesResponse.builder()
    .capabilities(TEST_CAPABILITY_SUMMARY_WITH_ALL_FIELDS)
    .build()

val TEST_LIST_CAPABILITIES_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .name(TEST_NAME)
    .type(TEST_TYPE)
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

// Read
val TEST_GET_CAPABILITY_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .build()

val TEST_GET_CAPABILITY_RESPONSE_WITH_ALL_FIELDS = GetCapabilityResponse.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .capabilityArn(TEST_CAPABILITY_ARN)
    .name(TEST_NAME)
    .type(TEST_TYPE)
    .configuration(TEST_CAPABILITY_CONFIGURATION.toSdkCapabilityConfiguration())
    .instructionsDocuments(listOf(TEST_INSTRUCTION_S3_LOCATION.toSdkS3Location()))
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_GET_CAPABILITY_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .capabilityArn(TEST_CAPABILITY_ARN)
    .name(TEST_NAME)
    .type(TEST_TYPE)
    .configuration(TEST_CAPABILITY_CONFIGURATION)
    .instructionsDocuments(listOf(TEST_INSTRUCTION_S3_LOCATION))
    .tags(listOf(TEST_RESOURCE_TAG))
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

// Update
val TEST_UPDATE_CAPABILITY_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .name(TEST_NAME)
    .configuration(TEST_CAPABILITY_CONFIGURATION)
    .instructionsDocuments(listOf(TEST_INSTRUCTION_S3_LOCATION))
    .tags(listOf(TEST_RESOURCE_TAG))
    .build()

val TEST_UPDATE_CAPABILITY_RESPONSE_WITH_ALL_FIELDS = UpdateCapabilityResponse.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .capabilityArn(TEST_CAPABILITY_ARN)
    .name(TEST_NAME)
    .type(TEST_TYPE)
    .configuration(TEST_CAPABILITY_CONFIGURATION.toSdkCapabilityConfiguration())
    .instructionsDocuments(listOf(TEST_INSTRUCTION_S3_LOCATION.toSdkS3Location()))
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_UPDATE_CAPABILITY_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .capabilityId(TEST_CAPABILITY_ID)
    .capabilityArn(TEST_CAPABILITY_ARN)
    .name(TEST_NAME)
    .type(TEST_TYPE)
    .configuration(TEST_CAPABILITY_CONFIGURATION)
    .instructionsDocuments(listOf(TEST_INSTRUCTION_S3_LOCATION))
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .tags(listOf(TEST_RESOURCE_TAG))
    .build()
