package org.kie.trusty.v1.xai.explainer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.trusty.v1.Feature;

public class Saliency {

    private final Map<Feature, Double> saliencyMap;

    public Saliency(Map<Feature, Double> saliencyMap) {
        this.saliencyMap = Collections.unmodifiableMap(saliencyMap);
    }

    public Map<Feature, Double> asMap() {
        return saliencyMap;
    }

    public Map<Feature, Double> getTopFeatures(int k) {
        return saliencyMap.entrySet().stream().sorted((f0, f1) -> Double.compare(Math.abs(f1.getValue()), Math.abs(f0.getValue()))).limit(k).collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)
        );
    }
}
