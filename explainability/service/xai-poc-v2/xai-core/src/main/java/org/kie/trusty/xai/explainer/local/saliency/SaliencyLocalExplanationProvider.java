package org.kie.trusty.xai.explainer.local.saliency;

import org.kie.trusty.xai.explainer.local.LocalExplanationProvider;
import org.kie.trusty.xai.model.Prediction;
import org.kie.trusty.xai.model.Saliency;

public interface SaliencyLocalExplanationProvider extends LocalExplanationProvider<Saliency> {

    Saliency explain(Prediction prediction);

}
