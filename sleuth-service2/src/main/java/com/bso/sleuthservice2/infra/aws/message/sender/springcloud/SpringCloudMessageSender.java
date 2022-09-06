package com.bso.sleuthservice2.infra.aws.message.sender.springcloud;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import com.bso.sleuthservice2.infra.aws.message.sender.MessageSender;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringCloudComponent
public class SpringCloudMessageSender implements MessageSender {
    private final QueueMessagingTemplate queueMessagingTemplate;
    private final Tracer tracer;
    private final TraceContext.Injector<Map<String, Object>> injector;

    public SpringCloudMessageSender(Tracing tracing, QueueMessagingTemplate queueMessagingTemplate, Tracer tracer) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.tracer = tracer;
        Propagation.Setter<Map<String, Object>, String> setter = (request, key, value) -> request.put(key, value);
        this.injector = tracing.propagation().injector(setter);
    }

    @Override
    public <T> void send(T message, String queue) {
        Map<String, Object> headers = new HashMap<>();
        injector.inject(tracer.currentSpan().context(), headers);
        queueMessagingTemplate.convertAndSend(queue, message, headers);
    }
}
