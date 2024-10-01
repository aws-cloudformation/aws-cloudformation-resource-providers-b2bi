# AWS::B2BI::Transformer Mapping

## Syntax

To declare this entity in your AWS CloudFormation template, use the following syntax:

### JSON

<pre>
{
    "<a href="#templatelanguage" title="TemplateLanguage">TemplateLanguage</a>" : <i>String</i>,
    "<a href="#template" title="Template">Template</a>" : <i>String</i>
}
</pre>

### YAML

<pre>
<a href="#templatelanguage" title="TemplateLanguage">TemplateLanguage</a>: <i>String</i>
<a href="#template" title="Template">Template</a>: <i>String</i>
</pre>

## Properties

#### TemplateLanguage

_Required_: Yes

_Type_: String

_Allowed Values_: <code>XSLT</code> | <code>JSONATA</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Template

_Required_: No

_Type_: String

_Maximum Length_: <code>350000</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

