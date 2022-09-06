package com.bso.sleuthservice3.infra.aws.sdk.listener;

import com.bso.sleuthservice3.infra.aws.OnSpringCloudDisabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@OnSpringCloudDisabled
public class SqsConfiguration {

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(awsCredentialsProvider())
                .region(new DefaultAwsRegionProviderChain().getRegion())
                .build();
    }
}
