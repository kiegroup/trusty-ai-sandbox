package com.redhat.developer.execution;

import java.util.List;

import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.models.OutcomeModelWithInputs;
import com.redhat.developer.execution.responses.decisions.inputs.DecisionStructuredInputsResponse;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;

public interface IExecutionService {
    DecisionStructuredInputsResponse getStructuredInputs(DMNResultModel event);

    OutcomeModelWithInputs getStructuredOutcome(String outcomeId, DMNResultModel event);

    List<SingleDecisionInputResponse> getStructuredOutcomesValues(DMNResultModel event);
}
