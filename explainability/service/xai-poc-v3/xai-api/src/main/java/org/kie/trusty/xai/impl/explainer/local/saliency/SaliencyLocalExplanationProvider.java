package org.kie.trusty.xai.impl.explainer.local.saliency;

import org.kie.trusty.m2x.model.Prediction;
import org.kie.trusty.xai.impl.explainer.local.LocalExplanationProvider;
import org.kie.trusty.xai.model.Saliency;

public interface SaliencyLocalExplanationProvider extends LocalExplanationProvider<Saliency> {

    Saliency explain(Prediction prediction);

}
