# AWS::B2Bi::Capability EdiConfiguration

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#type" title="Type">Type</a>" : <i><a href="editype.md">EdiType</a></i>,
    "<a href="#inputlocation" title="InputLocation">InputLocation</a>" : <i><a href="s3location.md">S3Location</a></i>,
    "<a href="#outputlocation" title="OutputLocation">OutputLocation</a>" : <i><a href="s3location.md">S3Location</a></i>,
    "<a href="#transformerid" title="TransformerId">TransformerId</a>" : <i>String</i>
}
</pre>

### YAML

<pre>
<a href="#type" title="Type">Type</a>: <i><a href="editype.md">EdiType</a></i>
<a href="#inputlocation" title="InputLocation">InputLocation</a>: <i><a href="s3location.md">S3Location</a></i>
<a href="#outputlocation" title="OutputLocation">OutputLocation</a>: <i><a href="s3location.md">S3Location</a></i>
<a href="#transformerid" title="TransformerId">TransformerId</a>: <i>String</i>
</pre>

## Properties

#### Type

_Required_: Yes

_Type_: <a href="editype.md">EdiType</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### InputLocation

_Required_: Yes

_Type_: <a href="s3location.md">S3Location</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### OutputLocation

_Required_: Yes

_Type_: <a href="s3location.md">S3Location</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### TransformerId

_Required_: Yes

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>64</code>

_Pattern_: <code>^[a-zA-Z0-9_-]+$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

