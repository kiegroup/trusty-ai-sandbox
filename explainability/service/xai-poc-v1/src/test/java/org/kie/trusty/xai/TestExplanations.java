package org.kie.trusty.xai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.DoubleStream;

import org.junit.Test;
import org.kie.trusty.v1.DummyModelRegistry;
import org.kie.trusty.v1.Feature;
import org.kie.trusty.v1.FeatureValue;
import org.kie.trusty.v1.Model;
import org.kie.trusty.v1.ModelInfo;
import org.kie.trusty.v1.Prediction;
import org.kie.trusty.v1.PredictionInput;
import org.kie.trusty.v1.PredictionOutput;
import org.kie.trusty.xai.builder.ExplanationProviderBuilder;
import org.kie.trusty.xai.explainer.Saliency;
import org.kie.trusty.xai.explainer.global.viz.GlobalVizExplanationProvider;
import org.kie.trusty.xai.explainer.global.viz.TabularData;
import org.kie.trusty.xai.explainer.local.saliency.SaliencyLocalExplanationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestExplanations {

    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Test
    public void testLocalSaliencyExplanation() {
        UUID uuid = UUID.randomUUID();
        Model model = createDummyTestModel();
        DummyModelRegistry.registerModel(uuid, model);
        PredictionInput in = getInput();
        PredictionOutput out = getOutput(model, in);
        ModelInfo info = getModelInfo(uuid, model);
        Prediction prediction = new Prediction(info, in, out);
        SaliencyLocalExplanationProvider explanationProvider = ExplanationProviderBuilder.newExplanationProviderBuilder()
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
            assertTrue(Math.abs(currentScore) <= Math.abs(score));
            score = entry.getValue();
        }
    }

    @Test
    public void testGlobalTabularExplanation() throws FileNotFoundException {
        UUID uuid = UUID.randomUUID();
        Model model = createDummyTestModel();
        DummyModelRegistry.registerModel(uuid, model);
        ModelInfo info = getModelInfo(uuid, model);
        int featureIndex = SECURE_RANDOM.nextInt(info.getInputShape());
        int outputIndex = SECURE_RANDOM.nextInt(info.getOutputShape());
        GlobalVizExplanationProvider explanationProvider = ExplanationProviderBuilder.newExplanationProviderBuilder()
                .global()
                .partialDependence()
                .onFeature(featureIndex)
                .onOutput(outputIndex)
                .build();
        TabularData tabularData = explanationProvider.explain(info);
        assertNotNull(tabularData);
        writeAsciiGraph(tabularData, new PrintWriter(new File("target/pdp.txt")));
    }

    private void writeAsciiGraph(TabularData tabularData, PrintWriter out) {
        double curMax = 1 + DoubleStream.of(tabularData.getY()).max().getAsDouble();
        int tempIdx = -1;
        for (int k = 0; k < tabularData.getX().length; k++) {
            double tempMax = -Integer.MAX_VALUE;
            for (int j = 0; j < tabularData.getY().length; j++) {
                double v = tabularData.getY()[j];
                if (v < curMax && v > tempMax && tempIdx != j) {
                    tempMax = v;
                    tempIdx = j;
                }
            }
            writeDot(tabularData, tempIdx, out);
            curMax = tempMax;
        }
        out.flush();
        out.close();
    }

    private void writeDot(TabularData data, int i, PrintWriter out) {
        for (int j = 0; j < data.getX()[i]; j++) {
            out.print(" ");
        }
        out.println("*");
    }

    private ModelInfo getModelInfo(UUID uuid, Model model) {
        ModelInfo info = mock(ModelInfo.class);
        when(info.getId()).thenReturn(uuid);
        PredictionInput input = getInput();
        double[] doubles = input.asDoubles();
        when(info.getInputShape()).thenReturn(doubles.length);
        PredictionOutput output = getOutput(model, input);
        double[] doubles1 = output.asDoubles();
        when(info.getOutputShape()).thenReturn(doubles1.length);
        ModelInfo.DataDistribution dataDistribution = mock(ModelInfo.DataDistribution.class);
        for (int i = 0; i < 10; i++) {
            when(dataDistribution.getMax(i)).thenReturn(SECURE_RANDOM.nextDouble());
            when(dataDistribution.getMean(i)).thenReturn(SECURE_RANDOM.nextDouble());
            when(dataDistribution.getStdDeviation(i)).thenReturn(SECURE_RANDOM.nextDouble());
            when(dataDistribution.getMin(i)).thenReturn(SECURE_RANDOM.nextDouble());
        }
        when(info.getTrainingDataDistribution()).thenReturn(dataDistribution);
        return info;
    }

    private PredictionOutput getOutput(Model model, PredictionInput in) {
        PredictionOutput out = mock(PredictionOutput.class);
        double[] outputValues = model.predict(in.asDoubles());
        when(out.asDoubles()).thenReturn(outputValues);
        return out;
    }

    private PredictionInput getInput() {
        PredictionInput in = mock(PredictionInput.class);
        List<Feature> features = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Feature f = mock(Feature.class);
            FeatureValue fv = mock(FeatureValue.class);
            double t = SECURE_RANDOM.nextDouble();
            when(fv.asDouble()).thenReturn(t);
            when(f.getValue()).thenReturn(fv);
            features.add(f);
        }
        double[] doubles = features.stream().mapToDouble(f -> f.getValue().asDouble()).toArray();
        when(in.asDoubles()).thenReturn(doubles);
        when(in.asFeatureList()).thenReturn(features);
        return in;
    }

    private Model createDummyTestModel() {
        double fixedPoint = SECURE_RANDOM.nextDouble();
        return inputs -> {
            double[] out = new double[2];
            double p1 = Math.abs((DoubleStream.of(inputs).average().getAsDouble() - fixedPoint) / fixedPoint);
            out[1] = p1;
            out[0] = 1 - p1;
            return out;
        };
    }
}
