package org.kie.trusty.m2x.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A feature in a predictive / decision model.
 * Each feature can be of different {@code Type}s, has a name, and a {@code FeatureValue}.
 */
@Schema(name="Feature")
public class Feature {

    private final String name;
    private final Type type;
    private final Value value;

    public Feature(String name, Type type, Value value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * the name of the feature
     *
     * @return this feature name
     */
    public String getName() {
        return this.name;
    }

    /**
     * the type of the feature
     *
     * @return this feature type
     */
    public Type getType() {
        return type;
    }

    /**
     * the value of the feature
     *
     * @return the feature value
     */
    public Value getValue() {
        return value;
    }
}
