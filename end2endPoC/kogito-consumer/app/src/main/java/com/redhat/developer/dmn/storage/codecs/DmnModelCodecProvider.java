package com.redhat.developer.dmn.storage.codecs;

import com.redhat.developer.dmn.models.DmnModel;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class DmnModelCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz == DmnModel.class) {
            return (Codec<T>) new DmnModelCodec();
        }
        return null;
    }
}
