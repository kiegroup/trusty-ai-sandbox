package org.kie.trusty.v1.xai.explainer.local;

import org.kie.trusty.v1.Prediction;

public interface LocalExplanationProvider<T> {

    T explain(Prediction prediction);
}
