package com.redhat.developer.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SaliencyResponse {
        @JsonProperty("featureImportance")
        public List<FeatureImportanceResponse> featureImportance;

        public SaliencyResponse(){}

        public SaliencyResponse(List<FeatureImportanceResponse> featureImportance){
            this.featureImportance = featureImportance;
        }
}
