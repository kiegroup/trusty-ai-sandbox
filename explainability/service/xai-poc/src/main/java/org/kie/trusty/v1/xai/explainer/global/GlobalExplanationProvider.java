package org.kie.trusty.v1.xai.explainer.global;

import org.kie.trusty.v1.ModelInfo;

public interface GlobalExplanationProvider<T> {

    T explain(ModelInfo modelInfo);

}
