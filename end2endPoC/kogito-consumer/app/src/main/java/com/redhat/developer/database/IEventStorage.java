package com.redhat.developer.database;

import java.util.List;

import com.redhat.developer.decision.storage.model.DMNEventModel;
import com.redhat.developer.decision.storage.model.DMNResultModel;

public interface IEventStorage {
    boolean storeEvent(String key, DMNEventModel event);

    DMNEventModel getEvent(String key);

    List<DMNResultModel> getDecisions();
}
