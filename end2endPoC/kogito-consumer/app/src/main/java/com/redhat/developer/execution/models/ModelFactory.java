package com.redhat.developer.execution.models;

import java.util.stream.Collectors;

import com.redhat.developer.dmn.utils.MyMD5;
import com.redhat.developer.kafka.messaging.dto.DMNEventDto;
import com.redhat.developer.kafka.messaging.dto.DMNResultDto;
import com.redhat.developer.kafka.messaging.dto.DecisionEvaluationStatusDto;
import com.redhat.developer.kafka.messaging.dto.DecisionResultDto;

import static com.redhat.developer.execution.models.OutcomeEvaluationStatusModel.EVALUATING;
import static com.redhat.developer.execution.models.OutcomeEvaluationStatusModel.FAILED;
import static com.redhat.developer.execution.models.OutcomeEvaluationStatusModel.NOT_EVALUATED;
import static com.redhat.developer.execution.models.OutcomeEvaluationStatusModel.SKIPPED;
import static com.redhat.developer.execution.models.OutcomeEvaluationStatusModel.SUCCEEDED;

public class ModelFactory {

    public static DMNEventModel fromKafkaCloudEvent(DMNEventDto event) {
        DMNResultDto resultDto = event.data.result;
        DMNResultModel resultModel = new DMNResultModel();
        resultModel.modelNamespace = resultDto.modelNamespace;
        resultModel.modelName = resultDto.modelName;
        resultModel.executionDate = resultDto.evaluationDate;
        resultModel.executionId = resultDto.evaluationId;
        resultModel.context = resultDto.context;
        resultModel.decisions = resultDto.decisions.stream().map(x -> fromDecisionDto(x)).collect(Collectors.toList());
        //resultModel.modelId = MyMD5.getMd5(resultModel.modelNamespace + resultModel.modelName);
        resultModel.modelId = MyMD5.getMd5("provaname");

        DMNEventModel eventModel = new DMNEventModel();
        eventModel.id = event.id;
        eventModel.data = new DMNDataModel(resultModel);
        eventModel.specversion = event.specversion;
        eventModel.source = event.source;
        eventModel.type = event.type;
        return eventModel;
    }

    public static OutcomeModel fromDecisionDto(DecisionResultDto decisionDto) {
        OutcomeModel model = new OutcomeModel();
        model.outcomeId = decisionDto.decisionId;
        model.outcomeName = decisionDto.decisionName;
        model.hasErrors = decisionDto.hasErrors;
        model.result = decisionDto.result;
        model.messages = decisionDto.messages;
        model.evaluationStatus = fromEvaluationStatusDto(decisionDto.evaluationStatus);
        return model;
    }

    public static OutcomeEvaluationStatusModel fromEvaluationStatusDto(DecisionEvaluationStatusDto evaluation) {
        switch (evaluation) {
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
