package com.bso.sleuthservice3.infra.aws.springcloud.listener;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.handlers.RequestHandler2;

@SpringCloudComponent
public class TracingRequestHandler extends RequestHandler2 {
    private final Tracer tracer;
    TraceContext.Injector<AmazonWebServiceRequest> injector;

    public TracingRequestHandler(Tracer tracer, Tracing tracing) {
        this.tracer = tracer;
        injector = tracing.propagation().injector(AmazonWebServiceRequest::putCustomRequestHeader);
    }

    @Override
    public AmazonWebServiceRequest beforeExecution(AmazonWebServiceRequest request) {
        var span = tracer.nextSpan().start();
        injector.inject(span.context(), request);
        return super.beforeExecution(request);
    }
}