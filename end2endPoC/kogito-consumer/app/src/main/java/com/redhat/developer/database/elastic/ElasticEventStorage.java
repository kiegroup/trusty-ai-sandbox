package com.redhat.developer.database.elastic;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.decision.dto.DMNEvent;
import com.redhat.developer.database.elastic.utils.HttpHelper;
import com.redhat.developer.decision.dto.DMNResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ElasticEventStorage implements IEventStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    private static final String HOST = "http://elasticsearch:9200/";
    private static ObjectMapper objectMapper;

    private static final String INDEX = "dmneventdata";

    private HttpHelper httpHelper;

    @PostConstruct
    void setUp(){
        httpHelper = new HttpHelper(HOST);
        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean storeEvent(String key, DMNEvent event){
        String request = null;
        try {
            request = objectMapper.writeValueAsString(event);
            LOGGER.info("going to store: " + request);
            httpHelper.doPost(INDEX + "/_doc/" + event.id, request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        LOGGER.info("New event stored.");
        return true;
    }

    @Override
    public DMNEvent getEvent(String key) {
        System.out.println("requested: " + key);
        String request = "{ \n" +
                "    \"query\": {\n" +
                "        \"match\": { \"data.result.evaluationId\" : \"" + key + "\"}\n" +
                "    }\n" +
                "}\n";
        LOGGER.debug("Going to query ES with " + request);
        String response = httpHelper.doPost(INDEX + "/_search", request);
        LOGGER.debug("ES returned " + response);
        try {
            return (DMNEvent) objectMapper.readValue(response, ElasticSearchResponse.class).hits.hits.get(0).source;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DMNResult> getDecisions() {
        String request = "{\n" + "\"size\": 100," +
                "    \"query\": {\n" +
                "        \"match_all\": {}\n" +
                "    }\n" +
                "}\n";
        String response = httpHelper.doPost(INDEX + "/_search", request);
        try {
            return objectMapper.readValue(response, ElasticSearchResponse.class).hits.hits.stream().map(x -> x.source.data.result).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
