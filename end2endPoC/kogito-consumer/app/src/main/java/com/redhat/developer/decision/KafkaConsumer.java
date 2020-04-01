package com.redhat.developer.decision;

import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.developer.decision.dto.DMNEvent;
import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.database.elastic.utils.HttpHelper;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    @Inject
    IEventStorage eventStorage;

    @Incoming("kogito-tracing")
    public void onProcessInstanceEvent(DMNEvent event) {
        LOGGER.info("Hey i've got this message: " + event.data.toString());
        CompletableFuture.runAsync(() -> {
            processEvent(event);
        });
    }

    private void processEvent(DMNEvent event){
        eventStorage.storeEvent(event.id, event);
    }
}
