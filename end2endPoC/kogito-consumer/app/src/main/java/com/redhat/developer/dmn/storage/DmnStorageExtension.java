package com.redhat.developer.dmn.storage;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.database.TrustyStorageQuery;
import com.redhat.developer.database.operators.StringOperator;
import com.redhat.developer.dmn.models.DmnModel;

// TODO: check application scoped? Request scoped?
@ApplicationScoped
public class DmnStorageExtension implements IDmnStorageExtension {

    private static final String MODELINDEX = "dmnmodeldata";

    @Inject
    IStorageManager storageManager;

    @Override
    public DmnModel getModel(String modelId) {
        TrustyStorageQuery query = new TrustyStorageQuery()
                .where("modelId", StringOperator.EQUALS, modelId);
        return storageManager.search(query, MODELINDEX, DmnModel.class).get(0);
    }

    @Override
    public boolean storeModel(DmnModel model) {
        storageManager.create(model.modelId, model, MODELINDEX);
        return true;
    }

    @Override
    public List<DmnModel> getModelIds() {
        return storageManager.search(new TrustyStorageQuery(), MODELINDEX, DmnModel.class);
    }
}