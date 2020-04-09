package com.redhat.developer.dmn.responses.inputs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeDefinition{
    @JsonProperty("typeName")
    public String typeName;

    @JsonProperty("isCollection")
    public boolean isCollection;

    @JsonProperty("components")
    public List<TypeComponent> components;
}