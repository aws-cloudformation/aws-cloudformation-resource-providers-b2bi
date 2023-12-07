package software.amazon.b2bi.partnership

import software.amazon.awssdk.services.b2bi.model.CreatePartnershipResponse
import software.amazon.awssdk.services.b2bi.model.GetPartnershipResponse
import software.amazon.awssdk.services.b2bi.model.ListPartnershipsResponse
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceResponse
import software.amazon.awssdk.services.b2bi.model.PartnershipSummary
import software.amazon.awssdk.services.b2bi.model.UpdatePartnershipResponse
import software.amazon.b2bi.partnership.TagHelper.toSdkTag
import java.time.Instant

const val TEST_NAME = "Test partnership name"
const val TEST_PROFILE_ID = "p-12345678901234567"
const val TEST_PARTNERSHIP_ID = "ps-12345678901234567"
const val TEST_PARTNERSHIP_EMAIL = "test@amazon.com"
const val TEST_PARTNERSHIP_PHONE = "2341234523"
const val TEST_TRADING_PARTNER_ID = "tp-12345678901234567"
const val TEST_PARTNERSHIP_ARN = "arn:aws:b2bi:us-east-1:123456789012:partnership/$TEST_PARTNERSHIP_ID"
val TEST_PARTNERSHIP_LIST_CAPABILITIES = listOf("ca-12345678901234567")

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
val TEST_CREATE_PARTNERSHIP_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .name(TEST_NAME)
    .email(TEST_PARTNERSHIP_EMAIL)
    .tags(emptyList())
    .build()

val TEST_CREATE_PARTNERSHIP_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS =
    TEST_CREATE_PARTNERSHIP_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS.toBuilder()
        .phone(TEST_PARTNERSHIP_PHONE)
        .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
        .tags(listOf(TEST_RESOURCE_TAG))
        .build()

val TEST_CREATE_PARTNERSHIP_RESPONSE_WITH_REQUIRED_FIELDS = CreatePartnershipResponse.builder()
    .profileId(TEST_PROFILE_ID)
    .partnershipId(TEST_PARTNERSHIP_ID)
    .partnershipArn(TEST_PARTNERSHIP_ARN)
    .name(TEST_NAME)
    .email(TEST_PARTNERSHIP_EMAIL)
    .capabilities(emptyList())
    .createdAt(TEST_INSTANT)
    .build()

val TEST_CREATE_PARTNERSHIP_RESPONSE_WITH_ALL_FIELDS =
    TEST_CREATE_PARTNERSHIP_RESPONSE_WITH_REQUIRED_FIELDS.toBuilder()
        .phone(TEST_PARTNERSHIP_PHONE)
        .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
        .tradingPartnerId(TEST_TRADING_PARTNER_ID)
        .build()

// Delete
val TEST_DELETE_PARTNERSHIP_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .partnershipId(TEST_PARTNERSHIP_ID)
    .build()

// List
val TEST_PARTNERSHIP_SUMMARY_WITH_ALL_FIELDS = PartnershipSummary.builder()
    .profileId(TEST_PROFILE_ID)
    .partnershipId(TEST_PARTNERSHIP_ID)
    .name(TEST_NAME)
    .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
    .tradingPartnerId(TEST_TRADING_PARTNER_ID)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_LIST_PARTNERSHIPS_RESPONSE_WITH_ONE_PARTNERSHIP_WITH_ALL_FIELDS = ListPartnershipsResponse.builder()
    .partnerships(TEST_PARTNERSHIP_SUMMARY_WITH_ALL_FIELDS)
    .build()

val TEST_LIST_PARTNERSHIPS_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .partnershipId(TEST_PARTNERSHIP_ID)
    .name(TEST_NAME)
    .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
    .tradingPartnerId(TEST_TRADING_PARTNER_ID)
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

// Read
val TEST_GET_PARTNERSHIP_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .partnershipId(TEST_PARTNERSHIP_ID)
    .build()

val TEST_GET_PARTNERSHIP_RESPONSE_WITH_ALL_FIELDS = GetPartnershipResponse.builder()
    .profileId(TEST_PROFILE_ID)
    .partnershipId(TEST_PARTNERSHIP_ID)
    .partnershipArn(TEST_PARTNERSHIP_ARN)
    .name(TEST_NAME)
    .email(TEST_PARTNERSHIP_EMAIL)
    .phone(TEST_PARTNERSHIP_PHONE)
    .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
    .tradingPartnerId(TEST_TRADING_PARTNER_ID)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_GET_PARTNERSHIP_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .partnershipId(TEST_PARTNERSHIP_ID)
    .partnershipArn(TEST_PARTNERSHIP_ARN)
    .name(TEST_NAME)
    .email(TEST_PARTNERSHIP_EMAIL)
    .phone(TEST_PARTNERSHIP_PHONE)
    .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
    .tradingPartnerId(TEST_TRADING_PARTNER_ID)
    .tags(listOf(TEST_RESOURCE_TAG))
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

// Update
val TEST_UPDATE_PARTNERSHIP_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .partnershipId(TEST_PARTNERSHIP_ID)
    .name(TEST_NAME)
    .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
    .tags(listOf(TEST_RESOURCE_TAG))
    .build()

val TEST_UPDATE_PARTNERSHIP_RESPONSE_WITH_ALL_FIELDS = UpdatePartnershipResponse.builder()
    .profileId(TEST_PROFILE_ID)
    .partnershipId(TEST_PARTNERSHIP_ID)
    .partnershipArn(TEST_PARTNERSHIP_ARN)
    .name(TEST_NAME)
    .email(TEST_PARTNERSHIP_EMAIL)
    .phone(TEST_PARTNERSHIP_PHONE)
    .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
    .tradingPartnerId(TEST_TRADING_PARTNER_ID)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_UPDATE_PARTNERSHIP_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .profileId(TEST_PROFILE_ID)
    .partnershipId(TEST_PARTNERSHIP_ID)
    .partnershipArn(TEST_PARTNERSHIP_ARN)
    .name(TEST_NAME)
    .email(TEST_PARTNERSHIP_EMAIL)
    .phone(TEST_PARTNERSHIP_PHONE)
    .capabilities(TEST_PARTNERSHIP_LIST_CAPABILITIES)
    .tradingPartnerId(TEST_TRADING_PARTNER_ID)
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .tags(listOf(TEST_RESOURCE_TAG))
    .build()