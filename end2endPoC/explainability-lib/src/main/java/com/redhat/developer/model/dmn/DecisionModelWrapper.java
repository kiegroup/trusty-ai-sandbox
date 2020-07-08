package com.redhat.developer.model.dmn;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

import com.redhat.developer.model.DataDistribution;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureDistribution;
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
            if (contextVariables.containsKey("context")) {
                contextVariables = (Map<String, Object>) contextVariables.get("context");
            }
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
        return null;
    }

    @Override
    public PredictionOutput getOutputShape() {
        return null;
    }
}
