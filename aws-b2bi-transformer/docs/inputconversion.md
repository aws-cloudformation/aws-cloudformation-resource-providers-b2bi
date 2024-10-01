# AWS::B2BI::Transformer InputConversion

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#fromformat" title="FromFormat">FromFormat</a>" : <i>String</i>,
    "<a href="#formatoptions" title="FormatOptions">FormatOptions</a>" : <i><a href="formatoptions.md">FormatOptions</a></i>
}
</pre>

### YAML

<pre>
<a href="#fromformat" title="FromFormat">FromFormat</a>: <i>String</i>
<a href="#formatoptions" title="FormatOptions">FormatOptions</a>: <i><a href="formatoptions.md">FormatOptions</a></i>
</pre>

## Properties

#### FromFormat

_Required_: Yes

_Type_: String

_Allowed Values_: <code>X12</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### FormatOptions

_Required_: No

_Type_: <a href="formatoptions.md">FormatOptions</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

