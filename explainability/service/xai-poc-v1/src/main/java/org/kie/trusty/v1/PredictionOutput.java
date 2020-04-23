package org.kie.trusty.v1;

/**
 * The output of a prediction
 */
public interface PredictionOutput {

    /**
     * Turn the output into an array of floating-point numbers
     * @return the prediction output in the form of an array of {@code double}
     */
    double[] asDoubles();
}
