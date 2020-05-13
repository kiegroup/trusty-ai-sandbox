package com.redhat.developer.execution.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.dmn.models.DmnModel;
import io.vertx.core.impl.launcher.commands.ExecUtils;
import org.infinispan.functional.Param;
import org.kie.dmn.api.core.DMNModel;

public class ExecutionModelResponse {

    @JsonProperty("deploymentDate")
    public String deploymentDate;

    @JsonProperty("modelId")
    public String modelId;

    @JsonProperty("name")
    public String name;

    @JsonProperty("namespace")
    public String namespace;

    @JsonProperty("type")
    public String type;

    @JsonProperty("serviceIdentifier")
    public ServiceIdentifier serviceIdentifier;

    @JsonProperty("model")
    public String model;

    public ExecutionModelResponse(){}

    public static ExecutionModelResponse fromDmnModel(DmnModel model){
        ExecutionModelResponse response = new ExecutionModelResponse();
        response.deploymentDate = null;
        response.modelId = model.modelId;
        response.name = model.name;
        response.namespace = model.nameSpace;
        response.type = "http://www.omg.org/spec/DMN/20151101/dmn.xsd";
        response.model = model.model;
        response.serviceIdentifier = new ServiceIdentifier();
        return response;
    }
}
