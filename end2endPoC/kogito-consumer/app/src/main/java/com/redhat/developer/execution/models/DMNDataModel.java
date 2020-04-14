package com.redhat.developer.execution.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DMNDataModel {

    @JsonProperty("result")
    public DMNResultModel result;

    public DMNDataModel() {
    }

    public DMNDataModel(DMNResultModel resultModel) {
        this.result = resultModel;
    }
}
