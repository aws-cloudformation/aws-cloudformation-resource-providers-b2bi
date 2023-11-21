# AWS::B2Bi::Capability X12Details

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#transactionset" title="TransactionSet">TransactionSet</a>" : <i>String</i>,
    "<a href="#version" title="Version">Version</a>" : <i>String</i>
}
</pre>

### YAML

<pre>
<a href="#transactionset" title="TransactionSet">TransactionSet</a>: <i>String</i>
<a href="#version" title="Version">Version</a>: <i>String</i>
</pre>

## Properties

#### TransactionSet

_Required_: No

_Type_: String

_Allowed Values_: <code>X12_110</code> | <code>X12_180</code> | <code>X12_204</code> | <code>X12_210</code> | <code>X12_214</code> | <code>X12_215</code> | <code>X12_310</code> | <code>X12_315</code> | <code>X12_322</code> | <code>X12_404</code> | <code>X12_410</code> | <code>X12_820</code> | <code>X12_824</code> | <code>X12_830</code> | <code>X12_846</code> | <code>X12_850</code> | <code>X12_852</code> | <code>X12_855</code> | <code>X12_856</code> | <code>X12_860</code> | <code>X12_861</code> | <code>X12_864</code> | <code>X12_940</code> | <code>X12_990</code> | <code>X12_997</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Version

_Required_: No

_Type_: String

_Allowed Values_: <code>VERSION_4010</code> | <code>VERSION_4030</code> | <code>VERSION_5010</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

