package com.redhat.developer.database.infinispan.marshallers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.models.OutcomeModel;
import org.infinispan.protostream.MessageMarshaller;

public class ExecutionMarshaller extends AbstractMarshaller implements MessageMarshaller<DMNResultModel> {

    private static final ObjectMapper myMapper = new ObjectMapper();

    public ExecutionMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public DMNResultModel readFrom(ProtoStreamReader reader) throws IOException {
        DMNResultModel result =  new DMNResultModel();

        result.executionId = reader.readString("executionId");
        result.executionDate = new Date(reader.readLong("executionDate"));
        result.modelId = reader.readString("modelId");
        result.modelName = reader.readString("modelName");
        result.modelNamespace = reader.readString("modelNamespace");
        result.context = myMapper.readValue(reader.readString("context"), Map.class);
        result.decisions = Arrays.asList(reader.readArray("decisions", OutcomeModel.class));
        return result;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, DMNResultModel result) throws IOException {
        writer.writeString("executionId", result.executionId );
        writer.writeLong("executionDate", result.executionDate.toInstant().toEpochMilli() );
        writer.writeString("modelId", result.modelId );
        writer.writeString("modelNamespace", result.modelNamespace );
        writer.writeString("modelName", result.modelName );
        writer.writeArray("decisions", result.decisions.toArray(), OutcomeModel.class );
        writer.writeString("context", myMapper.writeValueAsString(result.context));
    }

    @Override
    public Class<? extends DMNResultModel> getJavaClass() {
        return DMNResultModel.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
