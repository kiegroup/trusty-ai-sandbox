package org.kie.trusty.xai.explainer.local;

import org.kie.trusty.xai.model.Prediction;

public interface LocalExplanationProvider<T> {

    T explain(Prediction prediction);
}
