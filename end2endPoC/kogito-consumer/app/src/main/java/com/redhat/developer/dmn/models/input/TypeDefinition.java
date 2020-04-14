package com.redhat.developer.dmn.models.input;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeDefinition {

    @JsonProperty("typeName")
    public String typeName;

    @JsonProperty("isCollection")
    public boolean isCollection;

    @JsonProperty("isComposite")
    public boolean isComposite;

    @JsonProperty("components")
    public List<TypeComponent> components;

    public TypeDefinition(String typeName, boolean isCollection, boolean isComposite, List<TypeComponent> components) {
        this.typeName = typeName;
        this.isCollection = isCollection;
        this.isComposite = isComposite;
        this.components = components;
    }
}