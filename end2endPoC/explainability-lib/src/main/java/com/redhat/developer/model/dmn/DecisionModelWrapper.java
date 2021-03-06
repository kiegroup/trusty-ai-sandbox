package com.redhat.developer.model.dmn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

import com.redhat.developer.model.DataDistribution;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureDistribution;
import com.redhat.developer.model.FeatureFactory;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.utils.DataUtils;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.dmn.api.core.ast.InputDataNode;
import org.kie.dmn.model.api.Decision;
import org.kie.kogito.decision.DecisionModel;

/**
 * {@link Model} implementation based on a Kogito {@link DecisionModel}.
 */
public class DecisionModelWrapper implements Model {

    private final DecisionModel decisionModel;

    public DecisionModelWrapper(DecisionModel decisionModel) {
        this.decisionModel = decisionModel;
    }

    @Override
    public List<PredictionOutput> predict(List<PredictionInput> inputs) {
        List<PredictionOutput> predictionOutputs = new LinkedList<>();
        for (PredictionInput input : inputs) {
            Map<String, Object> contextVariables = toMap(input.getFeatures());
            final DMNContext context = decisionModel.newContext(contextVariables);
            DMNResult dmnResult = decisionModel.evaluateAll(context);
            List<Output> outputs = new LinkedList<>();
            for (DMNDecisionResult decisionResult : dmnResult.getDecisionResults()) {
                Output output = new Output(decisionResult.getDecisionName(), Type.TEXT, new Value<>(decisionResult.getResult()), 1d);
                outputs.add(output);
            }
            PredictionOutput predictionOutput = new PredictionOutput(outputs);
            predictionOutputs.add(predictionOutput);
        }
        return predictionOutputs;
    }

    private Map<String, Object> toMap(List<Feature> features) {
        Map<String, Object> map = new HashMap<>();
        for (Feature f : features) {
            if (Type.COMPOSITE.equals(f.getType())) {
                List<Feature> compositeFeatures = (List<Feature>) f.getValue().getUnderlyingObject();
                Map<String, Object> maps = new HashMap<>();
                for (Feature cf : compositeFeatures) {
                    Map<String, Object> compositeFeatureMap = toMap(List.of(cf));
                    maps.putAll(compositeFeatureMap);
                }
                map.put(f.getName(), maps);
            } else {
                if (Type.UNDEFINED.equals(f.getType())) {
                    Feature underlyingFeature = (Feature) f.getValue().getUnderlyingObject();
                    map.put(f.getName(), toMap(List.of(underlyingFeature)));
                } else {
                    Object underlyingObject = f.getValue().getUnderlyingObject();
                    map.put(f.getName(), underlyingObject);
                }
            }
        }
        if (map.containsKey("context")) {
            map = (Map<String, Object>) map.get("context");
        }
        return map;
    }

    @Override
    public DataDistribution getDataDistribution() {
        List<FeatureDistribution> featureDistributions = new LinkedList<>();
        PredictionInput inputShape = getInputShape();
        for (Feature f : inputShape.getFeatures()) {
            if (Type.NUMBER.equals(f.getType()) || Type.BOOLEAN.equals(f.getType())) {
                double v = f.getValue().asNumber();
                double[] doubles = DoubleStream.of(DataUtils.generateData(0, 1, 1000)).map(d -> d * v + v).toArray();
                FeatureDistribution featureDistribution = DataUtils.getFeatureDistribution(doubles);
                featureDistributions.add(featureDistribution);
            }
        }
        return new DataDistribution(featureDistributions);
    }

    @Override
    public PredictionInput getInputShape() {
        Map<String, Object> context = new HashMap<>();
        for (InputDataNode input : decisionModel.getDMNModel().getInputs()) {
            String name = input.getName();
            DMNType type = input.getType();
            Object object = getObject(type);
            context.put(name, object);
        }
        return new PredictionInput(List.of(FeatureFactory.newCompositeFeature("context", context)));
    }

    private Object getObject(DMNType type) {
        Object object;
        if (type.isComposite()) {
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, DMNType> entry : type.getFields().entrySet()) {
                map.put(entry.getKey(), getObject(entry.getValue()));
            }
            object = map;
        } else {
            if (!type.getAllowedValues().isEmpty()) {
                object = type.getAllowedValues().get(0);
            } else {
                object = "";
            }
        }
        return object;
    }

    @Override
    public PredictionOutput getOutputShape() {
        List<Output> outputs = new LinkedList<>();
        for (DecisionNode node : decisionModel.getDMNModel().getDecisions()) {
            String name = node.getName();
            Decision decision = node.getDecision();
            Type type = Type.TEXT;
            if ("\"yes\" , \"no\"".equalsIgnoreCase(decision.getAllowedAnswers())) {
                type = Type.BOOLEAN;
            }
            Output output = new Output(name, type, new Value<>(decision.getLabel()), 1d);
            outputs.add(output);
        }
        return new PredictionOutput(outputs);
    }
}
