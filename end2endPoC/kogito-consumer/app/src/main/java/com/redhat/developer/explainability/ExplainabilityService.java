package com.redhat.developer.explainability;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.execution.IExecutionService;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.responses.decisions.inputs.DecisionStructuredInputsResponse;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;
import com.redhat.developer.execution.storage.ExecutionsStorageExtension;
import com.redhat.developer.explainability.model.LimeExplanationRequest;
import com.redhat.developer.explainability.model.LimeResponse;
import com.redhat.developer.explainability.model.Saliency;
import com.redhat.developer.explainability.responses.local.DecisionExplanationResponse;
import com.redhat.developer.explainability.storage.IExplanabilityStorageExtension;
import com.redhat.developer.utils.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ExplainabilityService implements IExplainabilityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExplainabilityService.class);

    private static final HttpHelper httpHelper = new HttpHelper("http://explanability:1338/");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    IExecutionService executionService;
    
    @Inject
    IExplanabilityStorageExtension storageExtension;

    @Override
    public DecisionExplanationResponse lime(String decisionId) {
        return null;
    }

    @Override
    public Saliency getFeatureImportace(String decisionId) {
        return storageExtension.getExplanation(decisionId);
    }

    @Override
    public boolean processExecution(DMNResultModel execution) {

        List<SingleDecisionInputResponse> structuredInputs = executionService.getStructuredInputs(execution).input;
        List<SingleDecisionInputResponse> structuredOutcomes = executionService.getStructuredOutcomesValues(execution);
        LimeExplanationRequest request = new LimeExplanationRequest(structuredInputs, structuredOutcomes, execution.modelName);
        String response = null;
        try {
            response = httpHelper.doPost("xai/saliency/lime", objectMapper.writeValueAsString(request));
        } catch (IOException e) {
            LOGGER.error("Something went wrong in the communication with the explanability service: ", e);
            return false;
        }

        LimeResponse limeResponse = null;
        try {
            limeResponse = objectMapper.readValue(response, LimeResponse.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Can't parse the explanability response. ", e);
            return false;
        }

        Saliency saliency = null; // create dto

        storageExtension.storeExplanation(execution.executionId, saliency);

        return true;
    }
}
