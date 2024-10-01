package software.amazon.b2bi.transformer

import software.amazon.awssdk.services.b2bi.model.CreateTransformerResponse
import software.amazon.awssdk.services.b2bi.model.GetTransformerResponse
import software.amazon.awssdk.services.b2bi.model.ListTransformersResponse
import software.amazon.awssdk.services.b2bi.model.ListTagsForResourceResponse
import software.amazon.awssdk.services.b2bi.model.TransformerStatus
import software.amazon.awssdk.services.b2bi.model.TransformerSummary
import software.amazon.awssdk.services.b2bi.model.UpdateTransformerResponse
import software.amazon.b2bi.transformer.TagHelper.toSdkTag
import software.amazon.b2bi.transformer.Translator.translateToSdkEdi
import software.amazon.b2bi.transformer.Translator.translateToSdkInputConversion
import software.amazon.b2bi.transformer.Translator.translateToSdkMapping
import software.amazon.b2bi.transformer.Translator.translateToSdkOutputConversion
import software.amazon.b2bi.transformer.Translator.translateToSdkSampleDocuments

import java.time.Instant

//building edi type
const val TEST_TRANSACTION_SET = "test transaction"
const val TEST_VERSION = "test version"
val TEST_X12: X12Details = X12Details.builder().transactionSet(TEST_TRANSACTION_SET).version(TEST_VERSION).build()

//required legacy fields
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

// required new fields
const val TEST_MAPPING_LANGUAGE = "JSONATA"
const val TEST_X12_FORMAT = "X12"
const val TEST_BUCKET_NAME = "test-s3-bucket"
const val TEST_INPUT_SAMPLE_DOC = "input-doc"
const val TEST_OUTPUT_SAMPLE_DOC = "output-doc"
val TEST_X12_DETAILS = X12Details.builder().transactionSet(TEST_TRANSACTION_SET).version(TEST_VERSION).build()
val TEST_FORMAT_OPTIONS = FormatOptions.builder()
    .x12(TEST_X12_DETAILS)
    .build()
val TEST_SAMPLE_DOC_KEYS = SampleDocumentKeys.builder()
    .input(TEST_INPUT_SAMPLE_DOC)
    .output(TEST_OUTPUT_SAMPLE_DOC)
    .build()
val TEST_INPUT_CONVERSION = InputConversion.builder()
    .fromFormat(TEST_X12_FORMAT)
    .formatOptions(TEST_FORMAT_OPTIONS)
    .build()
val TEST_SDK_INPUT_CONVERSION = TEST_INPUT_CONVERSION.translateToSdkInputConversion()
val TEST_OUTPUT_CONVERSION = OutputConversion.builder()
    .toFormat(TEST_X12_FORMAT)
    .formatOptions(TEST_FORMAT_OPTIONS)
    .build()
val TEST_SDK_OUTPUT_CONVERSION = TEST_OUTPUT_CONVERSION.translateToSdkOutputConversion()
val TEST_MAPPING = Mapping.builder().templateLanguage(TEST_MAPPING_LANGUAGE).template(TEST_MAPPING_TEMPLATE).build()
val TEST_SDK_MAPPING = TEST_MAPPING.translateToSdkMapping()
val TEST_SAMPLE_DOCUMENTS = SampleDocuments.builder()
    .bucketName(TEST_BUCKET_NAME)
    .keys(listOf(TEST_SAMPLE_DOC_KEYS))
    .build()
val TEST_SDK_SAMPLE_DOCUMENTS = TEST_SAMPLE_DOCUMENTS.translateToSdkSampleDocuments()

// Tags
val TEST_RESOURCE_TAG = Tag.builder()
    .key("testKey")
    .value("testValue")
    .build()

val TEST_LIST_TAGS_FOR_RESOURCE_RESPONSE = ListTagsForResourceResponse.builder()
    .tags(TEST_RESOURCE_TAG.toSdkTag())
    .build()

// This test data is used to test transformers with legacy fields
// Create
val TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_LEGACY_FIELDS = ResourceModel.builder()
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

val TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS =
    TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_LEGACY_FIELDS.toBuilder()
        .sampleDocument(TEST_SAMPLE_DOC)
        .tags(listOf(TEST_RESOURCE_TAG))
        .build()

val TEST_CREATE_TRANSFORMER_RESPONSE_WITH_REQUIRED_LEGACY_FIELDS = CreateTransformerResponse.builder()
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

val TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_LEGACY_FIELDS =
    TEST_CREATE_TRANSFORMER_RESPONSE_WITH_REQUIRED_LEGACY_FIELDS.toBuilder()
        .sampleDocument(TEST_SAMPLE_DOC)
        .build()

// Delete
val TEST_DELETE_TRANSFORMER_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .build()

// List
val TEST_TRANSFORMER_SUMMARY_WITH_ALL_LEGACY_FIELDS = TransformerSummary.builder()
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

val TEST_LIST_TRANSFORMERS_RESPONSE_WITH_ONE_TRANSFORMER_WITH_ALL_LEGACY_FIELDS = ListTransformersResponse.builder()
    .transformers(TEST_TRANSFORMER_SUMMARY_WITH_ALL_LEGACY_FIELDS)
    .build()

val TEST_LIST_TRANSFORMERS_RESPONSE_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS = ResourceModel.builder()
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

val TEST_GET_TRANSFORMER_RESPONSE_WITH_ALL_LEGACY_FIELDS = GetTransformerResponse.builder()
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

val TEST_GET_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS = ResourceModel.builder()
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
val TEST_UPDATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .ediType(TEST_RESOURCE_EDI_TYPE)
    .fileFormat(TEST_FILE_FORMAT)
    .mappingTemplate(TEST_MAPPING_TEMPLATE)
    .sampleDocument(TEST_SAMPLE_DOC)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_UPDATE_TRANSFORMER_RESPONSE_WITH_ALL_LEGACY_FIELDS = UpdateTransformerResponse.builder()
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

val TEST_UPDATE_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_LEGACY_FIELDS = ResourceModel.builder()
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

