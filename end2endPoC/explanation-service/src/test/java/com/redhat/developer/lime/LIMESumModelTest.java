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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LIMESumModelTest {

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @RepeatedTest(10)
    public void testUnusedFeature() {
        // explain a model that takes 2 out of 3 features and returns their sum
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
        e.value = "10";
        data.add(e);
        predictionInput.inputName = "predictioninput";
        predictionInput.components = data;
        predictionInput.typeRef = "multi";
        inputs.add(predictionInput);

        int idx = 2;
        String uselessFeatureName = data.get(idx).inputName;

        List<TypedData> outputs = new LinkedList<>();
        TypedData o = new TypedData();
        o.components = null;
        o.value = data.stream().filter(typedData -> !typedData.inputName.equals(uselessFeatureName))
                .map(typedData -> Double.parseDouble(String.valueOf(typedData.value))).reduce(Double::sum).get();
        o.typeRef = "number";
        o.inputName = "feature-" + idx;
        outputs.add(o);

        String modelName = "sum";

        LIMEishSaliencyExplanationProvider lime = new LIMEishSaliencyExplanationProvider(1000) {
            @Override
            protected List<PredictionOutput> predict(List<PredictionInput> perturbedInputs, List<TypedData> originalInput,
                                                     List<TypedData> originalOutputs, String modelName) {
                return LIMESumModelTest.this.predict(perturbedInputs, idx);
            }
        };

        Saliency saliency = lime.explain(inputs, outputs, modelName);

        assertNotNull(saliency);
        List<FeatureImportance> perFeatureImportance = saliency.getPerFeatureImportance();

        perFeatureImportance.sort((t1, t2) -> (int) (t2.getScore() - t1.getScore()));
        assertTrue(perFeatureImportance.get(0).getScore() > 0);
        assertTrue(perFeatureImportance.get(1).getScore() > 0);
        assertEquals(uselessFeatureName, perFeatureImportance.get(2).getFeature().getName());
    }

    private List<PredictionOutput> predict(List<PredictionInput> perturbedInputs, int idx) {
        List<PredictionOutput> predictionOutputs = new LinkedList<>();
        for (PredictionInput predictionInput : perturbedInputs) {
            List<Feature> features = predictionInput.getFeatures();
            double result = 0;
            for (int i = 0; i < features.size(); i++) {
                if (idx != i) {
                    result += features.get(i).getValue().asNumber();
                }
            }
            PredictionOutput predictionOutput = new PredictionOutput(
                    List.of(new Output("sum-but" + idx, Type.NUMBER, new Value<>(result), 1d)));
            predictionOutputs.add(predictionOutput);
        }
        return predictionOutputs;
    }
}