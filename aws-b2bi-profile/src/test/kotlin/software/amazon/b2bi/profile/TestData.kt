package software.amazon.b2bi.profile

import software.amazon.awssdk.services.b2bi.model.CreateProfileResponse
import software.amazon.awssdk.services.b2bi.model.GetProfileResponse
import software.amazon.awssdk.services.b2bi.model.ListProfilesResponse
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceResponse
import software.amazon.awssdk.services.b2bi.model.Logging
import software.amazon.awssdk.services.b2bi.model.ProfileSummary
import software.amazon.awssdk.services.b2bi.model.UpdateProfileResponse
import software.amazon.b2bi.profile.TagHelper.toSdkTag
import java.time.Instant

const val TEST_BUSINESS_NAME  = "Test business name"
const val TEST_EMAIL = "test@amazon.com"
const val TEST_LOG_GROUP_NAME = "Test log group name"
const val TEST_NAME = "Test name"
const val TEST_PHONE = "1234567890"
const val TEST_PROFILE_ARN = "Test profile ARN"
const val TEST_PROFILE_ID = "p-12345678901234567"

val TEST_INSTANT = Instant.now()

// Tags
val TEST_RESOURCE_TAG = Tag.builder()
    .key("testKey")
    .value("testValue")
    .build()

val TEST_LIST_TAGS_FOR_RESOURCE_RESPONSE = ListTagsForResourceResponse.builder()
    .tags(TEST_RESOURCE_TAG.toSdkTag())
    .build()

// Create
val TEST_CREATE_PROFILE_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS = ResourceModel.builder()
    .name(TEST_NAME)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .logging(Logging.ENABLED.toString())
    .tags(emptyList())
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

val TEST_CREATE_PROFILE_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS =
    TEST_CREATE_PROFILE_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS.toBuilder()
        .email(TEST_EMAIL)
        .logGroupName(TEST_LOG_GROUP_NAME)
        .tags(listOf(TEST_RESOURCE_TAG))
        .build()

val TEST_CREATE_PROFILE_RESPONSE_WITH_REQUIRED_FIELDS = CreateProfileResponse.builder()
    .profileId(TEST_PROFILE_ID)
    .name(TEST_NAME)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .logging(Logging.ENABLED)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_CREATE_PROFILE_RESPONSE_WITH_ALL_FIELDS =
    TEST_CREATE_PROFILE_RESPONSE_WITH_REQUIRED_FIELDS.toBuilder()
        .email(TEST_EMAIL)
        .logGroupName(TEST_LOG_GROUP_NAME)
        .build()

// Read
val TEST_GET_PROFILE_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .build()

val TEST_GET_PROFILE_RESPONSE_WITH_ALL_FIELDS = GetProfileResponse.builder()
    .profileId(TEST_PROFILE_ID)
    .profileArn(TEST_PROFILE_ARN)
    .name(TEST_NAME)
    .email(TEST_EMAIL)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .logging(Logging.ENABLED.toString())
    .logGroupName(TEST_LOG_GROUP_NAME)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_GET_PROFILE_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .profileArn(TEST_PROFILE_ARN)
    .name(TEST_NAME)
    .email(TEST_EMAIL)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .logging(Logging.ENABLED.toString())
    .logGroupName(TEST_LOG_GROUP_NAME)
    .tags(listOf(TEST_RESOURCE_TAG))
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

// Update
val TEST_UPDATE_PROFILE_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .name(TEST_NAME)
    .email(TEST_EMAIL)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .tags(listOf(TEST_RESOURCE_TAG))
    .build()

val TEST_UPDATE_PROFILE_RESPONSE_WITH_ALL_FIELDS = UpdateProfileResponse.builder()
    .profileId(TEST_PROFILE_ID)
    .name(TEST_NAME)
    .email(TEST_EMAIL)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .logging(Logging.ENABLED.toString())
    .logGroupName(TEST_LOG_GROUP_NAME)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_UPDATE_PROFILE_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .name(TEST_NAME)
    .email(TEST_EMAIL)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .tags(listOf(TEST_RESOURCE_TAG))
    .build()

// Delete
val TEST_DELETE_PROFILE_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .build()

// List
val TEST_PROFILE_SUMMARY_WITH_ALL_FIELDS = ProfileSummary.builder()
    .profileId(TEST_PROFILE_ID)
    .name(TEST_NAME)
    .email(TEST_EMAIL)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .logging(Logging.ENABLED.toString())
    .logGroupName(TEST_LOG_GROUP_NAME)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_LIST_PROFILES_RESPONSE_WITH_ONE_PROFILE_WITH_ALL_FIELDS = ListProfilesResponse.builder()
    .profiles(TEST_PROFILE_SUMMARY_WITH_ALL_FIELDS)
    .build()

val TEST_LIST_PROFILES_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .name(TEST_NAME)
    .email(TEST_EMAIL)
    .phone(TEST_PHONE)
    .businessName(TEST_BUSINESS_NAME)
    .logging(Logging.ENABLED.toString())
    .logGroupName(TEST_LOG_GROUP_NAME)
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()
