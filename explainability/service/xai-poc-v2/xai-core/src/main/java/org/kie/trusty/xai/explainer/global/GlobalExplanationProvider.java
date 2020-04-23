package org.kie.trusty.xai.explainer.global;

import org.kie.trusty.xai.model.ModelInfo;

public interface GlobalExplanationProvider<T> {

    T explain(ModelInfo modelInfo);

}
