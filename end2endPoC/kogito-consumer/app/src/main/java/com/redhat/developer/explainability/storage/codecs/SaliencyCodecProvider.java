package com.redhat.developer.explainability.storage.codecs;

import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.explainability.model.Saliency;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class SaliencyCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == Saliency.class) {
            return (Codec<T>) new SaliencyCodecs();
        }
        return null;
    }
}
