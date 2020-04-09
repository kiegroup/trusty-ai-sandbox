package com.redhat.developer.database;

import java.util.List;

import com.redhat.developer.dmn.storage.dto.DmnModel;
import com.redhat.developer.execution.storage.model.DMNEventModel;
import com.redhat.developer.execution.storage.model.DMNResultModel;

public interface IStorageManager {

    boolean storeEvent(String key, DMNEventModel event);

    List<DMNEventModel> getEventsByMatchingId(String key);

    List<DMNResultModel> getDecisions(String from, String to, String prefix);

    DmnModel getModel(String nameSpace);

    boolean storeModel(DmnModel model);

    List<DmnModel> getModelIds();
}
