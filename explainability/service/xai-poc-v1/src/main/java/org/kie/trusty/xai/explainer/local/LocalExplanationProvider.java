package org.kie.trusty.xai.explainer.local;

import org.kie.trusty.v1.Prediction;

public interface LocalExplanationProvider<T> {

    T explain(Prediction prediction);
}
