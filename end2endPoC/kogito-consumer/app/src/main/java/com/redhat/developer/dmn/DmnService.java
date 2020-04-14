package com.redhat.developer.dmn;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.developer.dmn.models.DmnModel;
import com.redhat.developer.dmn.models.input.InputData;
import com.redhat.developer.dmn.models.input.ModelInputStructure;
import com.redhat.developer.dmn.models.input.TypeComponent;
import com.redhat.developer.dmn.models.input.TypeDefinition;
import com.redhat.developer.dmn.storage.IDmnStorageExtension;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.core.impl.DMNContextImpl;

@ApplicationScoped
public class DmnService implements IDmnService {

    private final static int cacheSize = 5;
    @Inject
    IDmnStorageExtension storageExtension;
    private HashMap<String, DMNRuntime> decisionModelCache = new HashMap<>();

    @Override
    public DMNModel getDmnModel(String id) {
        DMNRuntime runtime = getDmnRuntime(id);
        return runtime.getModels().get(0);
    }

    @Override
    public ModelInputStructure getDmnInputStructure(String id) {
        DMNModel model = getDmnModel(id);
        Set<InputDataNode> inputs = model.getInputs();
        ModelInputStructure modelInputStructure = new ModelInputStructure();
        modelInputStructure.inputData = inputs.stream().map(x -> new InputData(x.getName(), x.getType().getName(), x.getType().isComposite(), x.getType().isCollection())).collect(Collectors.toList());
        modelInputStructure.customTypes = new ArrayList<>();

        for (InputDataNode input : inputs) {
            if (input.getType().isComposite()) {
                List<TypeComponent> components = input.getType().getFields().values().stream().map(x -> new TypeComponent(
                        x.getName(), x.getBaseType().getName(), x.isCollection(), x.isComposite(), false, null)).collect(Collectors.toList());
                modelInputStructure.customTypes.add(new TypeDefinition(input.getType().getName(), input.getType().isCollection(), input.getType().isComposite(), components));
            }
        }

        return modelInputStructure;
    }

    @Override
    public Object evaluateModel(String id, Map<String, Object> inputs) {
        DMNRuntime runtime = getDmnRuntime(id);
        return runtime.evaluateAll(runtime.getModels().get(0), new DMNContextImpl(inputs)).getContext();
    }

    private DMNRuntime getDmnRuntime(String id) {
        System.out.println(id);
        if (decisionModelCache.containsKey(id)) {
            return decisionModelCache.get(id);
        }

        if (decisionModelCache.size() == cacheSize) {
            decisionModelCache.remove(decisionModelCache.keySet().toArray()[new Random().nextInt() % cacheSize]);
        }

        DmnModel model = storageExtension.getModel(id);
        System.out.println(model);
        decisionModelCache.put(id, DMNKogito.createGenericDMNRuntime(new StringReader(model.model)));
        return decisionModelCache.get(id);
    }
}
