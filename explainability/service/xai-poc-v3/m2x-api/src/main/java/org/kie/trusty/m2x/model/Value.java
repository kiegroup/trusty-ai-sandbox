package org.kie.trusty.m2x.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Value")
public class Value<S> {

    private S underlyingObject;

    public Value() {
        this.underlyingObject = null;
    }

    @JsonCreator
    public Value(@JsonProperty("object") S underlyingObject) {
        this.underlyingObject = underlyingObject;
    }

    public String asString() {
        return String.valueOf(underlyingObject);
    }

    public String asString(ValueEncoder<S, String> encoder) {
        return encoder.encode(underlyingObject);
    }

    public double asNumber() {
        return Double.parseDouble(asString());
    }

    public double asNumber(ValueEncoder<S, Double> encoder) {
        return encoder.encode(underlyingObject);
    }

    public void setUnderlyingObject(S underlyingObject) {
        this.underlyingObject = underlyingObject;
    }

    public S getUnderlyingObject() {
        return underlyingObject;
    }

    @Override
    public String toString() {
        return "Value{" +
                "underlyingObject=" + underlyingObject +
                '}';
    }
}
