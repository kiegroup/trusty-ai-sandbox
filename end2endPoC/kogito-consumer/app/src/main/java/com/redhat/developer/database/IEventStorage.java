package com.redhat.developer.database;

import java.util.List;

import com.redhat.developer.decision.dto.DMNEvent;
import com.redhat.developer.decision.dto.DMNResult;

public interface IEventStorage {
    boolean storeEvent(String key, DMNEvent event);

    DMNEvent getEvent(String key);

    List<DMNResult> getDecisions();
}
