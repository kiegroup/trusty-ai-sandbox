package org.kie.trusty.m2x.model;

import java.util.Collections;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The output of a prediction
 */
@Schema(name="PredictionOutput")
public class PredictionOutput {

    private final List<Output> outputs;

    public PredictionOutput(List<Output> outputs) {
        this.outputs = Collections.unmodifiableList(outputs);
    }

    public List<Output> getOutputs() {
        return outputs;
    }
}
