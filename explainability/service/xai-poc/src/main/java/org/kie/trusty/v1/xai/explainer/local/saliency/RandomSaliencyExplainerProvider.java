package org.kie.trusty.v1.xai.explainer.local.saliency;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.kie.trusty.v1.Feature;
import org.kie.trusty.v1.Prediction;
import org.kie.trusty.v1.xai.explainer.Saliency;

public class RandomSaliencyExplainerProvider implements SaliencyLocalExplanationProvider {

    @Override
    public Saliency explain(Prediction prediction) {
        SecureRandom sr = new SecureRandom();
        Map<Feature, Double> map = new HashMap<>();
        for (Feature f : prediction.getInput().asFeatureList()) {
            double value = sr.nextFloat();
            if (sr.nextBoolean()) {
                value *= -1f;
            }

            map.put(f, value);
        }
        return new Saliency(map);
    }
}
