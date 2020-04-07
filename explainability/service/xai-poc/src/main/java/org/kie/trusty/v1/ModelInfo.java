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
    URI getEndpoint();

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

}
