package org.kie.trusty.xai.impl.explainer.global.saliency;

import org.kie.trusty.m2x.model.ModelInfo;
import org.kie.trusty.xai.impl.explainer.global.GlobalExplanationProvider;
import org.kie.trusty.xai.model.Saliency;

public interface SaliencyGlobalExplanationProvider extends GlobalExplanationProvider<Saliency> {

    Saliency explain(ModelInfo modelInfo);
}
