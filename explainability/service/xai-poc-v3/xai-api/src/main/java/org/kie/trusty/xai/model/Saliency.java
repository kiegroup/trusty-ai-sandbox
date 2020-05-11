package org.kie.trusty.xai.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class Saliency {

    private List<FeatureImportance> perFeatureImportance;

    public Saliency() {
        this.perFeatureImportance = Collections.emptyList();
    }

    public Saliency(List<FeatureImportance> perFeatureImportance) {
        this.perFeatureImportance = perFeatureImportance;
    }

    public List<FeatureImportance> getPerFeatureImportance() {
        return perFeatureImportance;
    }

    public void setPerFeatureImportance(List<FeatureImportance> perFeatureImportance) {
        this.perFeatureImportance = perFeatureImportance;
    }

    public List<FeatureImportance> getTopFeatures(int k) {
        return perFeatureImportance.stream().sorted((f0, f1) -> Double.compare(
                Math.abs(f1.getScore()), Math.abs(f0.getScore()))).limit(k).collect(Collectors.toList());
    }
}
