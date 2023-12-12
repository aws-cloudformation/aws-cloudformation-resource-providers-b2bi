# AWS::B2BI::Partnership

Definition of AWS::B2BI::Partnership Resource Type

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "Type" : "AWS::B2BI::Partnership",
    "Properties" : {
        "<a href="#capabilities" title="Capabilities">Capabilities</a>" : <i>[ String, ... ]</i>,
        "<a href="#email" title="Email">Email</a>" : <i>String</i>,
        "<a href="#name" title="Name">Name</a>" : <i>String</i>,
        "<a href="#phone" title="Phone">Phone</a>" : <i>String</i>,
        "<a href="#profileid" title="ProfileId">ProfileId</a>" : <i>String</i>,
        "<a href="#tags" title="Tags">Tags</a>" : <i>[ <a href="tag.md">Tag</a>, ... ]</i>,
    }
}
</pre>

### YAML

<pre>
Type: AWS::B2BI::Partnership
Properties:
    <a href="#capabilities" title="Capabilities">Capabilities</a>: <i>
      - String</i>
    <a href="#email" title="Email">Email</a>: <i>String</i>
    <a href="#name" title="Name">Name</a>: <i>String</i>
    <a href="#phone" title="Phone">Phone</a>: <i>String</i>
    <a href="#profileid" title="ProfileId">ProfileId</a>: <i>String</i>
    <a href="#tags" title="Tags">Tags</a>: <i>
      - <a href="tag.md">Tag</a></i>
</pre>

## Properties

#### Capabilities

_Required_: No

_Type_: List of String

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

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

_Required_: No

_Type_: List of <a href="tag.md">Tag</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

## Return Values

### Ref

When you pass the logical ID of this resource to the intrinsic `Ref` function, Ref returns the PartnershipId.

### Fn::GetAtt

The `Fn::GetAtt` intrinsic function returns a value for a specified attribute of this type. The following are the available attributes and sample return values.

For more information about using the `Fn::GetAtt` intrinsic function, see [Fn::GetAtt](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/intrinsic-function-reference-getatt.html).

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

