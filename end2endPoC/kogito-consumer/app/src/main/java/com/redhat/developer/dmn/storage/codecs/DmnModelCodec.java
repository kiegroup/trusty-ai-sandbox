package com.redhat.developer.dmn.storage.codecs;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.redhat.developer.dmn.models.DmnModel;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.models.OutcomeModel;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DmnModelCodec implements CollectibleCodec<DmnModel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DmnModelCodec.class);

    private final Codec<Document> documentCodec;

    private static final ObjectMapper mapper = new ObjectMapper();

    public DmnModelCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, DmnModel model, EncoderContext encoderContext) {
        try {
            Document myDoc = Document.parse(mapper.writeValueAsString(model));
            documentCodec.encode(writer, myDoc, encoderContext);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<DmnModel> getEncoderClass() {
        return DmnModel.class;
    }

    @Override
    public DmnModel generateIdIfAbsentFromDocument(DmnModel document) {
        if (!documentHasId(document)) {
            document.modelId = (UUID.randomUUID().toString());
        }
        return document;
    }

    @Override
    public boolean documentHasId(DmnModel document) {
        return document.modelId != null;
    }

    @Override
    public BsonValue getDocumentId(DmnModel document) {
        return new BsonString(document.modelId);
    }

    @Override
    public DmnModel decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        try {
            return mapper.readValue(document.toJson(), DmnModel.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}