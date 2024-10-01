# AWS::B2BI::Partnership X12InterchangeControlHeaders

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#senderidqualifier" title="SenderIdQualifier">SenderIdQualifier</a>" : <i>String</i>,
    "<a href="#senderid" title="SenderId">SenderId</a>" : <i>String</i>,
    "<a href="#receiveridqualifier" title="ReceiverIdQualifier">ReceiverIdQualifier</a>" : <i>String</i>,
    "<a href="#receiverid" title="ReceiverId">ReceiverId</a>" : <i>String</i>,
    "<a href="#repetitionseparator" title="RepetitionSeparator">RepetitionSeparator</a>" : <i>String</i>,
    "<a href="#acknowledgmentrequestedcode" title="AcknowledgmentRequestedCode">AcknowledgmentRequestedCode</a>" : <i>String</i>,
    "<a href="#usageindicatorcode" title="UsageIndicatorCode">UsageIndicatorCode</a>" : <i>String</i>
}
</pre>

### YAML

<pre>
<a href="#senderidqualifier" title="SenderIdQualifier">SenderIdQualifier</a>: <i>String</i>
<a href="#senderid" title="SenderId">SenderId</a>: <i>String</i>
<a href="#receiveridqualifier" title="ReceiverIdQualifier">ReceiverIdQualifier</a>: <i>String</i>
<a href="#receiverid" title="ReceiverId">ReceiverId</a>: <i>String</i>
<a href="#repetitionseparator" title="RepetitionSeparator">RepetitionSeparator</a>: <i>String</i>
<a href="#acknowledgmentrequestedcode" title="AcknowledgmentRequestedCode">AcknowledgmentRequestedCode</a>: <i>String</i>
<a href="#usageindicatorcode" title="UsageIndicatorCode">UsageIndicatorCode</a>: <i>String</i>
</pre>

## Properties

#### SenderIdQualifier

_Required_: No

_Type_: String

_Minimum Length_: <code>2</code>

_Maximum Length_: <code>2</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### SenderId

_Required_: No

_Type_: String

_Minimum Length_: <code>15</code>

_Maximum Length_: <code>15</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### ReceiverIdQualifier

_Required_: No

_Type_: String

_Minimum Length_: <code>2</code>

_Maximum Length_: <code>2</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### ReceiverId

_Required_: No

_Type_: String

_Minimum Length_: <code>15</code>

_Maximum Length_: <code>15</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### RepetitionSeparator

_Required_: No

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>1</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### AcknowledgmentRequestedCode

_Required_: No

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>1</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### UsageIndicatorCode

_Required_: No

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>1</code>

_Pattern_: <code>^[a-zA-Z0-9]*$</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

