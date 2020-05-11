package org.kie.trusty.xai.impl.explainer.local.saliency;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

import org.junit.Test;
import org.kie.trusty.m2x.api.LocalApi;
import org.kie.trusty.m2x.model.Feature;
import org.kie.trusty.m2x.model.ModelInfo;
import org.kie.trusty.m2x.model.Output;
import org.kie.trusty.m2x.model.Prediction;
import org.kie.trusty.m2x.model.PredictionInput;
import org.kie.trusty.m2x.model.PredictionOutput;
import org.kie.trusty.m2x.model.Type;
import org.kie.trusty.m2x.model.Value;
import org.kie.trusty.xai.impl.explainer.utils.DataUtils;
import org.kie.trusty.xai.model.FeatureImportance;
import org.kie.trusty.xai.model.Saliency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LIMEishSaliencyExplanationProviderTest {

    @Test
    public void testSaliency() {
        LocalApi api = new TestLocalApi();
        LIMEishSaliencyExplanationProvider limEishSaliencyExplanationProvider =
                new LIMEishSaliencyExplanationProvider(api, 10);
        Prediction prediction = mock(Prediction.class);
        ModelInfo modelInfo = mock(ModelInfo.class);
        when(modelInfo.getPredictionEndpoint()).thenReturn(URI.create("/path/to/model"));
        when(prediction.getModelInfo()).thenReturn(modelInfo);
        PredictionInput input = mock(PredictionInput.class);
        List<Feature> features = new LinkedList<>();
        double[] inputs = DataUtils.generateSamples(0, 1, 10);
        for (double i : inputs) {
            Feature feature = mock(Feature.class);
            when(feature.getValue()).thenReturn(new Value<>(i));
            when(feature.getType()).thenReturn(Type.NUMBER);
            features.add(feature);
        }
        when(input.getFeatures()).thenReturn(features);
        when(prediction.getPredictionInput()).thenReturn(input);
        Saliency saliency = limEishSaliencyExplanationProvider.explain(prediction);
        assertNotNull(saliency);
        List<FeatureImportance> topFeatures = saliency.getTopFeatures(3);
        assertFalse(topFeatures.isEmpty());
        assertEquals(3, topFeatures.size());
        double score = Double.MAX_VALUE;
        for (FeatureImportance featureImportance : topFeatures) {
            double currentScore = featureImportance.getScore();
            assertTrue(Math.abs(currentScore) <= Math.abs(score));
            score = currentScore;
        }
    }

    private static class TestLocalApi extends LocalApi {

        @Override
        public List<PredictionOutput> predict(List<PredictionInput> body) {
            List<PredictionOutput> predictionOutputs = new ArrayList<>(body.size());
            for (PredictionInput input : body) {
                int result = DoubleStream.of(DataUtils.toNumbers(input)).average().getAsDouble() > 0.5 ? 1 : 0;
                predictionOutputs.add(new PredictionOutput(List.of(new Output(new Value<>(result), Type.NUMBER, result))));
            }
            return predictionOutputs;
        }
    }
}