package org.kie.trusty.m2x.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The output associated to a {@link PredictionOutput}
 */
@Schema(name="Output")
public class Output {

    private final Value value;
    private final Type type;
    private final double score;

    public Output(Value value, Type type, double score) {
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
}
