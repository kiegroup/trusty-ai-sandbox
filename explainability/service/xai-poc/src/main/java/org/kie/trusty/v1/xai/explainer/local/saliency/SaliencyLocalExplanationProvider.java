package org.kie.trusty.v1.xai.explainer.local.saliency;

import org.kie.trusty.v1.Prediction;
import org.kie.trusty.v1.xai.explainer.Saliency;
import org.kie.trusty.v1.xai.explainer.local.LocalExplanationProvider;

public interface SaliencyLocalExplanationProvider extends LocalExplanationProvider<Saliency> {

    Saliency explain(Prediction prediction);

}
