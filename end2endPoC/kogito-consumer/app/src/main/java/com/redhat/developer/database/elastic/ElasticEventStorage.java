package com.redhat.developer.database.elastic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.database.elastic.utils.HttpHelper;
import com.redhat.developer.execution.storage.model.DMNEventModel;
import com.redhat.developer.execution.storage.model.DMNResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ElasticEventStorage implements IEventStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    private static final String HOST = "http://elasticsearch:9200/";
    private static final String INDEX = "dmneventdata";
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
        System.out.println("requested: " + key);
        String request = "{ \n" +
                "    \"query\": {\n" +
                "        \"match\": { \"data.result.executionId\" : \"" + key + "*\"}\n" +
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
    public List<DMNResultModel> getDecisions(String from, String to) {
        String request = String.format(
                "{" +
                        "\"size\": 10000, " +
                        "\"query\" : {" +
                        "\"range\" : {" +
                        "\"data.result.executionDate\" : {" +
                        "\"gte\" : \"%s\", \"lte\": \"%s\" " +
                        "} " +
                        "}" +
                        "}" +
                        "}", from, to);

        String response = httpHelper.doPost(INDEX + "/_search", request);
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
}
