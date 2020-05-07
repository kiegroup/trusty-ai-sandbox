package com.redhat.developer.explainability;

import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.explainability.model.LimeResponse;
import com.redhat.developer.explainability.model.Saliency;
import com.redhat.developer.explainability.responses.local.DecisionExplanationResponse;

public interface IExplainabilityService {
    DecisionExplanationResponse lime(String decisionId);

    Saliency getFeatureImportace(String decisionId);

    boolean processExecution(DMNResultModel execution);
}
