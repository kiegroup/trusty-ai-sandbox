package com.redhat.developer.dmn.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelDetail {

    @JsonProperty("modelNamespace")
    public String modelNamespace;

    @JsonProperty("modelName")
    public String modelName;

    @JsonProperty("modelVersion")
    public String modelVersion;

    @JsonProperty("creationDate")
    public String creationDate;
}
