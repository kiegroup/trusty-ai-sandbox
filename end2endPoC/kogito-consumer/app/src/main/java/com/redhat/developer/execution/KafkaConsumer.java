package com.redhat.developer.execution;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.execution.storage.ModelFactory;
import com.redhat.developer.execution.storage.model.DMNEventModel;
import com.redhat.developer.kafka.KafkaAbstractConsumer;
import com.redhat.developer.kafka.messaging.dto.DMNEventDto;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaConsumer extends KafkaAbstractConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Inject
    IEventStorage eventStorage;

    @Override
    @Incoming("kogito-tracing")
    public void onProcessInstanceEvent(DMNEventDto event) {
        super.onProcessInstanceEvent(event);
    }

    protected void processEvent(DMNEventDto event){
        LOGGER.debug("Processing a new event");
        DMNEventModel dmnEventModel = ModelFactory.fromKafkaCloudEvent(event);
        eventStorage.storeEvent(dmnEventModel.id, dmnEventModel);
    }
}
