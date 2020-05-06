package org.kie.trusty.m2x.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The prediction generated by a model
 */
@Schema(name="Prediction")
public class Prediction {

    private final ModelInfo modelInfo;

    private final PredictionInput input;

    private final PredictionOutput output;

    public Prediction(ModelInfo modelInfo, PredictionInput input, PredictionOutput output) {
        this.modelInfo = modelInfo;
        this.input = input;
        this.output = output;
    }

    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    public PredictionInput getInput() {
        return input;
    }

    public PredictionOutput getOutput() {
        return output;
    }

}
