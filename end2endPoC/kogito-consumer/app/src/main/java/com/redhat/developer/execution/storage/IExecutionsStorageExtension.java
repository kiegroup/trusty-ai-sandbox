package com.redhat.developer.execution.storage;

import java.util.List;

import com.redhat.developer.execution.models.DMNResultModel;

public interface IExecutionsStorageExtension {

    boolean storeEvent(String key, DMNResultModel event);

    List<DMNResultModel> getEventsByMatchingId(String key);

    List<DMNResultModel> getDecisions(Long from, Long to, String prefix);
}
