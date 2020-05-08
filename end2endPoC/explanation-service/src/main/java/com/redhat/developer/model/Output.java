package com.redhat.developer.model;

public class Output {

    private final Value value;
    private final Type type;
    private final double score;
    private final String name;

    public Output(String name, Type type, Value value, double score) {
        this.name = name;
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
