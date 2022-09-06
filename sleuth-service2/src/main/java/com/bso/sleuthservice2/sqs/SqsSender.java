package com.bso.sleuthservice2.sqs;

import brave.Tracer;
import brave.Tracing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SqsSender {
    private final QueueMessagingTemplate queueMessagingTemplate;
    private final Tracing tracing;
    private final Tracer tracer;

    public <T> void send(T message, String queue) {
        var injector = tracing.propagation().injector(Map<String, Object>::put);

        Map<String, Object> headers = new HashMap<>();
        injector.inject(tracer.currentSpan().context(), headers);
        queueMessagingTemplate.convertAndSend(queue, message, headers);
    }
}
