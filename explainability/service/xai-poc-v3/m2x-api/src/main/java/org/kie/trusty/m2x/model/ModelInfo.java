package org.kie.trusty.m2x.model;

import java.net.URI;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Information about a certain model.
 * Different instances of the same model implementation will share the same values for this class, wherever different
 * versions or types or model implementations won't.
 */
@Schema(name="ModelInfo", description = "Various information associated to a model")
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
     * Get the type of task associated to the given model
     * @return a task type
     */
    TaskType getTaskType();

    enum TaskType {

        /**
         * Classification tasks
         */
        CLASSIFICATION,

        /**
         * Regression tasks
         */
        REGRESSION

    }

}
