package com.redhat.developer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.developer.requests.TypedData;
import org.json.JSONObject;

public class PredictionOutput {

    private final List<Output> outputs;

    public PredictionOutput(List<Output> outputs) {
        this.outputs = outputs;
    }

    public List<Output> getOutputs() {
        return outputs;
    }
}
