# AWS::B2BI::Partnership X12FunctionalGroupHeaders

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#applicationsendercode" title="ApplicationSenderCode">ApplicationSenderCode</a>" : <i>String</i>,
    "<a href="#applicationreceivercode" title="ApplicationReceiverCode">ApplicationReceiverCode</a>" : <i>String</i>,
    "<a href="#responsibleagencycode" title="ResponsibleAgencyCode">ResponsibleAgencyCode</a>" : <i>String</i>
}
</pre>

### YAML

<pre>
<a href="#applicationsendercode" title="ApplicationSenderCode">ApplicationSenderCode</a>: <i>String</i>
<a href="#applicationreceivercode" title="ApplicationReceiverCode">ApplicationReceiverCode</a>: <i>String</i>
<a href="#responsibleagencycode" title="ResponsibleAgencyCode">ResponsibleAgencyCode</a>: <i>String</i>
</pre>

## Properties

#### ApplicationSenderCode

_Required_: No

_Type_: String

_Minimum Length_: <code>2</code>

_Maximum Length_: <code>15</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### ApplicationReceiverCode

_Required_: No

_Type_: String

_Minimum Length_: <code>2</code>

_Maximum Length_: <code>15</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### ResponsibleAgencyCode

_Required_: No

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>2</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

