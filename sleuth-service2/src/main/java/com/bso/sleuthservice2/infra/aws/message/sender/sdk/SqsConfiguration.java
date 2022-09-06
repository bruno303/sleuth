package com.bso.sleuthservice2.infra.aws.message.sender.sdk;

import com.bso.sleuthservice2.infra.aws.OnSpringCloudDisabled;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@OnSpringCloudDisabled
@Configuration
public class SqsConfiguration {

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }

    @Bean
    public SqsClient sqsAsyncClient() {
        return SqsClient.builder()
                .credentialsProvider(awsCredentialsProvider())
                .region(new DefaultAwsRegionProviderChain().getRegion())
                .build();
    }
}
