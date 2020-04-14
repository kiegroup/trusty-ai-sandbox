package com.redhat.developer.dmn.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.dmn.models.DmnModel;

public class FullModelResponse extends ModelDetail {

    @JsonProperty("model")
    public String model;

    public FullModelResponse(String modelNamespace, String modelName, String modelVersion, String creationDate, String modelId, String model) {
        super(modelNamespace, modelName, modelVersion, creationDate, modelId);
        this.model = model;
    }

    public static FullModelResponse fromStorageModel(DmnModel model) {
        return new FullModelResponse(model.nameSpace, model.name, model.version, model.creationDate, model.modelId, model.model);
    }
}
