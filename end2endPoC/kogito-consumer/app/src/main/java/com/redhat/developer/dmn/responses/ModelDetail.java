package com.redhat.developer.dmn.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.dmn.storage.dto.DmnModel;

public class ModelDetail {

    @JsonProperty("modelNamespace")
    public String modelNamespace;

    @JsonProperty("modelName")
    public String modelName;

    @JsonProperty("modelVersion")
    public String modelVersion;

    @JsonProperty("creationDate")
    public String creationDate;

    @JsonProperty("modelId")
    public String modelId;

    public ModelDetail(String modelNamespace, String modelName, String modelVersion, String creationDate, String modelId){
        this.modelNamespace = modelNamespace;
        this.modelName = modelName;
        this.modelVersion = modelVersion;
        this.creationDate = creationDate;
        this.modelId = modelId;
    }

    public static ModelDetail fromStorageModel(DmnModel model){
        return new ModelDetail(model.nameSpace, model.name, model.version, model.creationDate, model.modelId);
    }
}
