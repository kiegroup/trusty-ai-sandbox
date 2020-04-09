package com.redhat.developer.database.elastic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.database.elastic.decisions.ElasticSearchResponse;
import com.redhat.developer.database.elastic.decisions.Hit;
import com.redhat.developer.database.elastic.models.ElasticSearchModelResponse;
import com.redhat.developer.database.elastic.models.ModelHit;
import com.redhat.developer.database.elastic.utils.HttpHelper;
import com.redhat.developer.dmn.storage.dto.DmnModel;
import com.redhat.developer.execution.storage.model.DMNEventModel;
import com.redhat.developer.execution.storage.model.DMNResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ElasticStorageManager implements IStorageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    private static final String HOST = "http://elasticsearch:9200/"; //"http://localhost:9200/"
    private static final String INDEX = "dmneventdata";
    private static final String MODELINDEX = "dmnmodeldata";

    private static ObjectMapper objectMapper;
    private HttpHelper httpHelper;

    @PostConstruct
    void setUp() {
        httpHelper = new HttpHelper(HOST);
        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean storeEvent(String key, DMNEventModel event) {
        String request = null;
        try {
            request = objectMapper.writeValueAsString(event);
            LOGGER.info("going to store: " + request);
            httpHelper.doPost(INDEX + "/_doc/" + event.id, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Something went wrong.");
        }
        LOGGER.info("New event stored.");
        return true;
    }

    @Override
    public List<DMNEventModel> getEventsByMatchingId(String key) {
        LOGGER.info("requested: " + key);
        String request = "{ \n" +
                "    \"query\": {\n" +
                "        \"match_phrase_prefix\": { \"data.result.executionId\" : \"" + key + "\"}\n" +
                "    }\n" +
                "}\n";
        LOGGER.info("Going to query ES with " + request);
        String response = httpHelper.doPost(INDEX + "/_search", request);
        LOGGER.info("ES returned " + response);
        try {
            List<Hit> hits = objectMapper.readValue(response, ElasticSearchResponse.class).hits.hits;
            if (hits.size() == 0) {
                return null;
            }
            return hits.stream().map(x -> x.source).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DMNResultModel> getDecisions(String from, String to, String prefix) {
        String queryId = prefix.equals("") ? "" : "{\"match_phrase_prefix\": { \"data.result.executionId\" : \"" + prefix + "\"} }, ";

        String request = String.format(
                "{\"size\": 10000, \"query\" : { \"bool\": {\n" +
                        "          \"must\": [" + queryId +
                        "{\"range\" : {\"data.result.executionDate\" : {\"gte\" : \"%s\", \"lte\": \"%s\"}}}" +
                        " ] } } }", from, to);

        LOGGER.info(request);
        String response = httpHelper.doPost(INDEX + "/_search", request);
        LOGGER.info(response);
        try {
            List<Hit> hits = objectMapper.readValue(response, ElasticSearchResponse.class).hits.hits;

            if (hits.size() == 0) {
                return new ArrayList<>();
            }

            return hits.stream().map(x -> x.source.data.result).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DmnModel getModel(String nameSpace) {
        String request = String.format(
                "{\"query\" : {\"match\" : {\"modelId\" : \"%s\"}}}",
                nameSpace);

        String response = httpHelper.doPost(MODELINDEX + "/_search", request);
        LOGGER.info("GOT: " + response);
        try {
            List<ModelHit> hits = objectMapper.readValue(response, ElasticSearchModelResponse.class).hits.hits;

            if (hits.size() == 0) {
                return null;
            }

            return hits.get(0).source;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean storeModel(DmnModel model) {
        String request = null;
        try {
            request = objectMapper.writeValueAsString(model);
            LOGGER.info("going to store: " + request);
            httpHelper.doPost(MODELINDEX + "/_doc/" + model.modelId, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Something went wrong.");
        }
        LOGGER.info("New model stored.");
        return true;
    }

    @Override
    public List<DmnModel> getModelIds() {
        String request = "{ \"_source\" : {\"exclude\": [\"model\"]}, \"query\" : {\"match_all\" : {}}}";

        String response = httpHelper.doPost(MODELINDEX + "/_search", request);

        try {
            List<ModelHit> hits = objectMapper.readValue(response, ElasticSearchModelResponse.class).hits.hits;

            if (hits.size() == 0) {
                return null;
            }

            return hits.stream().map(x -> x.source).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
