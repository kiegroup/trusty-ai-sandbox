package com.redhat.developer.lime;

import java.util.LinkedList;
import java.util.List;

import com.redhat.developer.LIMEishSaliencyExplanationProvider;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureImportance;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.requests.TypedData;
import com.redhat.developer.utils.DataUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LIMEFeaturePassModelTest {

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @RepeatedTest(10)
    public void testMapOneFeatureToOutputRegressionExplanation() {
        TypedData predictionInput = new TypedData();
        List<TypedData> inputs = new LinkedList<>();
        List<TypedData> data = new LinkedList<>();
        TypedData e = new TypedData();
        e.typeRef = "number";
        e.inputName = "one";
        e.value = "100";
        data.add(e);
        e = new TypedData();
        e.typeRef = "number";
        e.inputName = "two";
        e.value = "20";
        data.add(e);
        e = new TypedData();
        e.typeRef = "number";
        e.inputName = "three";
        e.value = "0.1";
        data.add(e);
        predictionInput.inputName = "predictioninput";
        predictionInput.components = data;
        predictionInput.typeRef = "multi";
        inputs.add(predictionInput);

        int idx = 0;

        List<TypedData> outputs = new LinkedList<>();
        TypedData o = new TypedData();
        o.components = List.of(data.get(idx));
        o.typeRef = "single-feature";
        o.inputName = "feature-" + idx;
        outputs.add(o);

        String modelName = "featurePassModel";

        LIMEishSaliencyExplanationProvider lime = new LIMEishSaliencyExplanationProvider(100) {
            @Override
            protected List<PredictionOutput> predict(List<PredictionInput> perturbedInputs, List<TypedData> originalInput,
                                                     List<TypedData> originalOutputs, String modelName) {
                return LIMEFeaturePassModelTest.this.predict(perturbedInputs, idx);
            }
        };

        Saliency saliency = lime.explain(inputs, outputs, modelName);

        assertNotNull(saliency);
        List<FeatureImportance> topFeatures = saliency.getTopFeatures(3);
        assertEquals(topFeatures.get(0).getFeature().getName(), data.get(idx).inputName);
        assertTrue(topFeatures.get(1).getScore() < topFeatures.get(0).getScore() * 10);
        assertTrue(topFeatures.get(2).getScore() < topFeatures.get(0).getScore() * 10);
    }

    private List<PredictionOutput> predict(List<PredictionInput> perturbedInputs, int idx) {
        List<PredictionOutput> predictionOutputs = new LinkedList<>();
        for (PredictionInput predictionInput : perturbedInputs) {
            List<Feature> features = predictionInput.getFeatures();
            Feature feature = features.get(idx);
            PredictionOutput predictionOutput = new PredictionOutput(
                    List.of(new Output("feature-" + idx, feature.getType(), feature.getValue(), 1d)));
            predictionOutputs.add(predictionOutput);
        }
        return predictionOutputs;
    }
}