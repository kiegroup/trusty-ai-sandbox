package com.redhat.developer.dmn;

import java.util.Map;

public interface IDmnService {
    Object evaluateModel(String id, Map<String, Object> inputs);
}
