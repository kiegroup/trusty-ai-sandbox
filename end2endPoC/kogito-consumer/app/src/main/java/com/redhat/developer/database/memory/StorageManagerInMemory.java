package com.redhat.developer.database.memory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.dmn.storage.dto.DmnModel;
import com.redhat.developer.execution.storage.model.DMNEventModel;
import com.redhat.developer.execution.storage.model.DMNResultModel;

//@ApplicationScoped
public class StorageManagerInMemory implements IStorageManager {

    public ConcurrentHashMap<String, DMNEventModel> database;

    @PostConstruct
    void setUp() {
        database = new ConcurrentHashMap<String, DMNEventModel>();
    }

    public boolean storeEvent(String key, DMNEventModel event) {
        if (!database.containsKey(key)) {
            database.put(key, event);
            return true;
        }
        return false;
    }

    public List<DMNEventModel> getEventsByMatchingId(String key) {
        if (database.containsKey(key)) {
            return null;//database.get(key);
        }
        return null;
    }

    @Override
    public List<DMNResultModel> getDecisions(String from, String to, String prefix) {
        return database.values().stream().map(x -> x.data.result).collect(Collectors.toList());
    }

    @Override
    public DmnModel getModel(String nameSpace) {
        return null;
    }

    @Override
    public boolean storeModel(DmnModel model) {
        return false;
    }

    @Override
    public List<DmnModel> getModelIds() {
        return null;
    }
}
