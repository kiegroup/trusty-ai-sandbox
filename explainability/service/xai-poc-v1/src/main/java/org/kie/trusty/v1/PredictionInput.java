package org.kie.trusty.v1;

import java.util.List;

/**
 * The input of a prediction
 */
public interface PredictionInput {

    /**
     * Turn the input into a list of features (each having name, type, value, etc.)
     * @return the prediction output in the form of a list of features
     */
    List<Feature> asFeatureList();

    /**
     * Turn the input into an array of floating-point numbers
     * @return the prediction inputs in the form of an array of double
     */
    double[] asDoubles();

}
