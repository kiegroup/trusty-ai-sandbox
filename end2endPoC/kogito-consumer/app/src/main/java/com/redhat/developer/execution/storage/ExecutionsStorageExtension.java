package com.redhat.developer.execution.storage;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.database.TrustyStorageQuery;
import com.redhat.developer.database.operators.DateOperator;
import com.redhat.developer.database.operators.StringOperator;
import com.redhat.developer.execution.models.DMNResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: check application scoped? Request scoped?
@ApplicationScoped
public class ExecutionsStorageExtension implements IExecutionsStorageExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionsStorageExtension.class);
    private static final String INDEX = "dmneventdata";

    private static ObjectMapper objectMapper;

    @Inject
    IStorageManager storageManager;

    @PostConstruct
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean storeEvent(String key, DMNResultModel event) {
        LOGGER.info("going to store new dmn event model " + event.executionId);
        storageManager.create(key, event, INDEX);
        LOGGER.info("going to store new dmn event model - STORED.");
        return true;
    }

    @Override
    public List<DMNResultModel> getEventsByMatchingId(String key) {
        TrustyStorageQuery query = new TrustyStorageQuery().where("executionId", StringOperator.PREFIX, key);
        return storageManager.search(query, INDEX, DMNResultModel.class);
    }

    @Override
    public List<DMNResultModel> getDecisions(String from, String to, String prefix) {
        TrustyStorageQuery query = new TrustyStorageQuery();
        if (!prefix.equals("")) {
            query.where("executionId", StringOperator.PREFIX, prefix);
        }
        query.where("executionDate", DateOperator.GTE, from).where("executionDate", DateOperator.LTE, to);

        return storageManager.search(query, INDEX, DMNResultModel.class);
    }
}