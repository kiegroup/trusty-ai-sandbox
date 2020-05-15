package com.redhat.developer.explainability;

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.execution.IExecutionService;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;
import com.redhat.developer.explainability.model.FeatureImportance;
import com.redhat.developer.explainability.model.LimeExplanationRequest;
import com.redhat.developer.explainability.model.Saliency;
import com.redhat.developer.explainability.storage.IExplanabilityStorageExtension;
import com.redhat.developer.utils.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ExplainabilityService implements IExplainabilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExplainabilityService.class);

    private static final HttpHelper httpHelper = new HttpHelper("http://explanation-service:1338");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    IExecutionService executionService;

    @Inject
    IExplanabilityStorageExtension storageExtension;

    @Override
    public Saliency getFeatureImportance(String decisionId) {
        return storageExtension.getExplanation(decisionId);
    }

    @Override
    public boolean processExecution(DMNResultModel execution) {
        List<SingleDecisionInputResponse> structuredInputs = executionService.getStructuredInputs(execution).input;
        List<SingleDecisionInputResponse> structuredOutcomes = executionService.getStructuredOutcomesValues(execution).subList(0, 1);
        LimeExplanationRequest request = new LimeExplanationRequest(structuredInputs, structuredOutcomes, execution.modelName);
        String response = null;
        try {
            LOGGER.info(objectMapper.writeValueAsString(request));
            response = httpHelper.doPost("/xai/saliency/lime", objectMapper.writeValueAsString(request));
        } catch (IOException e) {
            LOGGER.error("Something went wrong in the communication with the explanability service: ", e);
            return false;
        }

        Saliency saliency = null;
        try {
            saliency = objectMapper.readValue(response, Saliency.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Can't parse the explanability response. ", e);
            return false;
        }

        saliency.executionId = execution.executionId;

        for (FeatureImportance fi : saliency.featureImportance){
            fi.featureScore = roundNumber(fi.featureScore);
        }

        storageExtension.storeExplanation(execution.executionId, saliency);

        return true;
    }

    private Double roundNumber(Double number) {
        return Double.valueOf(String.format("%.2f", number));
    }
}
