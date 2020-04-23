package org.kie.trusty.v1;

/**
 * A feature associated to a {@link PredictionInput}.
 * Each feature can be of different {@code Type}s, has a name, and a {@code FeatureValue}.
 */
public interface Feature {

    /**
     * the name of the feature
     *
     * @return this feature name
     */
    String getName();

    /**
     * the type (quantitative, ordinal, categorical) of the feature
     * @return this feature type
     */
    Type getType();

    /**
     * the value of the feature
     * @return the feature value
     */
    FeatureValue getValue();

    /**
     * an enum for feature types
     */
    public static enum Type {
        QUANTITATIVE,
        CATEGORICAL,
        ORDINAL
    }
}
