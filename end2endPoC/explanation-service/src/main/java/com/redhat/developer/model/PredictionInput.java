package com.redhat.developer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.redhat.developer.requests.TypedData;
import org.json.JSONObject;

public class PredictionInput {

    private final List<Feature> features;

    public PredictionInput( List<Feature> features) {
        this.features = features;
    }

    public JSONObject toKogitoRequestJson(List<TypedData> inputStructure){
        JSONObject json = new JSONObject();
        for (TypedData input : inputStructure){
            if (input.value != null){ // is a built in type
                Value value = features.stream().filter(x -> x.getName().equals(input.inputName)).findFirst().get().getValue();
                json.put(input.inputName, input.typeRef.equals("string") ? value.asString() : value.asNumber());
            }
            else{
                json.put(input.inputName, toKogitoRequestJson(input.components.get(0)));
            }
        }
        return json;
    }


    public List<Feature> getFeatures() {
        return features;
    }
}
