package com.redhat.developer.kafka.messaging;

import java.io.IOException;
import java.util.Map;

import com.redhat.developer.kafka.messaging.utils.JsonUtils;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCloudEventDeserializer<T> implements Deserializer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCloudEventDeserializer.class);
    private final Class<T> type;

    public AbstractCloudEventDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            return JsonUtils.getObjectMapper().readValue(data, type);
        } catch (IOException e) {
            LOGGER.error("Error parsing JSON content: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }
}