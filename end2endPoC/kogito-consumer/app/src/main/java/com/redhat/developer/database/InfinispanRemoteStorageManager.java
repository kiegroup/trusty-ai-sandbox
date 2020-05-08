package com.redhat.developer.database;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.redhat.developer.database.infinispan.InfinispanQueryFactory;
import com.redhat.developer.database.infinispan.TrustyCacheDefaultConfig;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@ApplicationScoped
public class InfinispanRemoteStorageManager implements IStorageManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanRemoteStorageManager.class);

    @Inject
    RemoteCacheManager manager;

    @PostConstruct
    void setup() {
        manager.start();
    }

    @Override
    public <T> boolean create(String key, T request, String index) {
        RemoteCache<String, T> remoteCache = manager.administration().getOrCreateCache(index, new TrustyCacheDefaultConfig(index));

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

        Query q = queryFactory.create(qq);

        return q.list();
    }

    @Override
    public boolean deleteIndex(String index) {
        return false;
    }
}
