package com.redhat.developer.explainability.storage;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.database.TrustyStorageQuery;
import com.redhat.developer.database.operators.StringOperator;
import com.redhat.developer.explainability.model.Saliency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ExplanabilityStorageExtension implements IExplanabilityStorageExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExplanabilityStorageExtension.class);
    private static final String INDEX = "localexplanation";

    private static ObjectMapper objectMapper;

    @Inject
    IStorageManager storageManager;

    @PostConstruct
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean storeExplanation(String executionId, Saliency saliency) {
        LOGGER.info("going to store new explanation " + executionId);
        storageManager.create(executionId, saliency, INDEX);
        LOGGER.info("going to store new explanation - STORED.");
        return true;
    }

    @Override
    public Saliency getExplanation(String executionId) {
        TrustyStorageQuery query = new TrustyStorageQuery().where("executionId", StringOperator.PREFIX, executionId);
        return storageManager.search(query, INDEX, Saliency.class).get(0);
    }
}
