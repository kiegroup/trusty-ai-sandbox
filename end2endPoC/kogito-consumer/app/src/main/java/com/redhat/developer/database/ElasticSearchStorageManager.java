package com.redhat.developer.database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.redhat.developer.database.elastic.ElasticQueryFactory;
import com.redhat.developer.database.elastic.model.ElasticSearchResponse;
import com.redhat.developer.database.elastic.model.Hit;
import com.redhat.developer.database.elastic.utils.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@ApplicationScoped

// TODO: ES does not work anymore becuase outcomeResult is a dynamic object -> the index must be configured properly!!!
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

    @Override
    public <T> String create(String key, T request, String index) {
        try {
            return httpHelper.doPost(index + "/_doc/" + key, objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> search(TrustyStorageQuery query, String index, Class<T> type) {
        String esQuery = ElasticQueryFactory.build(query);
        LOGGER.info("ES query " + esQuery);
        String response = httpHelper.doPost(index + "/_search", esQuery);
        JavaType javaType = TypeFactory.defaultInstance()
                .constructParametricType(ElasticSearchResponse.class, String.class);
        LOGGER.info("ES returned " + response);
        try {
            // TODO: check performance issue with generics
            List<Hit<T>> hits = ((ElasticSearchResponse) objectMapper.readValue(response, javaType)).hits.hits;
            if (hits.size() == 0) {
                return new ArrayList<>();
            }
            return hits.stream().map(x -> x.source).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}