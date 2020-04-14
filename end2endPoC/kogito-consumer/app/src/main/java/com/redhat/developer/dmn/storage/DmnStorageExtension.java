package com.redhat.developer.dmn.storage;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.dmn.models.DmnModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: check application scoped? Request scoped?
@ApplicationScoped
public class DmnStorageExtension implements IDmnStorageExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(DmnStorageExtension.class);
    private static final String MODELINDEX = "dmnmodeldata";

    private static ObjectMapper objectMapper;

    @Inject
    IStorageManager storageManager;

    @PostConstruct
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public DmnModel getModel(String nameSpace) {
        String request = String.format(
                "{\"query\" : {\"match\" : {\"modelId\" : \"%s\"}}}",
                nameSpace);
        LOGGER.info("Going to query ES with " + request);
        return storageManager.search(request, MODELINDEX, DmnModel.class).get(0);
    }

    @Override
    public boolean storeModel(DmnModel model) {
        try {
            LOGGER.info("going to store new dmn model");
            storageManager.create(model.modelId, objectMapper.writeValueAsString(model), MODELINDEX);
            LOGGER.info("going to store new dmn model - STORED.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<DmnModel> getModelIds() {
        String request = "{ \"_source\" : {\"exclude\": [\"model\"]}, \"query\" : {\"match_all\" : {}}}";
        LOGGER.info("Going to query ES with " + request);
        return storageManager.search(request, MODELINDEX, DmnModel.class);
    }
}