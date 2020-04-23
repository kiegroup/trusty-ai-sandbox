package com.redhat.developer.database;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.developer.database.infinispan.InfinispanQueryFactory;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.commons.configuration.XMLStringConfiguration;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@ApplicationScoped
// does not work at all
public class InfinispanRemoteStorageManager implements IStorageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanRemoteStorageManager.class);
    // private Map<String, RemoteCache> indexes = new HashMap<>();

    private static final String CACHE_CONFIG =
            "<infinispan><cache-container>" +
                    "<distributed-cache name=\"%s\"></distributed-cache>" +
                    "</cache-container></infinispan>";

    @Inject
    RemoteCacheManager manager;

    @PostConstruct
    void setup() {
        manager.start();
    }

    @Override
    public <T> boolean create(String key, T request, String index) {
//        if (!indexes.containsKey(index)) {
        XMLStringConfiguration config = new XMLStringConfiguration(String.format(CACHE_CONFIG, index));
        RemoteCache<String, T> remoteCache = manager.administration().getOrCreateCache(index, config);

        LOGGER.info(remoteCache.getDataFormat().getValueType().getClassType());
        LOGGER.info("put element in cache");
        remoteCache.put(key, request);
        LOGGER.info(remoteCache.getDataFormat().getValueType().getClassType());

        return true;
    }

    @Override
    public <T> List<T> search(TrustyStorageQuery query, String index, Class<T> type) {

        QueryFactory queryFactory = Search.getQueryFactory(manager.getCache(index));

        String qq = InfinispanQueryFactory.build(query, type.getName());

        LOGGER.info("Final string " + qq);

        qq = "from DMNResultModel";

        Query q = queryFactory.create(qq);

        return q.list();
    }

    @Override
    public boolean deleteIndex(String index) {
        return false;
    }
}
