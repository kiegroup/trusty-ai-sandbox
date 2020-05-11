package com.redhat.developer.utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinearModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private double[] weights;
    private final boolean classification;
    private final double threshold;

    public LinearModel(int noOfWeights) {
        this(noOfWeights, false, 0);
    }

    public LinearModel(int size, boolean classification, double threshold) {
        this.weights = new double[size];
        this.threshold = threshold;
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < weights.length; i++) {
            this.weights[i] = 1f / (double) secureRandom.nextInt(100);
        }
        this.classification = classification;
    }

    public void fit(Collection<Prediction> trainingData) {
        assert !trainingData.isEmpty() : "cannot fit on an empty dataset";

        double loss;
        for (int e = 0; e < 3; e++) {
            int i = 0;
            for (Prediction prediction : trainingData) {
                PredictionInput input = prediction.getInput();
                PredictionOutput output = prediction.getOutput();
                double[] doubles = DataUtils.toNumbers(input);
                double predictedOutput = predict(doubles);
                double targetOutput = DataUtils.toNumbers(output)[0]; // assume the output has always one element (by previous label encoding construction)
                double diff = targetOutput - predictedOutput;
                loss = Math.max(0, 1 - targetOutput * predictedOutput);
                if (diff != 0) { // avoid null update to save computation
                    weights = DoubleStream.of(weights).map(d -> d + 1e-5 * diff).map(d -> Double.isNaN(d) ? 0d : d).toArray();
                }
                i++;
                if (i == trainingData.size()) {
                    logger.info("loss: {}", loss);
                }
            }
        }
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
