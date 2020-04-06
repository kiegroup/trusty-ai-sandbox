package org.kie.trusty.v1.xai.explainer.local.saliency.utils;

import java.security.SecureRandom;
import java.util.List;

import org.kie.trusty.v1.Feature;
import org.kie.trusty.v1.Prediction;
import org.kie.trusty.v1.PredictionInput;
import org.kie.trusty.v1.PredictionOutput;

public class DataUtils {

    private static final SecureRandom random = new SecureRandom();

    public static double[] perturbDrop(double[] data) {
        double[] perturbed = new double[data.length];
        System.arraycopy(data, 0, perturbed, 0, data.length);
        for (int j = 0; j < random.nextInt(data.length / 2); j++) {
            perturbed[random.nextInt(data.length)] = 0;
        }
        return perturbed;
    }

    public static PredictionInput inputFrom(double[] doubles) {
        return new PredictionInput() {
            @Override
            public List<Feature> asFeatureList() {
                return null;
            }

            @Override
            public double[] asDoubles() {
                return doubles;
            }
        };
    }

    public static PredictionOutput outputFrom(double[] doubles) {
        return () -> doubles;
    }
}
