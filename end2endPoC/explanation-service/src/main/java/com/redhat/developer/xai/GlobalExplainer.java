package com.redhat.developer.xai;

import com.redhat.developer.model.Model;
import com.redhat.developer.model.Prediction;

public interface GlobalExplainer<T> {

    T explain(Model model);
}
