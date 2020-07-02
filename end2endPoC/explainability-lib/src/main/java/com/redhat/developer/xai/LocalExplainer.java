package com.redhat.developer.xai;

import com.redhat.developer.model.Model;
import com.redhat.developer.model.Prediction;

public interface LocalExplainer<T> {

    T explain(Prediction prediction, Model model);
}
