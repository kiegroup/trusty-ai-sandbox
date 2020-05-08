package com.redhat.developer.explainability;

import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.explainability.model.Saliency;

public interface IExplainabilityService {
    Saliency getFeatureImportance(String decisionId);

    boolean processExecution(DMNResultModel execution);
}
