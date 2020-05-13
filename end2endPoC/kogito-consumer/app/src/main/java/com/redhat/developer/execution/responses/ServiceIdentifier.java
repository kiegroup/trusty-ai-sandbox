package com.redhat.developer.execution.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceIdentifier {

    @JsonProperty("groupId")
    public String groupId;

    @JsonProperty("artifactId")
    public String artifactId;

    @JsonProperty("version")
    public String version;
}
