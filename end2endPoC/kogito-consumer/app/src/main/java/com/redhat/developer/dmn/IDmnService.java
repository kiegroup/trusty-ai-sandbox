package com.redhat.developer.dmn;

import java.util.List;
import java.util.Map;

import com.redhat.developer.dmn.models.DmnModel;
import com.redhat.developer.dmn.models.input.ModelInputStructure;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.core.ast.DMNBaseNode;

public interface IDmnService {

    Map<String, Object>  evaluateModel(String id, Map<String, Object> inputs);

    DMNModel getDmnModel(String id);

    ModelInputStructure getDmnInputStructure(String id);

    Map<DMNBaseNode, List<DMNBaseNode>> getDmnDependencyGraph(String id);

    DmnModel getDmnModelDocument(String id);
}
