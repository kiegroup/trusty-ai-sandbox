package com.redhat.developer.dmn.models.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeComponent {

    @JsonProperty("name")
    public String name;

    @JsonProperty("typeRef")
    public String typeRef;

    @JsonProperty("isCollection")
    public boolean isCollection;

    @JsonProperty("hasConstraint")
    public boolean hasConstraint;

    @JsonProperty("constraint")
    public Constraint constraint;

    @JsonProperty("isComposite")
    public boolean isComposite;

    public TypeComponent(String name, String typeRef, boolean isCollection, boolean isComposite, boolean hasConstraint, Constraint constraint) {
        this.name = name;
        this.typeRef = typeRef;
        this.isCollection = isCollection;
        this.isComposite = isComposite;
        this.hasConstraint = hasConstraint;
        this.constraint = constraint;
    }
}