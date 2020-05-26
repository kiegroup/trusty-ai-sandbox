package com.redhat.developer.execution.storage.codecs;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.redhat.developer.execution.models.DMNResultModel;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.json.JsonWriterSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionCodec implements CollectibleCodec<DMNResultModel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionCodec.class);

    private final Codec<Document> documentCodec;

    private static final ObjectMapper mapper = new ObjectMapper();

    JsonWriterSettings settings = JsonWriterSettings.builder()
            .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
            .build();

    public ExecutionCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, DMNResultModel result, EncoderContext encoderContext) {
        try {
            Document myDoc = Document.parse(mapper.writeValueAsString(result));
            documentCodec.encode(writer, myDoc, encoderContext);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<DMNResultModel> getEncoderClass() {
        return DMNResultModel.class;
    }

    @Override
    public DMNResultModel generateIdIfAbsentFromDocument(DMNResultModel document) {
        if (!documentHasId(document)) {
            document.executionId = (UUID.randomUUID().toString());
        }
        return document;
    }

    @Override
    public boolean documentHasId(DMNResultModel document) {
        return document.executionId != null;
    }

    @Override
    public BsonValue getDocumentId(DMNResultModel document) {
        return new BsonString(document.executionId);
    }

    @Override
    public DMNResultModel decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);

        try {
            return mapper.readValue(document.toJson(settings), DMNResultModel.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}