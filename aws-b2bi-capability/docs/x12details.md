# AWS::B2BI::Capability X12Details

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

_Allowed Values_: <code>X12_110</code> | <code>X12_180</code> | <code>X12_204</code> | <code>X12_210</code> | <code>X12_211</code> | <code>X12_214</code> | <code>X12_215</code> | <code>X12_259</code> | <code>X12_260</code> | <code>X12_266</code> | <code>X12_269</code> | <code>X12_270</code> | <code>X12_271</code> | <code>X12_274</code> | <code>X12_275</code> | <code>X12_276</code> | <code>X12_277</code> | <code>X12_278</code> | <code>X12_310</code> | <code>X12_315</code> | <code>X12_322</code> | <code>X12_404</code> | <code>X12_410</code> | <code>X12_417</code> | <code>X12_421</code> | <code>X12_426</code> | <code>X12_810</code> | <code>X12_820</code> | <code>X12_824</code> | <code>X12_830</code> | <code>X12_832</code> | <code>X12_834</code> | <code>X12_835</code> | <code>X12_837</code> | <code>X12_844</code> | <code>X12_846</code> | <code>X12_849</code> | <code>X12_850</code> | <code>X12_852</code> | <code>X12_855</code> | <code>X12_856</code> | <code>X12_860</code> | <code>X12_861</code> | <code>X12_864</code> | <code>X12_865</code> | <code>X12_869</code> | <code>X12_870</code> | <code>X12_940</code> | <code>X12_945</code> | <code>X12_990</code> | <code>X12_997</code> | <code>X12_999</code> | <code>X12_270_X279</code> | <code>X12_271_X279</code> | <code>X12_275_X210</code> | <code>X12_275_X211</code> | <code>X12_276_X212</code> | <code>X12_277_X212</code> | <code>X12_277_X214</code> | <code>X12_277_X364</code> | <code>X12_278_X217</code> | <code>X12_820_X218</code> | <code>X12_820_X306</code> | <code>X12_824_X186</code> | <code>X12_834_X220</code> | <code>X12_834_X307</code> | <code>X12_834_X318</code> | <code>X12_835_X221</code> | <code>X12_837_X222</code> | <code>X12_837_X223</code> | <code>X12_837_X224</code> | <code>X12_837_X291</code> | <code>X12_837_X292</code> | <code>X12_837_X298</code> | <code>X12_999_X231</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

#### Version

_Required_: No

_Type_: String

_Allowed Values_: <code>VERSION_4010</code> | <code>VERSION_4030</code> | <code>VERSION_5010</code> | <code>VERSION_5010_HIPAA</code>

_Update requires_: [No interruption](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/using-cfn-updating-stacks-update-behaviors.html#update-no-interrupt)

