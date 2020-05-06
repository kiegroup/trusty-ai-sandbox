package org.kie.trusty.m2x.model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Value")
public class Value<S extends Serializable> {

    private final S underlyingObject;

    public Value(S underlyingObject) {
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
}
