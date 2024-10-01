# AWS::B2BI::Partnership X12Delimiters

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#componentseparator" title="ComponentSeparator">ComponentSeparator</a>" : <i>String</i>,
    "<a href="#dataelementseparator" title="DataElementSeparator">DataElementSeparator</a>" : <i>String</i>,
    "<a href="#segmentterminator" title="SegmentTerminator">SegmentTerminator</a>" : <i>String</i>
}
</pre>

### YAML

<pre>
<a href="#componentseparator" title="ComponentSeparator">ComponentSeparator</a>: <i>String</i>
<a href="#dataelementseparator" title="DataElementSeparator">DataElementSeparator</a>: <i>String</i>
<a href="#segmentterminator" title="SegmentTerminator">SegmentTerminator</a>: <i>String</i>
</pre>

## Properties

#### ComponentSeparator

_Required_: No

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>1</code>

_Pattern_: <code>^[!&'()*+,\-./:;?=%@\[\]_{}|<>~^`"]$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### DataElementSeparator

_Required_: No

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>1</code>

_Pattern_: <code>^[!&'()*+,\-./:;?=%@\[\]_{}|<>~^`"]$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### SegmentTerminator

_Required_: No

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>1</code>

_Pattern_: <code>^[!&'()*+,\-./:;?=%@\[\]_{}|<>~^`"]$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

