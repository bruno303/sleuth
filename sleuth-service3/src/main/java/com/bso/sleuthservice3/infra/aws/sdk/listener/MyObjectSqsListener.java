package com.bso.sleuthservice3.infra.aws.sdk.listener;

import brave.Tracer;
import brave.Tracing;
import com.bso.sleuthservice3.model.MyObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@AwsSdkComponent
@Slf4j
public class MyObjectSqsListener extends AbstractSqsListener<MyObject> {

    public MyObjectSqsListener(
            Tracing tracing, Tracer tracer, SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper,
            @Value("${app.queues.my-queue}") String queue
    ) {
        super(tracing, tracer, sqsAsyncClient, objectMapper, queue);
    }

    @Override
    protected void processMessage(MyObject message) {
        log.info("Message received: {}", message);
    }

    @Override
    protected Class<MyObject> getClazz() {
        return MyObject.class;
    }
}
