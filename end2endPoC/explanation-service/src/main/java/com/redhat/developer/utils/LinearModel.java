package com.redhat.developer.utils;

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
    private double bias;

    public LinearModel(int size, boolean classification, double[] sampleWeights) {
        this.sampleWeights = sampleWeights;
        this.bias = 0;
        this.weights = new double[size];
        this.classification = classification;
    }

    public void fit(Collection<Prediction> trainingData) {
        assert !trainingData.isEmpty() : "cannot fit on an empty dataset";

        double lr = 0.01;
        double floss = 1;
        int e = 0;
        while(floss > 0.1 && e < 15) {
            double loss = 0;
            int i = 0;
            for (Prediction prediction : trainingData) {
                PredictionInput input = prediction.getInput();
                PredictionOutput output = prediction.getOutput();
                double[] doubles = DataUtils.toNumbers(input);
                double predictedOutput = predict(doubles);
                double targetOutput = DataUtils.toNumbers(output)[0]; // assume the output has always one element (by previous label encoding construction)
                double diff = checkFinite(targetOutput - predictedOutput);
                if (diff != 0) { // avoid null updates to save computation
                    loss += Math.abs(diff) / trainingData.size();
                    for (int j = 0; j < weights.length; j++) {
                        double v = lr * diff * doubles[j];
                        if (trainingData.size() == sampleWeights.length) {
                            v *= sampleWeights[i];
                        }
                        v = checkFinite(v);
                        weights[j] += v;
                        bias += lr * diff * sampleWeights[i] ;
                    }
                }
                i++;
            }
            lr *= (1d / (1d + 0.01 * e)); // learning rate decay

            floss = loss;
            logger.debug("loss: {}", loss);
            e++;
        }
    }

    private double checkFinite(double diff) {
        if (Double.isNaN(diff) || Double.isInfinite(diff)) {
            diff = 0;
        }
        return diff;
    }

    private double predict(double[] input) {
        double linearCombination = bias + IntStream.range(0, input.length).mapToDouble(i -> input[i] * weights[i]).sum();
        if (classification) {
            linearCombination = linearCombination >= 0 ? 1 : 0;
        }
        return linearCombination;
    }

    public double[] getWeights() {
        return weights;
    }
}
