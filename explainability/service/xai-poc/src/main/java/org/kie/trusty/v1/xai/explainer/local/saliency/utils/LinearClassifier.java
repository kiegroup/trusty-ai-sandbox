package org.kie.trusty.v1.xai.explainer.local.saliency.utils;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.kie.trusty.v1.Prediction;
import org.kie.trusty.v1.PredictionInput;
import org.kie.trusty.v1.PredictionOutput;

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
            PredictionInput input = prediction.getInput();
            PredictionOutput output = prediction.getOutput();
            double predictedOutput = predict(input);
            double targetOutput = DoubleStream.of(output.asDoubles()).sum();
            double diff = targetOutput - predictedOutput;
            if (diff != 0) {
                weights = DoubleStream.of(weights).map(d -> d + 1e-2 * diff).toArray();
            }
        }
    }

    public double predict(PredictionInput input) {
        return IntStream.range(0, input.asDoubles().length).mapToDouble(i -> input.asDoubles()[i] * weights[i]).sum();
    }

    public double[] getWeights() {
        return weights;
    }
}
