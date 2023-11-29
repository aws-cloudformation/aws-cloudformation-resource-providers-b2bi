# AWS::B2BI::Partnership

<<<<<<< Updated upstream
An example resource schema demonstrating some basic constructs and validation rules.
=======
Definition of AWS::B2BI::Partnership Resource Type
>>>>>>> Stashed changes

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "AWS::B2BI::Partnership",
    "Properties" : {
<<<<<<< Updated upstream
        "<a href="#title" title="Title">Title</a>" : <i>String</i>,
        "<a href="#coversheetincluded" title="CoverSheetIncluded">CoverSheetIncluded</a>" : <i>Boolean</i>,
        "<a href="#duedate" title="DueDate">DueDate</a>" : <i>String</i>,
        "<a href="#approvaldate" title="ApprovalDate">ApprovalDate</a>" : <i>String</i>,
        "<a href="#memo" title="Memo">Memo</a>" : <i><a href="memo.md">Memo</a></i>,
        "<a href="#secondcopyofmemo" title="SecondCopyOfMemo">SecondCopyOfMemo</a>" : <i><a href="memo.md">Memo</a></i>,
        "<a href="#testcode" title="TestCode">TestCode</a>" : <i>String</i>,
        "<a href="#authors" title="Authors">Authors</a>" : <i>[ String, ... ]</i>,
        "<a href="#tags" title="Tags">Tags</a>" : <i>[ <a href="tag.md">Tag</a>, ... ]</i>
=======
        "<a href="#capabilities" title="Capabilities">Capabilities</a>" : <i>[ String, ... ]</i>,
        "<a href="#email" title="Email">Email</a>" : <i>String</i>,
        "<a href="#name" title="Name">Name</a>" : <i>String</i>,
        "<a href="#phone" title="Phone">Phone</a>" : <i>String</i>,
        "<a href="#profileid" title="ProfileId">ProfileId</a>" : <i>String</i>,
        "<a href="#tags" title="Tags">Tags</a>" : <i>[ <a href="tag.md">Tag</a>, ... ]</i>,
>>>>>>> Stashed changes
    }
}
</pre>

### YAML

<pre>
Type: AWS::B2BI::Partnership
Properties:
<<<<<<< Updated upstream
    <a href="#title" title="Title">Title</a>: <i>String</i>
    <a href="#coversheetincluded" title="CoverSheetIncluded">CoverSheetIncluded</a>: <i>Boolean</i>
    <a href="#duedate" title="DueDate">DueDate</a>: <i>String</i>
    <a href="#approvaldate" title="ApprovalDate">ApprovalDate</a>: <i>String</i>
    <a href="#memo" title="Memo">Memo</a>: <i><a href="memo.md">Memo</a></i>
    <a href="#secondcopyofmemo" title="SecondCopyOfMemo">SecondCopyOfMemo</a>: <i><a href="memo.md">Memo</a></i>
    <a href="#testcode" title="TestCode">TestCode</a>: <i>String</i>
    <a href="#authors" title="Authors">Authors</a>: <i>
      - String</i>
=======
    <a href="#capabilities" title="Capabilities">Capabilities</a>: <i>
      - String</i>
    <a href="#email" title="Email">Email</a>: <i>String</i>
    <a href="#name" title="Name">Name</a>: <i>String</i>
    <a href="#phone" title="Phone">Phone</a>: <i>String</i>
    <a href="#profileid" title="ProfileId">ProfileId</a>: <i>String</i>
>>>>>>> Stashed changes
    <a href="#tags" title="Tags">Tags</a>: <i>
      - <a href="tag.md">Tag</a></i>
</pre>

## Properties

<<<<<<< Updated upstream
#### Title

The title of the TPS report is a mandatory element.

_Required_: Yes

_Type_: String

_Minimum Length_: <code>20</code>

_Maximum Length_: <code>250</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### CoverSheetIncluded

Required for all TPS Reports submitted after 2/19/1999

_Required_: No

_Type_: Boolean

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### DueDate

_Required_: No

_Type_: String

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### ApprovalDate

_Required_: No

_Type_: String

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Memo

_Required_: No

_Type_: <a href="memo.md">Memo</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### SecondCopyOfMemo

_Required_: No

_Type_: <a href="memo.md">Memo</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### TestCode

_Required_: Yes

_Type_: String

_Allowed Values_: <code>NOT_STARTED</code> | <code>CANCELLED</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Authors
=======
#### Capabilities
>>>>>>> Stashed changes

_Required_: No

_Type_: List of String

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

<<<<<<< Updated upstream
#### Tags

An array of key-value pairs to apply to this resource.
=======
#### Email

_Required_: Yes

_Type_: String

_Minimum Length_: <code>5</code>

_Maximum Length_: <code>254</code>

_Pattern_: <code>^[\w\.\-]+@[\w\.\-]+$</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### Name

_Required_: Yes

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>254</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Phone

_Required_: No

_Type_: String

_Minimum Length_: <code>7</code>

_Maximum Length_: <code>22</code>

_Pattern_: <code>^\+?([0-9 \t\-()\/]{7,})(?:\s*(?:#|x\.?|ext\.?|extension) \t*(\d+))?$</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### ProfileId

_Required_: Yes

_Type_: String

_Minimum Length_: <code>1</code>

_Maximum Length_: <code>64</code>

_Pattern_: <code>^[a-zA-Z0-9_-]+$</code>

_Update requires_: [Replacement](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-replacement)

#### Tags
>>>>>>> Stashed changes

_Required_: No

_Type_: List of <a href="tag.md">Tag</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Ref

<<<<<<< Updated upstream
When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the TPSCode.
=======
When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the PartnershipId.
>>>>>>> Stashed changes

### Fn::GetAtt

The `Fn::GetAtt` intrinsic function returns a value for a specified attribute of this type. The following are the available attributes and sample return values.

For more information about using the `Fn::GetAtt` intrinsic function, see [Fn::GetAtt](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-getatt.html).

<<<<<<< Updated upstream
#### TPSCode

A TPS Code is automatically generated on creation and assigned as the unique identifier.
=======
#### CreatedAt

Returns the <code>CreatedAt</code> value.

#### ModifiedAt

Returns the <code>ModifiedAt</code> value.

#### PartnershipArn

Returns the <code>PartnershipArn</code> value.

#### PartnershipId

Returns the <code>PartnershipId</code> value.

#### TradingPartnerId

Returns the <code>TradingPartnerId</code> value.
>>>>>>> Stashed changes

