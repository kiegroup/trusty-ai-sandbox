package org.kie.trusty.m2x.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The output associated to a {@link PredictionOutput}
 */
@Schema(name="Output")
public class Output {

    private Value value;
    private Type type;
    private double score;

    public Output() {
        this.value = new Value<>();
        this.type = Type.UNDEFINED;
        this.score = 0;
    }

    @JsonCreator
    public Output(@JsonProperty("value") Value value, @JsonProperty("type") Type type, @JsonProperty("score") double score) {
        this.value = value;
        this.type = type;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public Type getType() {
        return type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Output{" +
                "value=" + value +
                ", type=" + type +
                ", score=" + score +
                '}';
    }
}
