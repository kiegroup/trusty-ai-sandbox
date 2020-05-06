package org.kie.trusty.m2x.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Model")
public interface Model {

    /**
     * Perform predictions on a batch of inputs
     *
     * @param inputs the inputs to be passed to the underlying model
     * @return a prediction output for each input
     */
    List<PredictionOutput> predict(PredictionInput... inputs);

    /**
     * Get general information about the model
     *
     * @return information about the model
     */
    ModelInfo getInfo();
}
