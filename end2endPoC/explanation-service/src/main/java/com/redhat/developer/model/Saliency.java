package com.redhat.developer.model;

import java.util.List;
import java.util.stream.Collectors;

public class Saliency {

    private final List<FeatureImportance> perFeatureImportance;

    public Saliency(List<FeatureImportance> perFeatureImportance, int k) {
        this.perFeatureImportance = perFeatureImportance.stream().sorted((f0, f1) -> Double.compare(
                Math.abs(f1.getScore()), Math.abs(f0.getScore()))).limit(k).collect(Collectors.toList());
    }

    public Saliency(List<FeatureImportance> perFeatureImportance) {
        this.perFeatureImportance = perFeatureImportance;
    }

    public List<FeatureImportance> getPerFeatureImportance() {
        return perFeatureImportance;
    }

    public List<FeatureImportance> getTopFeatures(int k) {
        return perFeatureImportance.stream().sorted((f0, f1) -> Double.compare(
                Math.abs(f1.getScore()), Math.abs(f0.getScore()))).limit(k).collect(Collectors.toList());
    }
}

