package org.kie.trusty.xai.explainer.local.saliency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

import io.swagger.client.api.LocalApi;
import org.junit.Test;
import org.kie.trusty.xai.explainer.utils.DataUtils;
import org.kie.trusty.xai.model.Feature;
import org.kie.trusty.xai.model.ModelInfo;
import org.kie.trusty.xai.model.Output;
import org.kie.trusty.xai.model.Prediction;
import org.kie.trusty.xai.model.PredictionInput;
import org.kie.trusty.xai.model.PredictionOutput;
import org.kie.trusty.xai.model.Saliency;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LIMEishSaliencyExplanationProviderTest {

    @Test
    public void testSaliency() throws Exception {
        LocalApi api = new TestLocalApi();
        LIMEishSaliencyExplanationProvider limEishSaliencyExplanationProvider =
                new LIMEishSaliencyExplanationProvider(api, 10);
        Prediction prediction = mock(Prediction.class);
        ModelInfo modelInfo = mock(ModelInfo.class);
        when(prediction.getInfo()).thenReturn(modelInfo);
        PredictionInput input = mock(PredictionInput.class);
        List<Feature> features = new LinkedList<>();
        double[] inputs = DataUtils.generateSamples(0, 1, 10);
        for (double i : inputs) {
            Feature feature = mock(Feature.class);
            when(feature.getValue()).thenReturn(String.valueOf(i));
            features.add(feature);
        }
        when(input.getFeatures()).thenReturn(features);
        when(prediction.getInput()).thenReturn(input);
        Saliency saliency = limEishSaliencyExplanationProvider.explain(prediction);

        assertNotNull(saliency);
    }

    private static class TestLocalApi extends LocalApi {

        @Override
        public List<PredictionOutput> predict(List<PredictionInput> body) {
            List<PredictionOutput> outputs = new ArrayList<>(body.size());
            for (PredictionInput input : body) {
                int result = DoubleStream.of(DataUtils.toNumbers(input)).average().getAsDouble() > 0.5 ? 1 : 0;
                PredictionOutput output = new PredictionOutput();
                Output e1 = new Output();
                e1.setLabel(result == 1 ? "true" : "false");
                e1.setScore(new BigDecimal(result));
                output.setOutputs(List.of(e1));
                outputs.add(output);
            }
            return outputs;
        }
    }
}