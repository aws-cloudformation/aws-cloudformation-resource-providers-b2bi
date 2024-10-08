# AWS::B2BI::Transformer

Definition of AWS::B2BI::Transformer Resource Type

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "AWS::B2BI::Transformer",
    "Properties" : {
        "<a href="#editype" title="EdiType">EdiType</a>" : <i><a href="editype.md">EdiType</a></i>,
        "<a href="#fileformat" title="FileFormat">FileFormat</a>" : <i>String</i>,
        "<a href="#inputconversion" title="InputConversion">InputConversion</a>" : <i><a href="inputconversion.md">InputConversion</a></i>,
        "<a href="#mapping" title="Mapping">Mapping</a>" : <i><a href="mapping.md">Mapping</a></i>,
        "<a href="#mappingtemplate" title="MappingTemplate">MappingTemplate</a>" : <i>String</i>,
        "<a href="#name" title="Name">Name</a>" : <i>String</i>,
        "<a href="#outputconversion" title="OutputConversion">OutputConversion</a>" : <i><a href="outputconversion.md">OutputConversion</a></i>,
        "<a href="#sampledocument" title="SampleDocument">SampleDocument</a>" : <i>String</i>,
        "<a href="#sampledocuments" title="SampleDocuments">SampleDocuments</a>" : <i><a href="sampledocuments.md">SampleDocuments</a></i>,
        "<a href="#status" title="Status">Status</a>" : <i>String</i>,
        "<a href="#tags" title="Tags">Tags</a>" : <i>[ <a href="tag.md">Tag</a>, ... ]</i>,
    }
}
</pre>

### YAML

<pre>
Type: AWS::B2BI::Transformer
Properties:
    <a href="#editype" title="EdiType">EdiType</a>: <i><a href="editype.md">EdiType</a></i>
    <a href="#fileformat" title="FileFormat">FileFormat</a>: <i>String</i>
    <a href="#inputconversion" title="InputConversion">InputConversion</a>: <i><a href="inputconversion.md">InputConversion</a></i>
    <a href="#mapping" title="Mapping">Mapping</a>: <i><a href="mapping.md">Mapping</a></i>
    <a href="#mappingtemplate" title="MappingTemplate">MappingTemplate</a>: <i>String</i>
    <a href="#name" title="Name">Name</a>: <i>String</i>
    <a href="#outputconversion" title="OutputConversion">OutputConversion</a>: <i><a href="outputconversion.md">OutputConversion</a></i>
    <a href="#sampledocument" title="SampleDocument">SampleDocument</a>: <i>String</i>
    <a href="#sampledocuments" title="SampleDocuments">SampleDocuments</a>: <i><a href="sampledocuments.md">SampleDocuments</a></i>
    <a href="#status" title="Status">Status</a>: <i>String</i>
    <a href="#tags" title="Tags">Tags</a>: <i>
      - <a href="tag.md">Tag</a></i>
</pre>

## Properties

#### EdiType

_Required_: No

_Type_: <a href="editype.md">EdiType</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### FileFormat

_Required_: No

_Type_: String

_Allowed Values_: <code>XML</code> | <code>JSON</code> | <code>NOT_USED</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### InputConversion

_Required_: No

_Type_: <a href="inputconversion.md">InputConversion</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Mapping

_Required_: No

_Type_: <a href="mapping.md">Mapping</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### MappingTemplate

This shape is deprecated: This is a legacy trait. Please use input-conversion or output-conversion.

_Required_: No

_Type_: String

_Maximum Length_: <code>350000</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Name

_Required_: Yes

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>254</code>

_Pattern_: <code>^[a-zA-Z0-9_-]{1,512}$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### OutputConversion

_Required_: No

_Type_: <a href="outputconversion.md">OutputConversion</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### SampleDocument

This shape is deprecated: This is a legacy trait. Please use input-conversion or output-conversion.

_Required_: No

_Type_: String

_Maximum Length_: <code>1024</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### SampleDocuments

_Required_: No

_Type_: <a href="sampledocuments.md">SampleDocuments</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Status

_Required_: Yes

_Type_: String

_Allowed Values_: <code>active</code> | <code>inactive</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Tags

_Required_: No

_Type_: List of <a href="tag.md">Tag</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the TransformerId.

### Fn::GetAtt

The `Fn::GetAtt` intrinsic function returns a value for a specified attribute of this type. The following are the available attributes and sample return values.

For more information about using the `Fn::GetAtt` intrinsic function, see [Fn::GetAtt](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-getatt.html).

#### CreatedAt

Returns the <code>CreatedAt</code> value.

#### ModifiedAt

Returns the <code>ModifiedAt</code> value.

#### TransformerArn

Returns the <code>TransformerArn</code> value.

#### TransformerId

Returns the <code>TransformerId</code> value.

