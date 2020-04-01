package com.redhat.developer.kafka.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNDataDto {

    @JsonProperty("result")
    public DMNResultDto result;
}
