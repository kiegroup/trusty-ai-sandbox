package com.redhat.developer.decision.messaging;

import com.redhat.developer.decision.dto.DMNEvent;

public class DMNCloudEventDeserializer extends AbstractCloudEventDeserializer<DMNEvent> {

    public DMNCloudEventDeserializer(){
        super(DMNEvent.class);
    }
}
