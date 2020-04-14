package com.redhat.developer.dmn;

import java.util.Map;

import com.redhat.developer.dmn.models.input.ModelInputStructure;
import org.kie.dmn.api.core.DMNModel;

public interface IDmnService {

    Object evaluateModel(String id, Map<String, Object> inputs);

    DMNModel getDmnModel(String id);

    ModelInputStructure getDmnInputStructure(String id);
}
