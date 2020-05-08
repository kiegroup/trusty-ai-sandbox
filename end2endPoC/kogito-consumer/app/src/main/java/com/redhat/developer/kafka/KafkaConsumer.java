package com.redhat.developer.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.developer.execution.IExecutionService;
import com.redhat.developer.execution.models.DMNEventModel;
import com.redhat.developer.execution.models.ModelFactory;
import com.redhat.developer.explainability.IExplainabilityService;
import com.redhat.developer.kafka.messaging.dto.DMNEventDto;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaConsumer extends KafkaAbstractConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Inject
    IExecutionService executionService;

    @Inject
    IExplainabilityService explainabilityService;

    @Override
    @Incoming("kogito-tracing")
    public void onProcessInstanceEvent(DMNEventDto event) {
        super.onProcessInstanceEvent(event);
    }

    protected void processEvent(DMNEventDto event) {
        LOGGER.debug("Processing a new event");
        try{
            DMNEventModel dmnEventModel = ModelFactory.fromKafkaCloudEvent(event);
            executionService.storeEvent(dmnEventModel.id, dmnEventModel.data.result);
            explainabilityService.processExecution(dmnEventModel.data.result);
        }
        catch (Exception e){
            LOGGER.warn("error", e);
        }
    }
}
