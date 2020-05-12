package com.redhat.developer.utils;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.stream.IntStream;

import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinearModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final double[] weights;
    private final double[] sampleWeights;
    private final boolean classification;
    private final double threshold;

    public LinearModel(int size, boolean classification, int noOfWeights) {
        this(noOfWeights, false, 0, new double[0]);
    }

    public LinearModel(int size, boolean classification, double threshold, double[] sampleWeights) {
        this.sampleWeights = sampleWeights;
        this.weights = new double[size];
        this.threshold = threshold;
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < weights.length; i++) {
            this.weights[i] = 1f / (1d + secureRandom.nextInt(100));
        }
        this.classification = classification;
    }

    public void fit(Collection<Prediction> trainingData) {
        assert !trainingData.isEmpty() : "cannot fit on an empty dataset";

        for (int e = 0; e < 10; e++) {
            double loss = 0;
            int i = 0;
            for (Prediction prediction : trainingData) {
                PredictionInput input = prediction.getInput();
                PredictionOutput output = prediction.getOutput();
                double[] doubles = DataUtils.toNumbers(input);
                double predictedOutput = predict(doubles);
                double targetOutput = DataUtils.toNumbers(output)[0]; // assume the output has always one element (by previous label encoding construction)
                double diff = checkFinite(targetOutput - predictedOutput);
                loss += Math.max(0, 1 - checkFinite(targetOutput * predictedOutput)) / trainingData.size();
                if (diff != 0) { // avoid null update to save computation
                    for (int j = 0; j < weights.length; j++) {
                        double v = 1e-1 * diff * doubles[j];
                        if (trainingData.size() == sampleWeights.length) {
                            v *= sampleWeights[i];
                        }
                        v = checkFinite(v);
                        weights[j] += v;
                    }
                }
                i++;
            }
            logger.info("loss: {}", loss);
        }
    }

    private double checkFinite(double diff) {
        if (Double.isNaN(diff) || Double.isInfinite(diff)) {
            diff = 0;
        }
        return diff;
    }

    private double predict(double[] input) {
        double linearCombination = threshold + IntStream.range(0, input.length).mapToDouble(i -> input[i] * weights[i]).sum();
        if (classification) {
            linearCombination = linearCombination > 0 ? 1 : 0;
        }
        return linearCombination;
    }

    public double[] getWeights() {
        return weights;
    }
}
