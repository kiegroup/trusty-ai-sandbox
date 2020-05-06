package com.redhat.developer.explainability.storage;

import com.redhat.developer.explainability.model.Saliency;

public interface IExplanabilityStorageExtension {
    boolean storeExplanation(String decisionId, Saliency saliency);

    Saliency getExplanation(String decisionId);
}
