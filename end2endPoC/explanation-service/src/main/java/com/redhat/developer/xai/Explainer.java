package com.redhat.developer.xai;

import com.redhat.developer.model.Model;
import com.redhat.developer.model.Prediction;

public interface Explainer<T> {

    T explain(Prediction predictions, Model model);
}
