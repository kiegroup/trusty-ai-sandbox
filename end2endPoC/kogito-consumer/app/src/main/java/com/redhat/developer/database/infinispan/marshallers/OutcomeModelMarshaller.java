package com.redhat.developer.database.infinispan.marshallers;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.execution.models.OutcomeEvaluationStatusModel;
import com.redhat.developer.execution.models.OutcomeModel;
import org.infinispan.protostream.MessageMarshaller;

public class OutcomeModelMarshaller extends AbstractMarshaller implements MessageMarshaller<OutcomeModel> {

    private static final ObjectMapper myMapper = new ObjectMapper();

    public OutcomeModelMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public OutcomeModel readFrom(ProtoStreamReader reader) throws IOException {
        OutcomeModel outcome = new OutcomeModel();
        outcome.outcomeId = reader.readString("outcomeId");
        outcome.outcomeName = reader.readString("outcomeName");
        outcome.messages = Arrays.asList(reader.readArray("messages", String.class));
        outcome.hasErrors = reader.readBoolean("hasErrors");
        outcome.evaluationStatus = myMapper.readValue(reader.readString("evaluationStatus"), OutcomeEvaluationStatusModel.class);
        outcome.result = myMapper.readValue(reader.readString("outcomeResult"), Object.class);
        return outcome;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, OutcomeModel result) throws IOException {
        writer.writeString("outcomeId", result.outcomeId);
        writer.writeString("outcomeName", result.outcomeName);
        writer.writeArray("messages", result.messages.toArray(), String.class);
        writer.writeBoolean("hasErrors", result.hasErrors);
        writer.writeString("evaluationStatus", myMapper.writeValueAsString(result.evaluationStatus));
        writer.writeString("outcomeResult", myMapper.writeValueAsString(result.result));
    }

    @Override
    public Class<? extends OutcomeModel> getJavaClass() {
        return OutcomeModel.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