// This test data is used to test transformers with new fields
// Create
val TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_INBOUND_FIELDS = ResourceModel.builder()
    .name(TEST_NAME)
    .inputConversion(TEST_INPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .tags(emptyList())
    .status(TEST_TRANSFORMER_STATUS)
    .tags(emptyList())
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

val TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS =
    TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_INBOUND_FIELDS.toBuilder()
        .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
        .tags(listOf(TEST_RESOURCE_TAG))
        .build()

val TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_INBOUND_FIELDS = CreateTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .inputConversion(TEST_SDK_INPUT_CONVERSION)
    .mapping(TEST_SDK_MAPPING)
    .sampleDocuments(TEST_SDK_SAMPLE_DOCUMENTS)
    .createdAt(TEST_INSTANT)
    .status(TEST_TRANSFORMER_STATUS)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .build()

val TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_OUTBOUND_FIELDS = ResourceModel.builder()
    .name(TEST_NAME)
    .outputConversion(TEST_OUTPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .tags(emptyList())
    .status(TEST_TRANSFORMER_STATUS)
    .tags(emptyList())
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .build()

val TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS =
    TEST_CREATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_REQUIRED_OUTBOUND_FIELDS.toBuilder()
        .tags(listOf(TEST_RESOURCE_TAG))
        .build()

val TEST_CREATE_TRANSFORMER_RESPONSE_WITH_ALL_OUTBOUND_FIELDS = CreateTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .outputConversion(TEST_SDK_OUTPUT_CONVERSION)
    .mapping(TEST_SDK_MAPPING)
    .sampleDocuments(TEST_SDK_SAMPLE_DOCUMENTS)
    .createdAt(TEST_INSTANT)
    .status(TEST_TRANSFORMER_STATUS)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .build()

// List
val TEST_TRANSFORMER_SUMMARY_WITH_ALL_INBOUND_FIELDS = TransformerSummary.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .inputConversion(TEST_SDK_INPUT_CONVERSION)
    .mapping(TEST_SDK_MAPPING)
    .sampleDocuments(TEST_SDK_SAMPLE_DOCUMENTS)
    .status(TEST_TRANSFORMER_STATUS)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_LIST_TRANSFORMERS_RESPONSE_WITH_ONE_TRANSFORMER_WITH_ALL_INBOUND_FIELDS = ListTransformersResponse.builder()
    .transformers(TEST_TRANSFORMER_SUMMARY_WITH_ALL_INBOUND_FIELDS)
    .build()

val TEST_LIST_TRANSFORMERS_RESPONSE_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .inputConversion(TEST_INPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_TRANSFORMER_SUMMARY_WITH_ALL_OUTBOUND_FIELDS = TransformerSummary.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .outputConversion(TEST_SDK_OUTPUT_CONVERSION)
    .mapping(TEST_SDK_MAPPING)
    .sampleDocuments(TEST_SDK_SAMPLE_DOCUMENTS)
    .status(TEST_TRANSFORMER_STATUS)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .build()

val TEST_LIST_TRANSFORMERS_RESPONSE_WITH_ONE_TRANSFORMER_WITH_ALL_OUTBOUND_FIELDS = ListTransformersResponse.builder()
    .transformers(TEST_TRANSFORMER_SUMMARY_WITH_ALL_OUTBOUND_FIELDS)
    .build()

val TEST_LIST_TRANSFORMERS_RESPONSE_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .outputConversion(TEST_OUTPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .status(TEST_TRANSFORMER_STATUS)
    .build()

// READ
val TEST_GET_INBOUND_TRANSFORMER_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .build()

val TEST_GET_OUTBOUND_TRANSFORMER_REQUEST_RESOURCE_MODEL = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .build()

val TEST_GET_TRANSFORMER_RESPONSE_WITH_ALL_INBOUND_FIELDS = GetTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .name(TEST_NAME)
    .inputConversion(TEST_SDK_INPUT_CONVERSION)
    .mapping(TEST_SDK_MAPPING)
    .sampleDocuments(TEST_SDK_SAMPLE_DOCUMENTS)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_GET_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .name(TEST_NAME)
    .inputConversion(TEST_INPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .tags(listOf(TEST_RESOURCE_TAG))
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_GET_TRANSFORMER_RESPONSE_WITH_ALL_OUTBOUND_FIELDS = GetTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .name(TEST_NAME)
    .outputConversion(TEST_SDK_OUTPUT_CONVERSION)
    .mapping(TEST_SDK_MAPPING)
    .sampleDocuments(TEST_SDK_SAMPLE_DOCUMENTS)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_GET_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .name(TEST_NAME)
    .outputConversion(TEST_OUTPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .tags(listOf(TEST_RESOURCE_TAG))
    .createdAt(TEST_INSTANT.toString())
    .modifiedAt(TEST_INSTANT.toString())
    .status(TEST_TRANSFORMER_STATUS)
    .build()

// Update
val TEST_UPDATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .inputConversion(TEST_INPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_UPDATE_TRANSFORMER_RESPONSE_WITH_ALL_INBOUND_FIELDS = UpdateTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .inputConversion(TEST_SDK_INPUT_CONVERSION)
    .mapping(TEST_SDK_MAPPING)
    .sampleDocuments(TEST_SDK_SAMPLE_DOCUMENTS)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_UPDATE_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_INBOUND_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .inputConversion(TEST_INPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .modifiedAt(TEST_INSTANT.toString())
    .createdAt(TEST_INSTANT.toString())
    .status(TEST_TRANSFORMER_STATUS)
    .tags(emptyList())
    .build()

val TEST_UPDATE_TRANSFORMER_REQUEST_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .outputConversion(TEST_OUTPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_UPDATE_TRANSFORMER_RESPONSE_WITH_ALL_OUTBOUND_FIELDS = UpdateTransformerResponse.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .outputConversion(TEST_SDK_OUTPUT_CONVERSION)
    .mapping(TEST_SDK_MAPPING)
    .sampleDocuments(TEST_SDK_SAMPLE_DOCUMENTS)
    .createdAt(TEST_INSTANT)
    .modifiedAt(TEST_INSTANT)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .status(TEST_TRANSFORMER_STATUS)
    .build()

val TEST_UPDATE_TRANSFORMER_RESPONSE_RESOURCE_MODEL_WITH_ALL_OUTBOUND_FIELDS = ResourceModel.builder()
    .transformerId(TEST_TRANSFORMER_ID)
    .name(TEST_NAME)
    .outputConversion(TEST_OUTPUT_CONVERSION)
    .mapping(TEST_MAPPING)
    .sampleDocuments(TEST_SAMPLE_DOCUMENTS)
    .transformerArn(TEST_TRANSFORMER_ARN)
    .modifiedAt(TEST_INSTANT.toString())
    .createdAt(TEST_INSTANT.toString())
    .status(TEST_TRANSFORMER_STATUS)
    .tags(emptyList())
    .build()
