package com.redhat.developer.dmn.storage;

import java.util.List;

import com.redhat.developer.dmn.models.DmnModel;

public interface IDmnStorageExtension {

    DmnModel getModel(String modelId);

    boolean storeModel(DmnModel model);

    List<DmnModel> getModelIds();
}
