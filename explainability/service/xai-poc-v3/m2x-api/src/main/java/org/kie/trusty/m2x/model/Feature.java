package org.kie.trusty.m2x.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A feature in a predictive / decision model.
 * Each feature can be of different {@code Type}s, has a name, and a {@code FeatureValue}.
 */
@Schema(name="Feature")
public class Feature {

    private String name;
    private Type type;
    private Value value;

    public Feature() {
        this.name = "";
        this.type = Type.UNDEFINED;
        this.value = new Value<>();
    }

    @JsonCreator
    public Feature(@JsonProperty("name") String name, @JsonProperty("type") Type type, @JsonProperty("value") Value value) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feature feature = (Feature) o;
        return Objects.equals(name, feature.name) &&
                type == feature.type &&
                Objects.equals(value, feature.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value);
    }

    @Override
    public String toString() {
        return "Feature{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
