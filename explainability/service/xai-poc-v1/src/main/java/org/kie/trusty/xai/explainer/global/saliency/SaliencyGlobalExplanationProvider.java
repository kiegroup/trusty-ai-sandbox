package org.kie.trusty.xai.explainer.global.saliency;

import org.kie.trusty.v1.ModelInfo;
import org.kie.trusty.xai.explainer.global.GlobalExplanationProvider;
import org.kie.trusty.xai.explainer.Saliency;

public interface SaliencyGlobalExplanationProvider extends GlobalExplanationProvider<Saliency> {

    Saliency explain(ModelInfo modelInfo);

}
