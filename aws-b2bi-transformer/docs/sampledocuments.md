# AWS::B2BI::Transformer SampleDocuments

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#bucketname" title="BucketName">BucketName</a>" : <i>String</i>,
    "<a href="#keys" title="Keys">Keys</a>" : <i>[ <a href="sampledocumentkeys.md">SampleDocumentKeys</a>, ... ]</i>
}
</pre>

### YAML

<pre>
<a href="#bucketname" title="BucketName">BucketName</a>: <i>String</i>
<a href="#keys" title="Keys">Keys</a>: <i>
      - <a href="sampledocumentkeys.md">SampleDocumentKeys</a></i>
</pre>

## Properties

#### BucketName

_Required_: Yes

_Type_: String

_Minimum Length_: <code>3</code>

_Maximum Length_: <code>63</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Keys

_Required_: Yes

_Type_: List of <a href="sampledocumentkeys.md">SampleDocumentKeys</a>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

