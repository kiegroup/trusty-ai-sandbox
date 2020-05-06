package org.kie.trusty.xai.impl.explainer.global;

import org.kie.trusty.m2x.model.ModelInfo;

public interface GlobalExplanationProvider<T> {

    T explain(ModelInfo modelInfo);

}
