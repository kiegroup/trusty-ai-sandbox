package org.kie.trusty.xai.explainer.local.saliency;

import org.kie.trusty.v1.Prediction;
import org.kie.trusty.xai.explainer.Saliency;
import org.kie.trusty.xai.explainer.local.LocalExplanationProvider;

public interface SaliencyLocalExplanationProvider extends LocalExplanationProvider<Saliency> {

    Saliency explain(Prediction prediction);

}
