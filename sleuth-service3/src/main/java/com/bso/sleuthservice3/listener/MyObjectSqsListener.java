package com.bso.sleuthservice3.listener;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import com.bso.sleuthservice3.model.MyObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyObjectSqsListener {
    private final Tracing tracing;
    private final Tracer tracer;

    @SqsListener(value = "my-queue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void listen(String myObject, @Headers Map<String, String> headers) {
        var extractor = tracing.propagation().extractor(Map<String, String>::get);
        var span = tracer.nextSpan(extractor.extract(headers));
        try (var ws = tracer.withSpanInScope(span)) {
            log.info("Message received: {}", myObject);
            log.info("Span received: {}", span);
        }
    }
}
