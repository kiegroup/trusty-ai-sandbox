package com.redhat.developer.kafka.messaging;

import com.redhat.developer.kafka.messaging.dto.DMNEventDto;

public class DMNCloudEventDeserializer extends AbstractCloudEventDeserializer<DMNEventDto> {

    public DMNCloudEventDeserializer() {
        super(DMNEventDto.class);
    }
}
