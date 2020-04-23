package com.redhat.developer.execution.storage.codecs;

import com.redhat.developer.execution.models.DMNResultModel;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class ExecutionCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == DMNResultModel.class) {
            return (Codec<T>) new ExecutionCodec();
        }
        return null;
    }
}
