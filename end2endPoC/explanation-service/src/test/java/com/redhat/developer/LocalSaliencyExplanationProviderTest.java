package com.redhat.developer;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.requests.TypedData;
import com.redhat.developer.xai.ExplanationTestUtils;
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
            protected List<PredictionOutput> runDMN(List<PredictionInput> perturbedInputs, List<TypedData> originalInput,
                                                    List<TypedData> originalOutputs, String modelName) {
                return ExplanationTestUtils.getTextClassifier().predict(perturbedInputs);
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
            protected List<PredictionOutput> runDMN(List<PredictionInput> perturbedInputs, List<TypedData> originalInput,
                                                    List<TypedData> originalOutputs, String modelName) {
                return ExplanationTestUtils.getTextClassifier().predict(perturbedInputs);
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
            protected List<PredictionOutput> runDMN(List<PredictionInput> perturbedInputs, List<TypedData> originalInput,
                                                    List<TypedData> originalOutputs, String modelName) {
                return ExplanationTestUtils.getTextClassifier().predict(perturbedInputs);
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

    private List<TypedData> getTypedData(int depth) {
        List<TypedData> list = new LinkedList<>();
        int size = 1 + random.nextInt(5);
        for (int i = 0; i < size; i++) {
            TypedData typedData = new TypedData();
            typedData.inputName = RandomStringUtils.random(1);
            if (depth == 0) {
                if (random.nextBoolean()) {
                    typedData.typeRef = "number";
                    typedData.value = String.valueOf(random.nextDouble());
                } else {
                    typedData.typeRef = "string";
                    typedData.value = RandomStringUtils.random(random.nextInt(5));
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