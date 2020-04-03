package com.redhat.developer.database.memory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.execution.storage.model.DMNEventModel;
import com.redhat.developer.execution.storage.model.DMNResultModel;

//@ApplicationScoped
public class EventStorageInMemory implements IEventStorage {

    public ConcurrentHashMap<String, DMNEventModel> database;

    @PostConstruct
    void setUp(){
        database = new ConcurrentHashMap<String, DMNEventModel>();
    }

    public boolean storeEvent(String key, DMNEventModel event){
        if (!database.containsKey(key)){
            database.put(key, event);
            return true;
        }
        return false;
    }

    public DMNEventModel getEvent(String key){
        if (database.containsKey(key)){
            return database.get(key);
        }
        return null;
    }

    @Override
    public List<DMNResultModel> getDecisions(String from, String to){
        return database.values().stream().map(x -> x.data.result).collect(Collectors.toList());
    }
}
