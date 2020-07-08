package com.redhat.developer.xai.lime;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.utils.DataUtils;
import com.redhat.developer.xai.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class LimeExplainerTest {

    private final static SecureRandom random = new SecureRandom();

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @Test
    void testEmptyPrediction() {
        LimeExplainer limeExplainer = new LimeExplainer(10, 1);
        PredictionOutput output = mock(PredictionOutput.class);
        PredictionInput input = mock(PredictionInput.class);
        Prediction prediction = new Prediction(input, output);
        Model model = mock(Model.class);
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
    }

    @Test
    void testNonEmptyInput() {
        LimeExplainer limeExplainer = new LimeExplainer(10, 1);
        PredictionOutput output = mock(PredictionOutput.class);
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            features.add(TestUtils.getRandomFeature());
        }
        PredictionInput input = new PredictionInput(features);
        Prediction prediction = new Prediction(input, output);
        Model model = mock(Model.class);
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
    }

    @Test
    void testNonEmptyInputAndOutputWithTextClassifier() {
        LimeExplainer limeExplainer = new LimeExplainer(10, 1);
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            features.add(TestUtils.getRandomFeature());
        }
        PredictionInput input = new PredictionInput(features);
        Model model = TestUtils.getTextClassifier();
        Prediction prediction = new Prediction(input, model.predict(List.of(input)).get(0));
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
    }
}