package org.kie.trusty.v1;

import java.net.URI;
import java.util.UUID;

/**
 * Information about a certain model.
 * Different instances of the same model implementation will share the same values for this class, wherever different
 * versions or types or model implementations won't.
 */
public interface ModelInfo {

    /**
     * the universal identifier of the model
     * @return the UUID of the model
     */
    UUID getId();

    /**
     * a descriptive name of the model
     * @return the model name
     */
    String getName();

    /**
     * the version of the model, can be an alphanumerical String
     * @return the model version
     */
    String getVersion();

    /**
     * The endpoint that can be used in order to access the service associated to the given model
     * @return the endpoint URI
     */
    URI getPredictionEndpoint();

    URI getTrainingEndpoint();

    URI getTrainingDataURI();

    /**
     * Get data distribution information about the data used to train the model
     * @return the data distribution
     */
    DataDistribution getTrainingDataDistribution();

    /**
     * Get a sample input 'shape'
     * @return a synthetic prediction input
     */
    PredictionInput getInputShape();

    /**
     * Get a sample output 'shape'
     * @return a synthetic prediction output
     */
    PredictionOutput getOutputShape();

    /**
     * Information about distribution of data using for training a model
     */
    interface DataDistribution {

        /**
         * get mean value for a feature at a certain index
         * @param featureIndex the feature index
         * @return the feature mean value
         */
        double getMean(int featureIndex);

        /**
         * get the standard deviation for a feature at a certain index
         * @param featureIndex the feature index
         * @return the feature standard deviation
         */
        double getStdDeviation(int featureIndex);

        /**
         * get the minimum value for a feature at a certain index
         * @param featureIndex the feature index
         * @return the feature minimum value
         */
        double getMin(int featureIndex);

        /**
         * get the maximum value for a feature at a certain index
         * @param featureIndex the feature index
         * @return the feature maximum value
         */
        double getMax(int featureIndex);

    }
}
