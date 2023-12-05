package software.amazon.b2bi.transformer

import software.amazon.awssdk.services.b2bi.B2BiClient
import software.amazon.cloudformation.LambdaWrapper

object ClientBuilder {
        fun getClient(): B2BiClient = B2BiClient.builder()
            .httpClient(LambdaWrapper.HTTP_CLIENT)
            .build()
}
