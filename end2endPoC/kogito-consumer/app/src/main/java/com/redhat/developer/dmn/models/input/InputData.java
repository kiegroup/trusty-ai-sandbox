package com.redhat.developer.dmn.models.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputData {

    @JsonProperty("name")
    public String name;

    @JsonProperty("typeRef")
    public String typeRef;

    @JsonProperty("isComposite")
    public boolean isComposite;

    @JsonProperty("isCollection")
    public boolean isCollection;

    public InputData(String name, String typeRef, boolean isComposite, boolean isCollection) {
        this.name = name;
        this.typeRef = typeRef;
        this.isComposite = isComposite;
        this.isCollection = isCollection;
    }
}