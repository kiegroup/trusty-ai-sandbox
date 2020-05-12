package com.redhat.developer.execution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.redhat.developer.dmn.IDmnService;
import com.redhat.developer.dmn.models.input.InputData;
import com.redhat.developer.dmn.models.input.ModelInputStructure;
import com.redhat.developer.dmn.models.input.TypeComponent;
import com.redhat.developer.dmn.models.input.TypeDefinition;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.models.OutcomeModel;
import com.redhat.developer.execution.models.OutcomeModelWithInputs;
import com.redhat.developer.execution.responses.decisions.DecisionInputStructured;
import com.redhat.developer.execution.responses.decisions.inputs.DecisionStructuredInputsResponse;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;
import com.redhat.developer.execution.storage.IExecutionsStorageExtension;
import org.kie.dmn.core.ast.DMNBaseNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ExecutionService implements IExecutionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionService.class);

    @Inject
    IExecutionsStorageExtension storageExtension;

    @Inject
    IDmnService dmnService;


    public List<SingleDecisionInputResponse> getStructuredOutcomesValues(DMNResultModel event){
        Map<DMNBaseNode, List<DMNBaseNode>> dependencyGraph = dmnService.getDmnDependencyGraph(event.modelId);

        List<SingleDecisionInputResponse> response = new ArrayList<>();

        for (OutcomeModel outcome : event.decisions){
            DMNBaseNode baseNode = dependencyGraph.keySet().stream().filter(x -> x.getId().equals(outcome.outcomeId)).findFirst().get();
            response.add(buildStructuredForValue(
                    event.modelId,
                    baseNode.getName(),
                    baseNode.getType().getBaseType() != null ? baseNode.getType().getBaseType().getName() : baseNode.getType().getName(),
                    baseNode.getType().isComposite(),
                    outcome.result));
        }

        DMNBaseNode topLevelNode = dependencyGraph.entrySet()
                .stream()
                .filter(kv -> kv.getValue().size() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).get(0);

        SingleDecisionInputResponse top = response.stream().filter(x -> x.inputName.equals(topLevelNode.getName())).findFirst().get();

        response.remove(top);
        response.add(0, top);

        return response;
    }

    @Override
    public boolean storeEvent(String key, DMNResultModel event) {
        return storageExtension.storeEvent(key, event);
    }

    @Override
    public List<DMNResultModel> getEventsByMatchingId(String key) {
        return storageExtension.getEventsByMatchingId(key);
    }

    @Override
    public List<DMNResultModel> getDecisions(String from, String to, String prefix) {
        return storageExtension.getDecisions(from, to, prefix);
    }

    @Override
    public OutcomeModelWithInputs getStructuredOutcome(String outcomeId, DMNResultModel event){

        Map<DMNBaseNode, List<DMNBaseNode>> dependencyGraph = dmnService.getDmnDependencyGraph(event.modelId);

        List<DMNBaseNode> incomingDependencies = dependencyGraph.entrySet().stream().filter(x -> x.getValue().stream().anyMatch(y -> y.getId().equals(outcomeId))).map(x -> x.getKey()).collect(Collectors.toList());
        Map<String, OutcomeModel> modelDecisions = new HashMap<>();
        event.decisions.forEach(x -> modelDecisions.put(x.outcomeId, x));

        List<DMNBaseNode> outcomeModels = new ArrayList<>();
        Map<String, DMNBaseNode> inputModels = new HashMap<>();
        for (DMNBaseNode node : incomingDependencies){
            if (modelDecisions.keySet().contains(node.getId())){ // is a internal decision
                outcomeModels.add(node);
            }
            else{
                inputModels.put(node.getName(), node);
            }
        }

        DecisionStructuredInputsResponse structuredInputs = getStructuredInputs(event);
        List<SingleDecisionInputResponse> onlyUsedInputs = structuredInputs.input.stream().filter(x -> inputModels.keySet().contains(x.inputName)).collect(Collectors.toList());

        List<SingleDecisionInputResponse> onlyUsedDecisions = outcomeModels
                .stream().map(node ->
                                      buildStructuredForValue(
                                              event.modelId,
                                              node.getName(),
                                              node.getType().getBaseType() != null ? node.getType().getBaseType().getName() : node.getType().getName(),
                                              node.getType().isComposite(),
                                              modelDecisions.get(node.getId()).result))
                .collect(Collectors.toList());

        List<DecisionInputStructured> components = new ArrayList<>();

        onlyUsedInputs.stream().forEach(x -> components.add(new DecisionInputStructured(dependencyGraph.keySet().stream().filter(y -> y.getName().equals(x.inputName)).findFirst().get().getId(), "Input", x)));
        onlyUsedDecisions.stream().forEach(x -> components.add(new DecisionInputStructured(modelDecisions.values().stream().filter(y -> y.outcomeName.equals(x.inputName)).findFirst().get().outcomeId, "Decision", x)));

        OutcomeModel outcomeModel = event.decisions.stream().filter(x -> x.outcomeId.equals(outcomeId)).findFirst().get();
        DMNBaseNode baseNode = dependencyGraph.keySet().stream().filter(x -> x.getId().equals(outcomeId)).findFirst().get();
        SingleDecisionInputResponse bbn = buildStructuredForValue(
                event.modelId,
                baseNode.getName(),
                baseNode.getType().getBaseType() != null ? baseNode.getType().getBaseType().getName() : baseNode.getType().getName(),
                baseNode.getType().isComposite(),
                outcomeModel.result);

        OutcomeModelWithInputs response = new OutcomeModelWithInputs(outcomeModel.outcomeId, outcomeModel.outcomeName, outcomeModel.evaluationStatus, bbn, outcomeModel.messages, outcomeModel.hasErrors, components);
        return response;
    }

    public DecisionStructuredInputsResponse getStructuredInputs(DMNResultModel event){
        String modelId = event.modelId;
        Map<String, Object> context = event.context;

        ModelInputStructure dmnInputStructure = dmnService.getDmnInputStructure(modelId);

        DecisionStructuredInputsResponse response = new DecisionStructuredInputsResponse();

        response.input = new ArrayList<>();

        for (String inputName : context.keySet()) {
            Optional<InputData> modelInputDataOpt = dmnInputStructure.inputData.stream().filter(x -> x.name.equals(inputName)).findFirst();

            if (!modelInputDataOpt.isPresent()) { // It's a decision
                continue;
            }

            InputData modelInputData = modelInputDataOpt.get();

            if (!modelInputData.isComposite) {
                response.input.add(new SingleDecisionInputResponse(inputName, modelInputData.typeRef, null, context.get(inputName)));
            } else {
                response.input.add(new SingleDecisionInputResponse(inputName,
                                                                   modelInputData.typeRef,
                                                                   analyzeComponents(context.get(inputName), modelInputData.typeRef, dmnInputStructure.customTypes),
                                                                   null
                                   )
                );
            }
        }
        return response;
    }

    private SingleDecisionInputResponse buildStructuredForValue(String modelId, String name, String typeRef, boolean isComposite, Object value) {
        ModelInputStructure dmnInputStructure = dmnService.getDmnInputStructure(modelId);
        List<TypeDefinition> customTypes = dmnInputStructure.customTypes;
        if (isComposite){
            return new SingleDecisionInputResponse(name, typeRef, analyzeComponents(value, typeRef, customTypes), null);
        }
        return new SingleDecisionInputResponse(name, typeRef, null, value);
    }

    private List<List<SingleDecisionInputResponse>> analyzeComponents(Object value, String typeRef, List<TypeDefinition> definitions) {
        TypeDefinition typeDefinition = definitions.stream().filter(x -> x.typeName.equals(typeRef)).findFirst().orElseThrow(() -> new NoSuchElementException());
        List<List<SingleDecisionInputResponse>> components = new ArrayList<>();

        if (typeDefinition.isCollection) {
            List<Map<String, Object>> ss = (List) value;
            for (Map<String, Object> aa : ss) {
                List<SingleDecisionInputResponse> component = buildComponent(aa, typeDefinition, definitions);
                components.add(component);
            }
        } else {
            Map<String, Object> aa = (Map) value;
            List<SingleDecisionInputResponse> component = buildComponent(aa, typeDefinition, definitions);
            components.add(component);
        }
        return components;
    }

    private List<SingleDecisionInputResponse> buildComponent(Map<String, Object> aa, TypeDefinition typeDefinition, List<TypeDefinition> definitions) {
        List<SingleDecisionInputResponse> component = new ArrayList<>();
        for (Map.Entry<String, Object> entry : aa.entrySet()) {
            String componentInputName = entry.getKey();
            Object componentValue = entry.getValue();
            TypeComponent componentType = typeDefinition.components.stream().filter(x -> x.typeRef.equals(componentInputName) || x.name.equals(componentInputName)).findFirst().orElseThrow(() -> new NoSuchElementException());
            if (!componentType.isComposite) {
                component.add(new SingleDecisionInputResponse(componentInputName, componentType.typeRef, null, componentValue));
            } else {
                List<List<SingleDecisionInputResponse>> myComponent = analyzeComponents(componentValue, componentType.typeRef, definitions);
                component.add(new SingleDecisionInputResponse(componentInputName, componentType.typeRef, myComponent, null));
            }
        }
        return component;
    }
}
