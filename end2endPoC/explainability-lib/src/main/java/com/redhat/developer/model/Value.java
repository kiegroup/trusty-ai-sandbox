package com.redhat.developer.model;

import java.util.Arrays;

public class Value<S> {

    private final S underlyingObject;

    public Value(S underlyingObject) {
        this.underlyingObject = underlyingObject;
    }

    public String asString() {
        if (underlyingObject == null) {
            return "";
        } else {
            return String.valueOf(underlyingObject);
        }
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

    public double[] asVector() {
        double[] doubles;
        try {
            doubles = (double[]) underlyingObject;
        } catch (ClassCastException cce) {
            if (underlyingObject instanceof String) {
                int noOfWords = ((String) underlyingObject).split(" ").length;
                doubles = new double[noOfWords];
                Arrays.fill(doubles, 1);
            } else {
                try {
                    double v = asNumber();
                    doubles = new double[1];
                    doubles[0] = v;
                } catch (Exception e) {
                    doubles = new double[0];
                }
            }
        }
        return doubles;
    }
}
