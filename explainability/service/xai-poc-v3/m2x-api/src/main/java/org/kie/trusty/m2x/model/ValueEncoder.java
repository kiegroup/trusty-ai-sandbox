package org.kie.trusty.m2x.model;

public interface ValueEncoder<S, T> {

    T encode(S value);
}
