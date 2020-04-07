package org.kie.trusty.v1.xai;

import org.kie.trusty.v1.Prediction;

public interface ExplanationProvider<T> {

    T explain(Prediction prediction);
}
