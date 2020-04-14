package com.redhat.developer.dmn;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.drools.core.io.impl.ReaderResource;
import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.internal.utils.DMNEvaluationUtils;
import org.kie.dmn.core.internal.utils.DMNRuntimeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DMNKogito {

    private static final Logger LOG = LoggerFactory.getLogger(DMNKogito.class);

    private DMNKogito() {
        // intentionally private.
    }

    public static DMNRuntime createGenericDMNRuntime(Reader... readers) {
        List<Resource> resources = Stream.of(readers).map(ReaderResource::new).collect(Collectors.toList());
        return DMNRuntimeBuilder.fromDefaults()
                .setRootClassLoader(null)
                .buildConfiguration()
                .fromResources(resources)
                .getOrElseThrow(e -> new RuntimeException("Error initalizing DMNRuntime", e));
    }

    public static DMNModel modelByName(DMNRuntime dmnRuntime, String modelName) {
        List<DMNModel> modelsWithName = dmnRuntime.getModels().stream().filter(m -> modelName.equals(m.getName())).collect(Collectors.toList());
        if (modelsWithName.size() == 1) {
            return modelsWithName.get(0);
        } else {
            throw new RuntimeException("Multiple model with the same name: " + modelName);
        }
    }

    public static DMNResult evaluate(DMNRuntime dmnRuntime, String modelName, Map<String, Object> dmnContext) {
        return evaluate(dmnRuntime, modelByName(dmnRuntime, modelName).getNamespace(), modelName, dmnContext);
    }

    public static DMNResult evaluate(DMNRuntime dmnRuntime, String modelNamespace, String modelName, Map<String, Object> dmnContext) {
        DMNEvaluationUtils.DMNEvaluationResult evaluationResult = DMNEvaluationUtils.evaluate(dmnRuntime,
                                                                                              modelNamespace,
                                                                                              modelName,
                                                                                              dmnContext,
                                                                                              null,
                                                                                              null,
                                                                                              null);
        return evaluationResult.result;
    }
}
