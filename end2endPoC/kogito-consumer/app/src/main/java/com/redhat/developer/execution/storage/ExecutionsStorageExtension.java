package com.redhat.developer.execution.storage;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.execution.models.DMNEventModel;
import com.redhat.developer.execution.models.DMNResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: check application scoped? Request scoped?
@ApplicationScoped
public class ExecutionsStorageExtension implements IExecutionsStorageExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionsStorageExtension.class);
    private static final String INDEX = "dmneventdata";
    private static final String MODELINDEX = "dmnmodeldata";

    private static ObjectMapper objectMapper;

    @Inject
    IStorageManager storageManager;

    @PostConstruct
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean storeEvent(String key, DMNResultModel event) {
        try {
            LOGGER.info("going to store new dmn event model");
            storageManager.create(key, objectMapper.writeValueAsString(event), INDEX);
            LOGGER.info("going to store new dmn event model - STORED.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<DMNResultModel> getEventsByMatchingId(String key) {
        LOGGER.info("requested: " + key);
        String request = "{ \n" +
                "    \"query\": {\n" +
                "        \"match_phrase_prefix\": { \"executionId\" : \"" + key + "\"}\n" +
                "    }\n" +
                "}\n";
        LOGGER.info("Going to query ES with " + request);
        return storageManager.search(request, INDEX, DMNResultModel.class);
    }

    @Override
    public List<DMNResultModel> getDecisions(String from, String to, String prefix) {
        String queryId = prefix.equals("") ? "" : "{\"match_phrase_prefix\": { \"executionId\" : \"" + prefix + "\"} }, ";

        String request = String.format(
                "{\"size\": 10000, \"query\" : { \"bool\": {\n" +
                        "          \"must\": [" + queryId +
                        "{\"range\" : {\"executionDate\" : {\"gte\" : \"%s\", \"lte\": \"%s\"}}}" +
                        " ] } } }", from, to);
        return storageManager.search(request, INDEX, DMNResultModel.class);
    }
}