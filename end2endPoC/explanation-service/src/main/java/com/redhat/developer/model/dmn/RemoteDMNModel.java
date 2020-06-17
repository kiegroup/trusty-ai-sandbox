package com.redhat.developer.model.dmn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.model.DataDistribution;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureDistribution;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.requests.TypedData;
import com.redhat.developer.utils.DataUtils;
import com.redhat.developer.utils.HttpHelper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteDMNModel implements Model {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HttpHelper httpHelper;
    private final List<TypedData> inputStructure;
    private final List<TypedData> outputStructure;
    private final String modelName;

    public RemoteDMNModel(HttpHelper httpHelper, List<TypedData> inputStructure, List<TypedData> outputStructure, String modelName) {
        this.httpHelper = httpHelper;
        this.inputStructure = inputStructure;
        this.outputStructure = outputStructure;
        this.modelName = modelName;
    }

    private JSONObject toKogitoRequestJson(List<TypedData> inputStructure, List<Feature> features) {
        JSONObject json = new JSONObject();
        for (TypedData input : inputStructure) {
            if (input.value != null) { // is a built in type
                Value value = features.stream().filter(x -> x.getName().equals(input.inputName)).findFirst().get().getValue();
                json.put(input.inputName, input.typeRef.equals("string") ? value.asString() : value.asNumber());
            } else {
                json.put(input.inputName, toKogitoRequestJson(input.components, features));
            }
        }
        return json;
    }

    @Override
    public List<PredictionOutput> predict(List<PredictionInput> inputs) {
        List<PredictionOutput> result = new ArrayList<>();
        for (PredictionInput input : inputs) {
            String request = toKogitoRequestJson(inputStructure, input.getFeatures()).toString();
            String response = null;
            try {
                response = httpHelper.doPost("/" + modelName + "?tracing=false", request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.debug(request);
            Map<String, Object> outcome = null;
            try {
                outcome = new ObjectMapper().readValue(response, new HashMap<String, Object>().getClass());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            result.add(new PredictionOutput(flattenDmnResult(outcome, outputStructure.stream().map(x -> x.inputName).collect(Collectors.toList()))));
        }
        return result;
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
        return new PredictionInput(DMNUtils.extractInputFeatures(this.inputStructure));
    }

    @Override
    public PredictionOutput getOutputShape() {
        return new PredictionOutput(DMNUtils.extractOutputs(this.outputStructure));
    }

    private List<Output> flattenDmnResult(Map<String, Object> dmnResult, List<String> validOutcomeNames) {
        List<Output> result = new ArrayList<>();
        dmnResult.entrySet().stream().filter(x -> validOutcomeNames.contains(x.getKey())).forEach(x -> result.addAll(flattenOutput(x.getKey(), x.getValue())));
        return result;
    }

    private List<Output> flattenOutput(String key, Object value) {
        List<Output> result = new ArrayList<>();
        if (value instanceof Double || value instanceof Float) {
            result.add(new Output(key, Type.NUMBER, new Value<>((Double) value), 0));
            return result;
        }

        if (value instanceof Integer) {
            result.add(new Output(key, Type.NUMBER, new Value<>((Integer) value), 0));
            return result;
        }

        if (value instanceof Boolean) {
            Boolean vv = (Boolean) value;
            result.add(new Output(key, Type.NUMBER, new Value<>(vv ? 1d : 0d), 0));
            return result;
        }

        if (value instanceof String) {
            result.add(new Output(key, Type.TEXT, new Value<>((String) value), 0));
            return result;
        }

        Map<String, Object> aa = (Map) value;

        aa.entrySet().forEach(x -> result.addAll(flattenOutput(x.getKey(), x.getValue())));

        return result;
    }
}
