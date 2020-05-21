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
        return underlyingObject instanceof Boolean ? (Boolean) underlyingObject ? 1d : 0d : Double.parseDouble(asString());
    }

    public double asNumber(ValueEncoder<S, Double> encoder) {
        return encoder.encode(underlyingObject);
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
