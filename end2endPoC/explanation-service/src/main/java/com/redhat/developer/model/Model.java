package com.redhat.developer.model;

import java.util.List;

public interface Model {

    List<PredictionOutput> predict(List<PredictionInput> inputs);

    DataDistribution getDataDistribution();

    PredictionInput getInputShape();

    PredictionOutput getOutputShape();
}
