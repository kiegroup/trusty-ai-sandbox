package com.redhat.developer.xai.lime;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.utils.DataUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LIMEishExplainerTest {

    private final static SecureRandom random = new SecureRandom();

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @Test
    public void testEmptyDatasetEncoding() {
        List<PredictionInput> inputs = new LinkedList<>();
        List<Output> outputs = new LinkedList<>();
        List<Feature> features = new LinkedList<>();
        PredictionInput originalInput = new PredictionInput(features);
        Output originalOutput = new Output("foo", Type.NUMBER, new Value<>(1), 1d);
        Collection<Pair<double[], Double>> trainingSet = LIMEishExplainer.encodeTrainingSet(inputs, outputs, originalInput, originalOutput);
        assertNotNull(trainingSet);
        assertTrue(trainingSet.isEmpty());
    }

    @Test
    public void testDatasetEncodingWithNumericData() {
        List<PredictionInput> perturbedInputs = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            List<Feature> inputFeatures = new LinkedList<>();
            for (int j = 0; j < 3; j++) {
                inputFeatures.add(new Feature("f" + random.nextInt(), Type.NUMBER, new Value<>(random.nextInt())));
            }
            perturbedInputs.add(new PredictionInput(inputFeatures));
        }
        List<Output> outputs = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            outputs.add(new Output("o", Type.NUMBER, new Value<>(random.nextBoolean()), 1d));
        }
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            features.add(new Feature("f" + random.nextInt(), Type.NUMBER, new Value<>(random.nextInt())));
        }
        PredictionInput originalInput = new PredictionInput(features);
        Output originalOutput = new Output("o", Type.BOOLEAN, new Value<>(random.nextBoolean()), 1d);
        Collection<Pair<double[], Double>> trainingSet = LIMEishExplainer.encodeTrainingSet(perturbedInputs, outputs, originalInput, originalOutput);
        assertNotNull(trainingSet);
        assertEquals(10, trainingSet.size());
    }
}