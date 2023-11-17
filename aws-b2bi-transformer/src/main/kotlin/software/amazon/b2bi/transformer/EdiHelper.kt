package software.amazon.b2bi.transformer

import software.amazon.b2bi.transformer.EdiType as ResourceEdi
import software.amazon.awssdk.services.b2bi.model.EdiType as SdkEdi
import software.amazon.b2bi.transformer.X12Details as ResourceX12
import software.amazon.awssdk.services.b2bi.model.X12Details as SdkX12

object EdiHelper {
    fun ResourceEdi.translateToSdkEdi(): SdkEdi {
        val x12 : SdkX12 = SdkX12.builder().transactionSet(this.x12Details.transactionSet).version(this.x12Details.version).build()
        return SdkEdi.builder().x12Details(x12).build()
    }

    fun SdkEdi.translateToResourceEdi(): ResourceEdi {
        val x12 : ResourceX12 = ResourceX12.builder().transactionSet(this.x12Details().transactionSetAsString()).version(this.x12Details().versionAsString()).build()
        return ResourceEdi.builder().x12Details(x12).build()
    }
}