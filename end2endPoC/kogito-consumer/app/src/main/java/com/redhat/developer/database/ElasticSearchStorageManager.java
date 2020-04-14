package com.redhat.developer.database;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.redhat.developer.database.model.ElasticSearchResponse;
import com.redhat.developer.database.model.Hit;
import com.redhat.developer.database.utils.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ElasticSearchStorageManager implements IStorageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchStorageManager.class);

        private static final String HOST = "http://elasticsearch:9200/";
//    private static final String HOST = "http://localhost:9200/";

    private static ObjectMapper objectMapper;
    private HttpHelper httpHelper;

    @PostConstruct
    void setUp() {
        httpHelper = new HttpHelper(HOST);
        objectMapper = new ObjectMapper();
    }

    public String create(String key, String request, String index) {
        String response = httpHelper.doPost(index + "/_doc/" + key, request);
        return response;
    }

    public <T> List<T> search(String request, String index, Class<T> type) {
        LOGGER.info("ES query " + request);
        String response = httpHelper.doPost(index + "/_search", request);
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(ElasticSearchResponse.class, type);
        LOGGER.info("ES returned " + response);
        try {
            // TODO: check performance issue with generics
            List<Hit<T>> hits = ((ElasticSearchResponse) objectMapper.readValue(response, javaType)).hits.hits;
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