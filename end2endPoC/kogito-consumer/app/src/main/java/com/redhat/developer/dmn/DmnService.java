package com.redhat.developer.dmn;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.kie.dmn.api.core.ast.DMNNode;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.api.core.ast.ItemDefNode;
import org.kie.dmn.core.ast.DMNBaseNode;
import org.kie.dmn.core.impl.DMNContextImpl;
import org.kie.dmn.core.impl.DMNModelImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DmnService implements IDmnService {

    private final static int cacheSize = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(DmnService.class);

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

        Set<ItemDefNode> allDefinitions = model.getItemDefinitions();
        for (ItemDefNode ff : allDefinitions) {
            String comp = ff.getType().getName();
            if (!modelInputStructure.customTypes.stream().filter(x -> x.typeName.equals(comp)).findFirst().isPresent() && comp != "number" && comp != "boolean" && comp != "string" && comp != "dateTime") { // TODO: REVIEW BUILT IN TYPES
                List<TypeComponent> components = ff.getType().getFields().entrySet().stream().map(x -> new TypeComponent(
                        x.getKey(), x.getValue().getName(), x.getValue().isCollection(), x.getValue().isComposite(), false, null)).collect(Collectors.toList());
                modelInputStructure.customTypes.add(new TypeDefinition(ff.getType().getName(), ff.getType().isCollection(), ff.getType().isComposite(), components));
            }
        }
        return modelInputStructure;
    }

    public Map<DMNBaseNode, List<DMNBaseNode>> getDmnDependencyGraph(String id) {
        DMNModelImpl model = (DMNModelImpl) getDmnModel(id);
        return getGraph(model);
    }

    private void process(Stream<DMNBaseNode> stream, Map<DMNBaseNode, List<DMNBaseNode>> usedWhere) {
        stream.forEach(base -> {
            usedWhere.computeIfAbsent(base, x -> new ArrayList<>());
            for (DMNNode d : base.getDependencies().values()) {
                usedWhere.computeIfAbsent((DMNBaseNode) d, x -> new ArrayList<>()).add(base);
            }
        });
    }

    public Map<DMNBaseNode, List<DMNBaseNode>> getGraph(DMNModelImpl model) {
        Map<DMNBaseNode, List<DMNBaseNode>> usedWhere = new HashMap<>();
        process(model.getInputs().stream().map(DMNBaseNode.class::cast), usedWhere);
        process(model.getDecisions().stream().map(DMNBaseNode.class::cast), usedWhere);
        process(model.getBusinessKnowledgeModels().stream().map(DMNBaseNode.class::cast), usedWhere);
        process(model.getDecisionServices().stream().map(DMNBaseNode.class::cast), usedWhere);
        return usedWhere;
    }

    @Override
    public Map<String, Object> evaluateModel(String id, Map<String, Object> inputs) {
        DMNRuntime runtime = getDmnRuntime(id);
        return runtime.evaluateAll(runtime.getModels().get(0), new DMNContextImpl(inputs)).getContext().getAll();
    }

    private DMNRuntime getDmnRuntime(String id) {
        if (decisionModelCache.containsKey(id)) {
            return decisionModelCache.get(id);
        }

        if (decisionModelCache.size() == cacheSize) {
            decisionModelCache.remove(decisionModelCache.keySet().toArray()[new Random().nextInt() % cacheSize]);
        }

        DmnModel model = storageExtension.getModel(id);
        decisionModelCache.put(id, DMNKogito.createGenericDMNRuntime(new StringReader(model.model)));
        return decisionModelCache.get(id);
    }
}
