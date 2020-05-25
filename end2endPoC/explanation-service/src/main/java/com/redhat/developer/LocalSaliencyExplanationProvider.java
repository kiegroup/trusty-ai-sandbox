package com.redhat.developer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.requests.TypedData;
import com.redhat.developer.utils.HttpHelper;
import com.redhat.developer.xai.LIMEishExplainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LocalSaliencyExplanationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final HttpHelper httpHelper = new HttpHelper("http://producer:1337/");

    /**
     * no. of samples to be generated for the local linear model training
     */
    private final int noOfSamples;

    /**
     * no. of perturbations to perform on a prediction
     */
    private final int noOfPerturbations;

    public LocalSaliencyExplanationProvider() {
        this(100, 1);
    }

    public LocalSaliencyExplanationProvider(int noOfSamples, int noOfPerturbations) {
        this.noOfSamples = noOfSamples;
        this.noOfPerturbations = noOfPerturbations;
    }

    public LocalSaliencyExplanationProvider(int noOfSamples) {
        this(noOfSamples, 1);
    }

    public Saliency explain(List<TypedData> dmnInputs, List<TypedData> dmnOutputs, String modelName) {
        Prediction prediction = convert(dmnInputs, dmnOutputs);
        LIMEishExplainer limEishExplainer = new LIMEishExplainer(noOfSamples, noOfPerturbations);
        Model model = inputs -> runDMN(inputs, dmnInputs, dmnOutputs, modelName);
        return limEishExplainer.explain(prediction, model);
    }

    protected List<PredictionOutput> runDMN(List<PredictionInput> perturbedInputs, List<TypedData> originalInput,
                                            List<TypedData> originalOutputs, String modelName) {
        List<PredictionOutput> result = new ArrayList<>();
        for (PredictionInput perturbatedInput : perturbedInputs) {
            String request = perturbatedInput.toKogitoRequestJson(originalInput).toString();
            String response = null;
            try {
                response = httpHelper.doPost("/" + modelName + "?tracing=false", request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info(request);
            Map<String, Object> outcome = null;
            try {
                outcome = new ObjectMapper().readValue(response, new HashMap<String, Object>().getClass());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            result.add(new PredictionOutput(flattenDmnResult(outcome, originalOutputs.stream().map(x -> x.inputName).collect(Collectors.toList()))));
        }
        return result;
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
            result.add(new Output(key, Type.STRING, new Value<>((String) value), 0));
            return result;
        }

        Map<String, Object> aa = (Map) value;

        aa.entrySet().forEach(x -> result.addAll(flattenOutput(x.getKey(), x.getValue())));

        return result;
    }

    private Prediction convert(List<TypedData> inputs, List<TypedData> outputs) {
        PredictionInput predictionInput = new PredictionInput(extractInputFeatures(inputs));
        PredictionOutput predictionOutput = new PredictionOutput(extractOutputs(outputs));
        return new Prediction(predictionInput, predictionOutput);
    }

    private List<Feature> extractInputFeatures(List<TypedData> data) {
        List<Feature> features = new ArrayList<>();
        for (TypedData input : data) {
            List<Feature> result = getFlatBuiltInInput(input);
            features.addAll(result);
        }
        return features;
    }

    private List<Output> extractOutputs(List<TypedData> data) {
        List<Output> features = new ArrayList<>();
        for (TypedData input : data) {
            List<Output> result = getFlatBuiltInOutputs(input);
            features.addAll(result);
        }
        return features;
    }

    private List<Output> getFlatBuiltInOutputs(TypedData input) {
        List<Output> features = new ArrayList<>();
        if (input.typeRef.equals("string")) {
            features.add(new Output(input.inputName, Type.STRING, new Value<>((String) input.value), 0));
            return features;
        }
        if (input.typeRef.equals("number")) {
            features.add(new Output(input.inputName, Type.NUMBER, new Value<>(Double.valueOf(String.valueOf(input.value))), 0));
            return features;
        }
        if (input.typeRef.equals("boolean")) {
            features.add(new Output(input.inputName, Type.BOOLEAN, new Value<>((Boolean) input.value), 0));
            return features;
        }

        input.components.forEach(x -> features.addAll(getFlatBuiltInOutputs(x)));
        return features;
    }

    private List<Feature> getFlatBuiltInInput(TypedData input) {
        List<Feature> features = new ArrayList<>();
        if (input.typeRef.equals("string")) {
            features.add(new Feature(input.inputName, Type.STRING, new Value<>((String) input.value)));
            return features;
        }
        if (input.typeRef.equals("number")) {
            features.add(new Feature(input.inputName, Type.NUMBER, new Value<>(Double.valueOf(String.valueOf(input.value)))));
            return features;
        }
        input.components.forEach(x -> features.addAll(getFlatBuiltInInput(x)));
        return features;
    }
}
