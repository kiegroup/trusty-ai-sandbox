package com.redhat.developer.model;

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
