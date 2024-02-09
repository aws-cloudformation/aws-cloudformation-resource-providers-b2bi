package software.amazon.b2bi.transformer

import software.amazon.awssdk.services.b2bi.model.CreateTransformerResponse
import software.amazon.awssdk.services.b2bi.model.CreateTransformerRequest
import software.amazon.awssdk.services.b2bi.model.GetTransformerResponse
import software.amazon.awssdk.services.b2bi.model.ListTransformersResponse
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceResponse
import software.amazon.awssdk.services.b2bi.model.Logging
import software.amazon.awssdk.services.b2bi.model.TransformerStatus
import software.amazon.awssdk.services.b2bi.model.TransformerSummary
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerResponse
import software.amazon.b2bi.transformer.TagHelper.toSdkTag
import software.amazon.b2bi.transformer.Translator.translateToSdkEdi

import java.time.Instant

//building edi type
const val TEST_TRANSACTION_SET = "test transaction"
const val TEST_VERSION = "test version"
val TEST_X12: X12Details = X12Details.builder().transactionSet(TEST_TRANSACTION_SET).version(TEST_VERSION).build()

//required
const val TEST_NAME  = "Test transformer name"
const val TEST_FILE_FORMAT = "JSON"
const val TEST_MAPPING_TEMPLATE = "test mapping template"
val TEST_TRANSFORMER_STATUS = TransformerStatus.INACTIVE.toString()
val TEST_RESOURCE_EDI_TYPE: EdiType = EdiType.builder().x12Details(TEST_X12).build()
val TEST_SDK_EDI_TYPE = TEST_RESOURCE_EDI_TYPE.translateToSdkEdi()
const val TEST_TRANSFORMER_ID = "t-12345678901234567"

const val TEST_TRANSFORMER_ARN = "Test transformer ARN"
const val TEST_SAMPLE_DOC = "test/path"
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
val TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS = ResourceModel.builder()
    .name(TEST_NAME)
    .tags(emptyList())
    .fileFormat(TEST_FILE_FORMAT)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .ediType(TEST_RESOURCE_EDI_TYPE)
    .status(TEST_TRANSFORMER_STATUS)
    .tags(emptyList())
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

val TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS =
    TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_FIELDS.toBuilder()
        .sampleDocument(TEST_SAMPLE_DOC)
        .tags(listOf(TEST_RESOURCE_TAG))
        .build()

val TEST_CREATE_TRANSFORMER_RESPONSE_WITH_REQUIRED_FIELDS = CreateTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .fileFormat(TEST_FILE_FORMAT)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .status(TEST_TRANSFORMER_STATUS)
    .ediType(TEST_SDK_EDI_TYPE)
    .createdAt(TEST_INSTANT)
    .status(TEST_TRANSFORMER_STATUS)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .build()

val TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_FIELDS =
    TEST_CREATE_TRANSFORMER_RESPONSE_WITH_REQUIRED_FIELDS.toBuilder()
        .sampleDocument(TEST_SAMPLE_DOC)
        .build()

// Delete
val TEST_DELETE_TRANSFORMER_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .build()

// List
val TEST_TRANSFORMER_SUMMARY_WITH_ALL_FIELDS = TransformerSummary.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .fileFormat(TEST_FILE_FORMAT)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .sampleDocument(TEST_SAMPLE_DOC)
    .ediType(TEST_SDK_EDI_TYPE)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_LIST_TRANSFORMERS_RESPONSE_WITH_ONE_TRANSFORMER_WITH_ALL_FIELDS = ListTransformersResponse.builder()
    .transformers(TEST_TRANSFORMER_SUMMARY_WITH_ALL_FIELDS)
    .build()

val TEST_LIST_TRANSFORMERS_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .fileFormat(TEST_FILE_FORMAT)
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .sampleDocument(TEST_SAMPLE_DOC)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

// READ
val TEST_GET_TRANSFORMER_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .build()

val TEST_GET_TRANSFORMER_RESPONSE_WITH_ALL_FIELDS = GetTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .name(TEST_NAME)
    .fileFormat(TEST_FILE_FORMAT)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .ediType(TEST_SDK_EDI_TYPE)
    .createdAt(TEST_INSTANT)
    .sampleDocument(TEST_SAMPLE_DOC)
    .modifiedAt(TEST_INSTANT)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_GET_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .name(TEST_NAME)
    .fileFormat(TEST_FILE_FORMAT)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .tags(listOf(TEST_RESOURCE_TAG))
    .ediType(TEST_RESOURCE_EDI_TYPE)
    .createdAt(TEST_INSTANT.toString())
    .sampleDocument(TEST_SAMPLE_DOC)
    .modifiedAt(TEST_INSTANT.toString())
    .status(TEST_TRANSFORMER_STATUS)
    .build()

// Update
val TEST_UPDATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .ediType(TEST_RESOURCE_EDI_TYPE)
    .fileFormat(TEST_FILE_FORMAT)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .sampleDocument(TEST_SAMPLE_DOC)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_UPDATE_TRANSFORMER_RESPONSE_WITH_ALL_FIELDS = UpdateTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .ediType(TEST_SDK_EDI_TYPE)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .sampleDocument(TEST_SAMPLE_DOC)
    .fileFormat(TEST_FILE_FORMAT)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_UPDATE_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .ediType(TEST_RESOURCE_EDI_TYPE)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .sampleDocument(TEST_SAMPLE_DOC)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .modifiedAt(TEST_INSTANT.toString())
    .createdAt(TEST_INSTANT.toString())
    .fileFormat(TEST_FILE_FORMAT)
    .status(TEST_TRANSFORMER_STATUS)
    .tags(emptyList())
    .build()
