package software.amazon.b2bi.partnership

import software.amazon.awssdk.services.b2bi.model.CreatePartnershipResponse
import software.amazon.awssdk.services.b2bi.model.GetPartnershipResponse
import software.amazon.awssdk.services.b2bi.model.ListPartnershipsResponse
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceResponse
import software.amazon.awssdk.services.b2bi.model.PartnershipSummary
import software.amazon.awssdk.services.b2bi.model.UpdatePartnershipResponse
import software.amazon.awssdk.services.b2bi.model.CapabilityOptions as SdkCapabilityOptions
import software.amazon.awssdk.services.b2bi.model.OutboundEdiOptions as SdkOutboundEdiOptions
import software.amazon.awssdk.services.b2bi.model.X12Delimiters as SdkX12Delimiters
import software.amazon.awssdk.services.b2bi.model.X12Envelope as SdkX12Envelope
import software.amazon.awssdk.services.b2bi.model.X12FunctionalGroupHeaders as SdkX12FunctionalGroupHeaders
import software.amazon.awssdk.services.b2bi.model.X12InterchangeControlHeaders as SdkX12InterchangeControlHeaders
import software.amazon.awssdk.services.b2bi.model.X12OutboundEdiHeaders as SdkX12OutboundEdiHeaders
import software.amazon.b2bi.partnership.TagHelper.toSdkTag
import software.amazon.b2bi.partnership.CapabilityOptions as ResourceCapabilityOptions
import software.amazon.b2bi.partnership.OutboundEdiOptions as ResourceOutboundEdiOptions
import software.amazon.b2bi.partnership.X12Envelope as ResourceX12Envelope
import software.amazon.b2bi.partnership.X12OutboundEdiHeaders as ResourceX12OutboundEdiHeaders
import software.amazon.b2bi.partnership.X12InterchangeControlHeaders as ResourceX12InterchangeControlHeaders
import software.amazon.b2bi.partnership.X12FunctionalGroupHeaders as ResourceX12FunctionalGroupHeaders
import software.amazon.b2bi.partnership.X12Delimiters as ResourceX12Delimiters

import java.time.Instant

const val TEST_NAME = "Test partnership name"
const val TEST_PROFILE_ID = "p-12345678901234567"
const val TEST_PARTNERSHIP_ID = "ps-12345678901234567"
const val TEST_PARTNERSHIP_EMAIL = "test@amazon.com"
const val TEST_PARTNERSHIP_PHONE = "2341234523"
const val TEST_TRADING_PARTNER_ID = "tp-12345678901234567"
const val TEST_PARTNERSHIP_ARN = "arn:aws:b2bi:us-east-1:123456789012:partnership/$TEST_PARTNERSHIP_ID"
val TEST_PARTNERSHIP_LIST_CAPABILITIES = listOf("ca-12345678901234567")
val TEST_RESOURCE_PARTNERSHIP_CAPABILITY_OPTIONS = ResourceCapabilityOptions.builder().outboundEdi(
    ResourceOutboundEdiOptions.builder().x12(
        ResourceX12Envelope.builder().common(
            ResourceX12OutboundEdiHeaders.builder()
                .interchangeControlHeaders(
                    ResourceX12InterchangeControlHeaders.builder()
                        .senderIdQualifier("01")
                        .senderId("123456789012345")
                        .receiverIdQualifier("01")
                        .receiverId("098765432109876")
                        .repetitionSeparator("]")
                        .acknowledgmentRequestedCode("0")
                        .usageIndicatorCode("T")
                        .build()
                )
                .functionalGroupHeaders(
                    ResourceX12FunctionalGroupHeaders.builder()
                        .applicationSenderCode("ASC")
                        .applicationReceiverCode("ASC")
                        .responsibleAgencyCode("X")
                        .build()
                )
                .delimiters(
                    ResourceX12Delimiters.builder()
                        .componentSeparator(":")
                        .dataElementSeparator("*")
                        .segmentTerminator("~")
                        .build()
                )
                .validateEdi(true)
                .build()
        ).build()
    ).build()
).build()
val TEST_SDK_PARTNERSHIP_CAPABILITY_OPTIONS = SdkCapabilityOptions.builder().outboundEdi(
    SdkOutboundEdiOptions.builder().x12(
        SdkX12Envelope.builder().common(
            SdkX12OutboundEdiHeaders.builder()
                .interchangeControlHeaders(
                    SdkX12InterchangeControlHeaders.builder()
                        .senderIdQualifier("01")
                        .senderId("123456789012345")
                        .receiverIdQualifier("01")
                        .receiverId("098765432109876")
                        .repetitionSeparator("]")
                        .acknowledgmentRequestedCode("0")
                        .usageIndicatorCode("T")
                        .build()
                )
                .functionalGroupHeaders(
                    SdkX12FunctionalGroupHeaders.builder()
                        .applicationSenderCode("ASC")
                        .applicationReceiverCode("ASC")
                        .responsibleAgencyCode("X")
                        .build()
                )
                .delimiters(
                    SdkX12Delimiters.builder()
                        .componentSeparator(":")
                        .dataElementSeparator("*")
                        .segmentTerminator("~")
                        .build()
                )
                .validateEdi(true)
                .build()
        ).build()
    ).build()
).build()

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
        .capabilityOptions(TEST_RESOURCE_PARTNERSHIP_CAPABILITY_OPTIONS)
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
        .capabilityOptions(TEST_SDK_PARTNERSHIP_CAPABILITY_OPTIONS)
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
    .capabilityOptions(TEST_SDK_PARTNERSHIP_CAPABILITY_OPTIONS)
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
    .capabilityOptions(TEST_RESOURCE_PARTNERSHIP_CAPABILITY_OPTIONS)
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
    .capabilityOptions(TEST_SDK_PARTNERSHIP_CAPABILITY_OPTIONS)
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
    .capabilityOptions(TEST_RESOURCE_PARTNERSHIP_CAPABILITY_OPTIONS)
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
    .capabilityOptions(TEST_RESOURCE_PARTNERSHIP_CAPABILITY_OPTIONS)
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
    .capabilityOptions(TEST_SDK_PARTNERSHIP_CAPABILITY_OPTIONS)
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
    .capabilityOptions(TEST_RESOURCE_PARTNERSHIP_CAPABILITY_OPTIONS)
    .tradingPartnerId(TEST_TRADING_PARTNER_ID)
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .tags(listOf(TEST_RESOURCE_TAG))
    .build()