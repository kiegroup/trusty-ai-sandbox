package com.redhat.developer;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import com.redhat.developer.model.DataDistribution;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.model.dmn.TypedData;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalSaliencyExplanationProviderTest {

    private final static SecureRandom random = new SecureRandom();

    @Test
    public void testEmptyData() {
        LocalSaliencyExplanationProvider explanationProvider = new LocalSaliencyExplanationProvider();
        List<TypedData> inputs = new LinkedList<>();
        List<TypedData> outputs = new LinkedList<>();
        String name = "dummy";
        Saliency saliency = explanationProvider.explain(inputs, outputs, name);
        assertNotNull(saliency);
    }

    @Test
    public void testDepthZeroInputData() {
        LocalSaliencyExplanationProvider explanationProvider = new LocalSaliencyExplanationProvider() {
            @Override
            protected Model getModel(List<TypedData> dmnInputs, List<TypedData> dmnOutputs, String modelName) {
                return getRandomModel();
            }
        };
        List<TypedData> inputs = getTypedData(0);
        TypedData outputData = new TypedData();
        outputData.value = "0";
        outputData.typeRef = "number";
        outputData.inputName = "spam";
        List<TypedData> outputs = List.of(outputData);
        String name = "dummy";
        Saliency saliency = explanationProvider.explain(inputs, outputs, name);
        assertNotNull(saliency);
    }

    @Test
    public void testDepthOneInputData() {
        LocalSaliencyExplanationProvider explanationProvider = new LocalSaliencyExplanationProvider() {
            @Override
            protected Model getModel(List<TypedData> dmnInputs, List<TypedData> dmnOutputs, String modelName) {
                return getRandomModel();
            }
        };
        List<TypedData> inputs = getTypedData(1);
        TypedData outputData = new TypedData();
        outputData.value = "0";
        outputData.typeRef = "number";
        outputData.inputName = "spam";
        List<TypedData> outputs = List.of(outputData);
        String name = "dummy";
        Saliency saliency = explanationProvider.explain(inputs, outputs, name);
        assertNotNull(saliency);
    }

    @Test
    public void testDepthTwoInputData() {
        LocalSaliencyExplanationProvider explanationProvider = new LocalSaliencyExplanationProvider() {
            @Override
            protected Model getModel(List<TypedData> dmnInputs, List<TypedData> dmnOutputs, String modelName) {
                return getRandomModel();
            }
        };
        List<TypedData> inputs = getTypedData(2);
        TypedData outputData = new TypedData();
        outputData.value = "0";
        outputData.typeRef = "number";
        outputData.inputName = "spam";
        List<TypedData> outputs = List.of(outputData);
        String name = "dummy";
        Saliency saliency = explanationProvider.explain(inputs, outputs, name);
        assertNotNull(saliency);
    }

    private Model getRandomModel() {
        return new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> predictionOutputs = new LinkedList<>();
                for (PredictionInput ignored : inputs) {
                    predictionOutputs.add(new PredictionOutput(List.of(new Output("dummy", Type.BOOLEAN, new Value<>(random.nextBoolean()), 1d))));
                }
                return predictionOutputs;
            }

            @Override
            public DataDistribution getDataDistribution() {
                return null;
            }

            @Override
            public PredictionInput getInputShape() {
                return null;
            }

            @Override
            public PredictionOutput getOutputShape() {
                return null;
            }
        };
    }

    private List<TypedData> getTypedData(int depth) {
        List<TypedData> list = new LinkedList<>();
        int size = 1 + random.nextInt(3);
        for (int i = 0; i < size; i++) {
            TypedData typedData = new TypedData();
            typedData.inputName = RandomStringUtils.random(1);
            if (depth == 0) {
                if (random.nextBoolean()) {
                    typedData.typeRef = "number";
                    typedData.value = String.valueOf(random.nextDouble());
                } else {
                    typedData.typeRef = "string";
                    typedData.value = RandomStringUtils.random(random.nextInt(3));
                }
            } else {
                typedData.components = getTypedData(depth - 1);
                typedData.typeRef = "complexType";
            }
            list.add(typedData);
        }
        return list;
    }
}