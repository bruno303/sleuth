package com.bso.sleuthservice3.infra.aws.sdk.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

@AwsSdkComponent
@RequiredArgsConstructor
@Slf4j
public class SqsListenersConfiguration {
    private final List<AbstractSqsListener<?>> listeners;

    @EventListener(ApplicationReadyEvent.class)
    public void initListeners() {
        log.info("Starting listeners");
        listeners.forEach(AbstractSqsListener::listen);
    }

}
