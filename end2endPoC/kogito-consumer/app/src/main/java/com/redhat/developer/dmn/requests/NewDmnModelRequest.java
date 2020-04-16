package com.redhat.developer.dmn.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewDmnModelRequest {

    @JsonProperty("nameSpace")
    public String nameSpace;

    @JsonProperty("name")
    public String name;

    @JsonProperty("model")
    public String model;

    @JsonProperty("creationDate")
    public String creationDate;

    @JsonProperty("version")
    public String version = "0.1";

    public NewDmnModelRequest() {
    }
}
