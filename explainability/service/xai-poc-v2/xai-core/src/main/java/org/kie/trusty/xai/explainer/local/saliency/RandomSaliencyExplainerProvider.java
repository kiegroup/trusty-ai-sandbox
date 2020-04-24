package org.kie.trusty.xai.explainer.local.saliency;

import java.math.BigDecimal;
import java.security.SecureRandom;

import org.kie.trusty.xai.model.Feature;
import org.kie.trusty.xai.model.FeatureImportance;
import org.kie.trusty.xai.model.Prediction;
import org.kie.trusty.xai.model.Saliency;

public class RandomSaliencyExplainerProvider implements SaliencyLocalExplanationProvider {

    @Override
    public Saliency explain(Prediction prediction) {
        SecureRandom sr = new SecureRandom();
        Saliency saliency = new Saliency();
        for (Feature f : prediction.getInput().getFeatures()) {
            double value = sr.nextFloat();
            if (sr.nextBoolean()) {
                value *= -1f;
            }

            FeatureImportance featureImportance = new FeatureImportance();
            featureImportance.setFeature(f);
            featureImportance.setScore(BigDecimal.valueOf(value));
            saliency.addFeatureImportancesItem(featureImportance);
        }
        return saliency;
    }
}
