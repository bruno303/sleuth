package com.bso.sleuthservice3.infra.aws.sdk.listener;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
public abstract class AbstractSqsListener<T> {
    private final Tracer tracer;
    private final SqsAsyncClient sqsAsyncClient;
    private final TraceContext.Extractor<Message> extractor;
    private final Executor loopExecutor = Executors.newSingleThreadExecutor();
    private final ObjectMapper objectMapper;
    protected final String queue;

    protected AbstractSqsListener(
            Tracing tracing, Tracer tracer, SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper, String queue
    ) {
        this.tracer = tracer;
        this.sqsAsyncClient = sqsAsyncClient;
        this.objectMapper = objectMapper;
        extractor = tracing.propagation().extractor((request, key) -> request.messageAttributes().get(key).stringValue());
        this.queue = queue;
    }

    public void listen() {
        CompletableFuture.runAsync(this::doListen, loopExecutor);
    }

    protected abstract void processMessage(T message);
    protected abstract Class<T> getClazz();

    private void doListen() {
        while(true) {
            try {
                ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queue)
                        .maxNumberOfMessages(5)
                        .messageAttributeNames("All")
                        .build();
                sqsAsyncClient.receiveMessage(receiveMessageRequest)
                        .thenAcceptAsync(response -> response.messages().forEach(this::processMessageWithTrace));
            } catch (SqsException e) {
                log.error(e.awsErrorDetails().errorMessage());
            }
            sleep();
        }
    }

    private void processMessageWithTrace(Message message) {
        var span = tracer.nextSpan(extractor.extract(message));
        try (var ignored = tracer.withSpanInScope(span)) {
            final T objectReceived = fromJson(message.body());
            processMessage(objectReceived);
            deleteMessage(message);
        }
    }

    private void deleteMessage(Message message) {
        sleep();
        var request = DeleteMessageRequest.builder()
                .queueUrl(queue)
                .receiptHandle(message.receiptHandle())
                .build();
        sqsAsyncClient.deleteMessage(request);
    }

    @SneakyThrows
    private T fromJson(String json) {
        return objectMapper.readValue(json, getClazz());
    }

    @SneakyThrows
    private void sleep() {
        Thread.sleep(1000);
    }
}
