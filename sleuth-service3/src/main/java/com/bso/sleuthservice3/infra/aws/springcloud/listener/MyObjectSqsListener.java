package com.bso.sleuthservice3.infra.aws.springcloud.listener;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import com.bso.sleuthservice3.model.MyObject;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

@SpringCloudComponent
@Slf4j
public class MyObjectSqsListener {
    private final Tracer tracer;
    private final TraceContext.Extractor<MessageHeaders> extractor;

    public MyObjectSqsListener(Tracer tracer, Tracing tracing) {
        this.tracer = tracer;
        extractor = tracing.propagation().extractor((request, key) -> request.get(key, String.class));
    }

    @SqsListener("${app.queues.my-queue}")
    public void listen(Message<MyObject> message) {
        TraceContextOrSamplingFlags ctx = extractor.extract(message.getHeaders());
        Span span = tracer.nextSpan(ctx);
        try (var ignored = tracer.withSpanInScope(span)) {
            log.info("Message received: {}", message.getPayload());
        }
    }
}
