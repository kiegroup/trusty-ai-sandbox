package org.kie.trusty.xai.impl.explainer.local;

import org.kie.trusty.m2x.model.Prediction;

public interface LocalExplanationProvider<T> {

    T explain(Prediction prediction);
}
