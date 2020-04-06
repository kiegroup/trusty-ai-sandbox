package org.kie.trusty.v1.xai.explainer.local.saliency;

import org.kie.trusty.v1.Prediction;
import org.kie.trusty.v1.xai.ExplanationProvider;

public interface SaliencyExplanationProvider extends ExplanationProvider<Saliency> {

    Saliency explain(Prediction prediction);

}
