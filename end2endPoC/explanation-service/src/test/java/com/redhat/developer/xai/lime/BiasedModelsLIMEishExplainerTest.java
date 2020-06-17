package com.redhat.developer.xai.lime;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureImportance;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.utils.DataUtils;
import com.redhat.developer.xai.ExplanationTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BiasedModelsLIMEishExplainerTest {

    private final static SecureRandom random = new SecureRandom();

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @RepeatedTest(10)
    public void testMapOneFeatureToOutputRegression() {
        int idx = 1;
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("f1", Type.NUMBER, new Value<>(100)));
        features.add(new Feature("f2", Type.NUMBER, new Value<>(20)));
        features.add(new Feature("f3", Type.NUMBER, new Value<>(0.1)));
        PredictionInput input = new PredictionInput(features);
        Model model = ExplanationTestUtils.getFeaturePassModel(idx);
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));

        LIMEishExplainer limEishExplainer = new LIMEishExplainer(100, 1);
        Saliency saliency = limEishExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> topFeatures = saliency.getTopFeatures(3);
        assertEquals(topFeatures.get(0).getFeature().getName(), features.get(idx).getName());
        assertTrue(topFeatures.get(1).getScore() < topFeatures.get(0).getScore() * 10);
        assertTrue(topFeatures.get(2).getScore() < topFeatures.get(0).getScore() * 10);
    }

    @RepeatedTest(10)
    public void testUnusedFeatureRegression() {
        int idx = 2;
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("f1", Type.NUMBER, new Value<>(100)));
        features.add(new Feature("f2", Type.NUMBER, new Value<>(20)));
        features.add(new Feature("f3", Type.NUMBER, new Value<>(10)));
        Model model = ExplanationTestUtils.getSumSkipModel(idx);
        PredictionInput input = new PredictionInput(features);
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));
        LIMEishExplainer limEishExplainer = new LIMEishExplainer(1000, 1);
        Saliency saliency = limEishExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> perFeatureImportance = saliency.getPerFeatureImportance();

        perFeatureImportance.sort((t1, t2) -> (int) (t2.getScore() - t1.getScore()));
        assertTrue(perFeatureImportance.get(0).getScore() > 0);
        assertTrue(perFeatureImportance.get(1).getScore() > 0);
        assertEquals(features.get(idx).getName(), perFeatureImportance.get(2).getFeature().getName());
    }

    @RepeatedTest(10)
    public void testMapOneFeatureToOutputClassification() {
        int idx = 1;
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("f1", Type.NUMBER, new Value<>(3)));
        features.add(new Feature("f2", Type.NUMBER, new Value<>(2)));
        features.add(new Feature("f3", Type.NUMBER, new Value<>(7)));
        PredictionInput input = new PredictionInput(features);
        Model model = ExplanationTestUtils.getEvenFeatureModel(idx);
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));

        LIMEishExplainer limEishExplainer = new LIMEishExplainer(1000, 1);
        Saliency saliency = limEishExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> topFeatures = saliency.getTopFeatures(1);
        assertEquals(features.get(idx).getName(), topFeatures.get(0).getFeature().getName());
    }

    @RepeatedTest(10)
    public void testTextSpamClassification() {
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("f1", Type.TEXT, new Value<>("we go here and there")));
        features.add(new Feature("f2", Type.TEXT, new Value<>("please give me some money")));
        features.add(new Feature("f3", Type.TEXT, new Value<>("dear friend, please reply")));
        PredictionInput input = new PredictionInput(features);
        Model model = ExplanationTestUtils.getTextClassifier();
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));

        LIMEishExplainer limEishExplainer = new LIMEishExplainer(1000, 1);
        Saliency saliency = limEishExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> topFeatures = saliency.getPositiveFeatures(1);
        assertEquals("money (f2)", topFeatures.get(0).getFeature().getName());
    }

    @Disabled
    public void testUnusedFeatureClassification() {
        int idx = 2;
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("f1", Type.NUMBER, new Value<>(6)));
        features.add(new Feature("f2", Type.NUMBER, new Value<>(3)));
        features.add(new Feature("f3", Type.NUMBER, new Value<>(5)));
        Model model = ExplanationTestUtils.getEvenSumModel(idx);
        PredictionInput input = new PredictionInput(features);
        List<PredictionOutput> outputs = model.predict(List.of(input));
        Prediction prediction = new Prediction(input, outputs.get(0));
        LIMEishExplainer limEishExplainer = new LIMEishExplainer(100, 1);
        Saliency saliency = limEishExplainer.explain(prediction, model);

        assertNotNull(saliency);
        List<FeatureImportance> perFeatureImportance = saliency.getTopFeatures(3);
        assertEquals(features.get(idx).getName(), perFeatureImportance.get(perFeatureImportance.size() - 1).getFeature().getName());
    }
}