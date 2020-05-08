package com.redhat.developer.explainability.storage.codecs;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClientSettings;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.explainability.model.Saliency;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaliencyCodecs implements CollectibleCodec<Saliency> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaliencyCodecs.class);

    private final Codec<Document> documentCodec;

    private static final ObjectMapper mapper = new ObjectMapper();

    public SaliencyCodecs() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Saliency result, EncoderContext encoderContext) {
        try {
            Document myDoc = Document.parse(mapper.writeValueAsString(result));
            documentCodec.encode(writer, myDoc, encoderContext);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<Saliency> getEncoderClass() {
        return Saliency.class;
    }

    @Override
    public Saliency generateIdIfAbsentFromDocument(Saliency document) {
        if (!documentHasId(document)) {
            document.executionId = (UUID.randomUUID().toString());
        }
        return document;
    }

    @Override
    public boolean documentHasId(Saliency document) {
        return document.executionId != null;
    }

    @Override
    public BsonValue getDocumentId(Saliency document) {
        return new BsonString(document.executionId);
    }

    @Override
    public Saliency decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);

        try {
            return mapper.readValue(document.toJson(), Saliency.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}