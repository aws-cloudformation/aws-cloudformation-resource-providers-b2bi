# AWS::B2BI::Capability

Definition of AWS::B2BI::Capability Resource Type

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "AWS::B2BI::Capability",
    "Properties" : {
        "<a href="#configuration" title="Configuration">Configuration</a>" : <i><a href="capabilityconfiguration.md">CapabilityConfiguration</a></i>,
        "<a href="#instructionsdocuments" title="InstructionsDocuments">InstructionsDocuments</a>" : <i>[ <a href="s3location.md">S3Location</a>, ... ]</i>,
        "<a href="#name" title="Name">Name</a>" : <i>String</i>,
        "<a href="#tags" title="Tags">Tags</a>" : <i>[ <a href="tag.md">Tag</a>, ... ]</i>,
        "<a href="#type" title="Type">Type</a>" : <i>String</i>
    }
}
</pre>

### YAML

<pre>
Type: AWS::B2BI::Capability
Properties:
    <a href="#configuration" title="Configuration">Configuration</a>: <i><a href="capabilityconfiguration.md">CapabilityConfiguration</a></i>
    <a href="#instructionsdocuments" title="InstructionsDocuments">InstructionsDocuments</a>: <i>
      - <a href="s3location.md">S3Location</a></i>
    <a href="#name" title="Name">Name</a>: <i>String</i>
    <a href="#tags" title="Tags">Tags</a>: <i>
      - <a href="tag.md">Tag</a></i>
    <a href="#type" title="Type">Type</a>: <i>String</i>
</pre>

## Properties

#### Configuration

_Required_: Yes

_Type_: <a href="capabilityconfiguration.md">CapabilityConfiguration</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### InstructionsDocuments

_Required_: No

_Type_: List of <a href="s3location.md">S3Location</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Name

_Required_: Yes

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>254</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Tags

_Required_: No

_Type_: List of <a href="tag.md">Tag</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Type

_Required_: Yes

_Type_: String

_Allowed Values_: <code>edi</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the CapabilityId.

### Fn::GetAtt

The `Fn::GetAtt` intrinsic function returns a value for a specified attribute of this type. The following are the available attributes and sample return values.

For more information about using the `Fn::GetAtt` intrinsic function, see [Fn::GetAtt](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-getatt.html).

#### CapabilityArn

Returns the <code>CapabilityArn</code> value.

#### CapabilityId

Returns the <code>CapabilityId</code> value.

#### CreatedAt

Returns the <code>CreatedAt</code> value.

#### ModifiedAt

Returns the <code>ModifiedAt</code> value.

