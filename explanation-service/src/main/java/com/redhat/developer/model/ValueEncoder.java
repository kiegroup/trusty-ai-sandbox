package com.redhat.developer.model;

public interface ValueEncoder<S, T> {

    T encode(S value);
}
