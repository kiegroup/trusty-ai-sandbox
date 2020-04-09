package com.redhat.developer.dmn;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.dmn.storage.dto.DmnModel;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.impl.DMNContextImpl;

@ApplicationScoped
public class DmnService implements IDmnService{

    @Inject
    IStorageManager storageManager;

    private final static int cacheSize = 5;

    private HashMap<String, DMNRuntime> decisionModelCache = new HashMap<>();

    @Override
    public Object evaluateModel(String id, Map<String, Object> inputs){
        if ( decisionModelCache.containsKey(id) ){
            DMNRuntime runtime = decisionModelCache.get(id);
            return runtime.evaluateAll(runtime.getModels().get(0), new DMNContextImpl(inputs)).getContext();
        }

        if(decisionModelCache.size() == cacheSize){
            decisionModelCache.remove(decisionModelCache.keySet().toArray()[new Random().nextInt() % cacheSize]);
        }

        DmnModel model = storageManager.getModel(id);
        decisionModelCache.put(id, DMNKogito.createGenericDMNRuntime(new StringReader(model.model)));
        DMNRuntime runtime = decisionModelCache.get(id);

        return  runtime.evaluateAll(runtime.getModels().get(0), new DMNContextImpl(inputs)).getContext();
    }
}
