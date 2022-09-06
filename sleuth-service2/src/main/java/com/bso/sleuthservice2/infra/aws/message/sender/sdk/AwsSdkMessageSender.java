package com.bso.sleuthservice2.infra.aws.message.sender.sdk;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import com.bso.sleuthservice2.infra.aws.message.sender.MessageSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@AwsSdkComponent
@Slf4j
public class AwsSdkMessageSender implements MessageSender {
    private final Tracer tracer;
    private final SqsClient sqsClient;
    private final TraceContext.Injector<SendMessageRequest.Builder> injector;
    private final ObjectMapper objectMapper;

    public AwsSdkMessageSender(Tracing tracing, Tracer tracer, SqsClient sqsClient, ObjectMapper objectMapper) {
        this.tracer = tracer;
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        Propagation.Setter<SendMessageRequest.Builder, String> setter = (request, key, value) -> {
            final var attribute = MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue(value)
                    .build();
            Map<String, MessageAttributeValue> attributes = new HashMap<>();
            attributes.put(key, attribute);
            request.messageAttributes(attributes);
        };
        this.injector = tracing.propagation().injector(setter);
    }

    @Override
    public <T> void send(T message, String queue) {
        log.info("Sending message: {}", message);
        var queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queue).build()).queueUrl();
        doSend(message, queueUrl);
    }

    private <T> void doSend(T message, String queueUrl) {
        var requestBuilder = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(toJson(message));

        log.info("Configuring trace in message: {}", message);
        injector.inject(tracer.currentSpan().context(), requestBuilder);
        sqsClient.sendMessage(requestBuilder.build());
    }

    @SneakyThrows
    private <T> String toJson(T message) {
        return objectMapper.writeValueAsString(message);
    }
}
