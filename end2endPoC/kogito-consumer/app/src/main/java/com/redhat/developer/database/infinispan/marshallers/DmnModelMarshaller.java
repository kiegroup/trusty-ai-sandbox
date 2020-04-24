package com.redhat.developer.database.infinispan.marshallers;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.dmn.models.DmnModel;
import org.infinispan.protostream.MessageMarshaller;

public class DmnModelMarshaller extends AbstractMarshaller implements MessageMarshaller<DmnModel> {

    private static final ObjectMapper myMapper = new ObjectMapper();

    public DmnModelMarshaller(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public DmnModel readFrom(ProtoStreamReader reader) throws IOException {
        DmnModel model = new DmnModel();

        model.modelId = reader.readString("modelId");
        model.model = reader.readString("model");
        model.creationDate = reader.readString("creationDate");
        model.name = reader.readString("name");
        model.nameSpace = reader.readString("nameSpace");
        model.version = reader.readString("version");
        return model;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, DmnModel model) throws IOException {
        writer.writeString("modelId", model.modelId);
        writer.writeString("model", model.model);
        writer.writeString("creationDate", model.creationDate);
        writer.writeString("name", model.name);
        writer.writeString("nameSpace", model.nameSpace);
        writer.writeString("version", model.version);
    }

    @Override
    public Class<? extends DmnModel> getJavaClass() {
        return DmnModel.class;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getName();
    }
}
