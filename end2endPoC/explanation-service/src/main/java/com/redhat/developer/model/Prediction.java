package com.redhat.developer.model;

public class Prediction {

//    private final ModelInfo modelInfo;

    private final PredictionInput input;

    private final PredictionOutput output;

    public Prediction(PredictionInput input,
                      PredictionOutput output) {
//        this.modelInfo = modelInfo;
        this.input = input;
        this.output = output;
    }

//    public ModelInfo getModelInfo() {
//        return modelInfo;
//    }

    public PredictionInput getInput() {
        return input;
    }

    public PredictionOutput getOutput() {
        return output;
    }

}
