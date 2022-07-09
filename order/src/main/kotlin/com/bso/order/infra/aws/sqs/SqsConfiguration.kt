package com.bso.order.infra.aws.sqs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.regions.providers.AwsRegionProvider
import software.amazon.awssdk.regions.providers.AwsRegionProviderChain
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
class SqsConfiguration(
    @Value("\${application.aws.endpoint}")
    private val awsUrl: String
) {

    @Bean
    fun sqsClient(): SqsClient {
        return SqsClient
            .builder()
            .endpointOverride(URI(awsUrl))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .region(CustomAwsRegionProviderChain(
                EnvironmentVariableRegionProvider(),
                DefaultAwsRegionProviderChain()
            ).region)
            .build()
    }

    inner class CustomAwsRegionProviderChain(vararg providers: AwsRegionProvider) : AwsRegionProviderChain(*providers)

    inner class EnvironmentVariableRegionProvider : AwsRegionProvider {
        override fun getRegion(): Region? {
            return System.getenv("AWS_REGION")?.let { region ->
                Region.of(region)
            }
        }

    }
}
