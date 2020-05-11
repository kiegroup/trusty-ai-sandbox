package org.kie.trusty.xai.impl.explainer.utils;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.kie.trusty.m2x.model.Prediction;
import org.kie.trusty.m2x.model.PredictionInput;
import org.kie.trusty.m2x.model.PredictionOutput;

/**
 * Simple linear classifier
 */
public class LinearClassifier {

    private double[] weights;

    public LinearClassifier(int noOfWeights) {
        weights = new double[noOfWeights];
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1f / (double) secureRandom.nextInt(100);
        }
    }

    public void fit(Collection<Prediction> trainingData) {
        for (Prediction prediction : trainingData) {
            PredictionInput input = prediction.getPredictionInput();
            PredictionOutput output = prediction.getPredictionOutput();
            double predictedOutput = predict(input);
            double targetOutput = DoubleStream.of(DataUtils.toNumbers(output)).sum();
            double diff = targetOutput - predictedOutput;
            if (diff != 0) {
                weights = DoubleStream.of(weights).map(d -> d + 1e-2 * diff).map(d -> Double.isNaN(d) ? 0d : d).toArray();
            }
        }
    }

    public double predict(PredictionInput input) {
        return IntStream.range(0, DataUtils.toNumbers(input).length).mapToDouble(i -> DataUtils.toNumbers(input)[i] * weights[i]).sum();
    }

    public double[] getWeights() {
        return weights;
    }
}
