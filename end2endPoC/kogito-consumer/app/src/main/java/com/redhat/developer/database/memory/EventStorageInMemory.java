package com.redhat.developer.database.memory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.decision.dto.DMNEvent;
import com.redhat.developer.decision.dto.DMNResult;

//@ApplicationScoped
public class EventStorageInMemory implements IEventStorage {

    public ConcurrentHashMap<String, DMNEvent> database;

    @PostConstruct
    void setUp(){
        database = new ConcurrentHashMap<String, DMNEvent>();
    }

    public boolean storeEvent(String key, DMNEvent event){
        if (!database.containsKey(key)){
            database.put(key, event);
            return true;
        }
        return false;
    }

    public DMNEvent getEvent(String key){
        if (database.containsKey(key)){
            return database.get(key);
        }
        return null;
    }

    public List<DMNResult> getDecisions(){
        return database.values().stream().map(x -> x.data.result).collect(Collectors.toList());
    }
}
