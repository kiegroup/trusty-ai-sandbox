package com.redhat.developer.model;

public class Value<S> {

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
