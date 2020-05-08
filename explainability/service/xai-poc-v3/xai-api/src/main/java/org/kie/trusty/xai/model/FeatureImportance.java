package org.kie.trusty.xai.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kie.trusty.m2x.model.Feature;

@Schema
public class FeatureImportance {

    private Feature feature;
    private double score;

    public FeatureImportance() {
        this.feature = new Feature();
        this.score = 0;
    }

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

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
