package com.redhat.developer.execution.storage;

import java.util.stream.Collectors;

import com.redhat.developer.execution.storage.model.DMNDataModel;
import com.redhat.developer.execution.storage.model.DMNEventModel;
import com.redhat.developer.execution.storage.model.DMNResultModel;
import com.redhat.developer.execution.storage.model.DecisionEvaluationStatusModel;
import com.redhat.developer.execution.storage.model.DecisionResultModel;
import com.redhat.developer.kafka.messaging.dto.DMNEventDto;
import com.redhat.developer.kafka.messaging.dto.DMNResultDto;
import com.redhat.developer.kafka.messaging.dto.DecisionEvaluationStatusDto;
import com.redhat.developer.kafka.messaging.dto.DecisionResultDto;

import static com.redhat.developer.execution.storage.model.DecisionEvaluationStatusModel.EVALUATING;
import static com.redhat.developer.execution.storage.model.DecisionEvaluationStatusModel.FAILED;
import static com.redhat.developer.execution.storage.model.DecisionEvaluationStatusModel.NOT_EVALUATED;
import static com.redhat.developer.execution.storage.model.DecisionEvaluationStatusModel.SKIPPED;
import static com.redhat.developer.execution.storage.model.DecisionEvaluationStatusModel.SUCCEEDED;

public class ModelFactory {

    public static DMNEventModel fromKafkaCloudEvent(DMNEventDto event){
        DMNResultDto resultDto = event.data.result;
        DMNResultModel resultModel = new DMNResultModel();
        resultModel.modelNamespace = resultDto.modelNamespace;
        resultModel.modelName = resultDto.modelName;
        resultModel.executionDate = resultDto.evaluationDate;
        resultModel.executionId = resultDto.evaluationId;
        resultModel.decisions = resultDto.decisions.stream().map(x -> fromDecisionDto(x)).collect(Collectors.toList());

        DMNEventModel eventModel = new DMNEventModel();
        eventModel.id = event.id;
        eventModel.data = new DMNDataModel(resultModel);
        eventModel.specversion = event.specversion;
        eventModel.source = event.source;
        eventModel.type = event.type;
        return eventModel;
    }

    public static DecisionResultModel fromDecisionDto(DecisionResultDto decisionDto){
        DecisionResultModel model = new DecisionResultModel();
        model.decisionId = decisionDto.decisionId;
        model.decisionName = decisionDto.decisionName;
        model.hasErrors = decisionDto.hasErrors;
        model.result = decisionDto.result;
        model.messages = decisionDto.messages;
        model.evaluationStatus = fromEvaluationStatusDto(decisionDto.evaluationStatus);
        return model;
    }

    public static DecisionEvaluationStatusModel fromEvaluationStatusDto(DecisionEvaluationStatusDto evaluation){
        switch(evaluation){
            case FAILED:
                return FAILED;
            case EVALUATING:
                return EVALUATING;
            case SKIPPED:
                return SKIPPED;
            case SUCCEEDED:
                return SUCCEEDED;
            case NOT_EVALUATED:
                return NOT_EVALUATED;
            default:
                throw new IllegalArgumentException("Evaluation status not supported.");
        }
    }
}
