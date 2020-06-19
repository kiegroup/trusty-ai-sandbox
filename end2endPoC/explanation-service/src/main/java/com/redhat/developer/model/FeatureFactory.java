package com.redhat.developer.model;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Currency;
import java.util.Date;

/**
 * Factory class for {@link Feature}s
 */
public class FeatureFactory {

    private FeatureFactory() {
    }

    public static Feature newTextFeature(String name, String text) {
        return new Feature(name, Type.TEXT, new Value<>(text));
    }

    public static Feature newCategoricalFeature(String name, String category) {
        return new Feature(name, Type.CATEGORICAL, new Value<>(category));
    }

    public static Feature newNumericalFeature(String name, Number number) {
        return new Feature(name, Type.NUMBER, new Value<>(number));
    }

    public static Feature newBooleanFeature(String name, Boolean truthValue) {
        return new Feature(name, Type.BOOLEAN, new Value<>(truthValue));
    }

    public static Feature newDateFeature(String name, Date date) {
        return new Feature(name, Type.DATE, new Value<>(date));
    }

    public static Feature newCurrencyFeature(String name, Currency currency) {
        return new Feature(name, Type.CURRENCY, new Value<>(currency));
    }

    public static Feature newBinaryFeature(String name, ByteBuffer byteBuffer) {
        return new Feature(name, Type.BINARY, new Value<>(byteBuffer));
    }

    public static Feature newURIFeature(String name, URI uri) {
        return new Feature(name, Type.URI, new Value<>(uri));
    }

    public static Feature newDurationFeature(String name, Duration duration) {
        return new Feature(name, Type.DURATION, new Value<>(duration));
    }

    public static Feature newTimeFeature(String name, LocalTime time) {
        return new Feature(name, Type.TIME, new Value<>(time));
    }

    public static Feature newVectorFeature(String name, double... doubles) {
        return new Feature(name, Type.VECTOR, new Value<>(doubles));
    }

    public static Feature newObjectFeature(String name, Object object) {
        return new Feature(name, Type.UNDEFINED, new Value<>(object));
    }
}
