package com.redhat.developer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.developer.requests.TypedData;
import org.json.JSONObject;

public class PredictionInput {

    private final List<Feature> features;

    public PredictionInput( List<Feature> features) {
        this.features = features;
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
