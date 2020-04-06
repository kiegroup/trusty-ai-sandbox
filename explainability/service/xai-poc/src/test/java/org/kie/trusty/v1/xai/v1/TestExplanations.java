package org.kie.trusty.v1.xai.v1;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.DoubleStream;

import org.junit.Test;
import org.kie.trusty.v1.Feature;
import org.kie.trusty.v1.FeatureValue;
import org.kie.trusty.v1.Model;
import org.kie.trusty.v1.ModelInfo;
import org.kie.trusty.v1.DummyModelRegistry;
import org.kie.trusty.v1.Prediction;
import org.kie.trusty.v1.PredictionInput;
import org.kie.trusty.v1.PredictionOutput;
import org.kie.trusty.v1.xai.builder.ExplanationProviderBuilder;
import org.kie.trusty.v1.xai.explainer.local.saliency.Saliency;
import org.kie.trusty.v1.xai.explainer.local.saliency.SaliencyExplanationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestExplanations {

    @Test
    public void testLocalSaliencyExplanation() {
        SecureRandom secureRandom = new SecureRandom();
        UUID uuid = UUID.randomUUID();
        Model model = createModel(secureRandom);
        DummyModelRegistry.registerModel(uuid, model);
        PredictionInput in = getInput(secureRandom);
        PredictionOutput out = getOutput(model, in);
        ModelInfo info = getModelInfo(uuid);
        Prediction prediction = new Prediction(info, in, out);
        SaliencyExplanationProvider explanationProvider = ExplanationProviderBuilder.newExplanationProviderBuilder()
                .local()
                .saliency()
                .lime()
                .build();
        Saliency saliency = explanationProvider.explain(prediction);
        Map<Feature, Double> topFeatures = saliency.getTopFeatures(3);
        assertFalse(topFeatures.isEmpty());
        assertEquals(3, topFeatures.size());
        double score = Double.MAX_VALUE;
        for (Map.Entry<Feature, Double> entry : topFeatures.entrySet()) {
            Double currentScore = entry.getValue();
            assertTrue(Math.abs(currentScore) < Math.abs(score));
            score = entry.getValue();
        }
    }

    private ModelInfo getModelInfo(UUID uuid) {
        ModelInfo info = mock(ModelInfo.class);
        when(info.getId()).thenReturn(uuid);
        return info;
    }

    private PredictionOutput getOutput(Model model, PredictionInput in) {
        PredictionOutput out = mock(PredictionOutput.class);
        double[] outputValues = model.predict(in.asDoubles());
        when(out.asDoubles()).thenReturn(outputValues);
        return out;
    }

    private PredictionInput getInput(SecureRandom secureRandom) {
        PredictionInput in = mock(PredictionInput.class);
        List<Feature> features = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Feature f = mock(Feature.class);
            FeatureValue fv = mock(FeatureValue.class);
            double t = secureRandom.nextDouble();
            when(fv.asDouble()).thenReturn(t);
            when(f.getValue()).thenReturn(fv);
            features.add(f);
        }
        double[] doubles = features.stream().mapToDouble(f -> f.getValue().asDouble()).toArray();
        when(in.asDoubles()).thenReturn(doubles);
        when(in.asFeatureList()).thenReturn(features);
        return in;
    }

    private Model createModel(SecureRandom secureRandom) {
        double fixedPoint = secureRandom.nextDouble();
        return inputs -> {
            double[] out = new double[2];
            double p1 = Math.abs((DoubleStream.of(inputs).average().getAsDouble() - fixedPoint) / fixedPoint);
            out[1] = p1;
            out[0] = 1 - p1;
            return out;
        };
    }
}
