package com.redhat.developer.kafka;

import java.util.concurrent.CompletableFuture;

import com.redhat.developer.kafka.messaging.dto.DMNEventDto;

public abstract class KafkaAbstractConsumer {

    protected void onProcessInstanceEvent(DMNEventDto event) {
        CompletableFuture.runAsync(() -> {
            processEvent(event);
        });
    }

    protected abstract void processEvent(DMNEventDto event);
}
