package org.kie.trusty.xai.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kie.trusty.m2x.model.Feature;

@Schema
public class FeatureImportance {

    private final Feature feature;
    private final double score;

    public FeatureImportance(Feature feature, double score) {
        this.feature = feature;
        this.score = score;
    }

    public Feature getFeature() {
        return feature;
    }

    public double getScore() {
        return score;
    }
}
