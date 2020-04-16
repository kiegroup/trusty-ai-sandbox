package com.redhat.developer.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.redhat.developer.database.infinispan.InfinispanQueryFactory;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class InfinispanStorageManager implements IStorageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanStorageManager.class);
    private DefaultCacheManager cacheManager;
    private Map<String, Cache> indexes = new HashMap<>();

    @PostConstruct
    void setup() {
        cacheManager = new DefaultCacheManager();
        cacheManager.defineConfiguration("local", new ConfigurationBuilder().build());
    }

    @Override
    public <T> String create(String key, T request, String index) {
        if (!indexes.containsKey(index)) {
            Cache<String, T> ciao = cacheManager.getCache("local");
            indexes.put(index, ciao);
        }

        Cache cache = indexes.get(index);
        cache.put(key, request);

        return null;
    }

    @Override
    public <T> List<T> search(TrustyStorageQuery query, String index, Class<T> type) {

        QueryFactory queryFactory = Search.getQueryFactory(indexes.get(index));

        String qq = InfinispanQueryFactory.build(query, type.getCanonicalName());

        LOGGER.info("Final string " + qq);

        Query q = queryFactory.create(qq);

        return q.list();
    }
}
