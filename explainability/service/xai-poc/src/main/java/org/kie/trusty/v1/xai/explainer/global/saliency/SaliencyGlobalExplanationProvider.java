package org.kie.trusty.v1.xai.explainer.global.saliency;

import org.kie.trusty.v1.ModelInfo;
import org.kie.trusty.v1.xai.explainer.global.GlobalExplanationProvider;
import org.kie.trusty.v1.xai.explainer.Saliency;

public interface SaliencyGlobalExplanationProvider extends GlobalExplanationProvider<Saliency> {

    Saliency explain(ModelInfo modelInfo);

}
