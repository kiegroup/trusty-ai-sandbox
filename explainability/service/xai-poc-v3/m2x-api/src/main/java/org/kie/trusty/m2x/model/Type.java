package org.kie.trusty.m2x.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * an enum for accepted types of input / output values
 */
@Schema(name="Type")
public enum Type {

    STRING("string"),

    BINARY("binary"),

    NUMBER("number"),

    BOOLEAN("boolean"),

    DATE("date"),

    URI("uri"),

    TIME("time"),

    DURATION("duration"),

    VECTOR("vector"),

    UNDEFINED("undefined"),

    CURRENCY("currency");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Type fromValue(String text) {
        for (Type b : Type.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}