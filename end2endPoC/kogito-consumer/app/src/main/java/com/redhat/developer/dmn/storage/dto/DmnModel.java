package com.redhat.developer.dmn.storage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.dmn.requests.NewDmnModelRequest;

public class DmnModel {
    @JsonProperty("nameSpace")
    public String nameSpace;

    @JsonProperty("name")
    public String name;

    @JsonProperty("model")
    public String model;

    @JsonProperty("modelId")
    public String modelId;

    @JsonProperty("creationDate")
    public String creationDate;

    @JsonProperty("version")
    public String version = "0.1";

    public DmnModel(String nameSpace, String name, String model, String modelId, String creationDate, String version){
        this.nameSpace = nameSpace;
        this.name = name;
        this.model = model;
        this.modelId = modelId;
        this.creationDate = creationDate;
        this.version = version;
    }

    public static DmnModel fromNewDmnModelRequest(NewDmnModelRequest request, String modelId){
        return new DmnModel(request.nameSpace, request.name, request.model, modelId, request.creationDate, request.version);
    }
}

