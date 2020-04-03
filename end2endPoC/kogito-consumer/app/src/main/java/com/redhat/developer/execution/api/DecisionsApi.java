package com.redhat.developer.execution.api;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.execution.responses.execution.ExecutionDetailResponse;
import com.redhat.developer.execution.responses.decisions.DecisionInputsResponse;
import com.redhat.developer.execution.responses.decisions.DecisionOutputsResponse;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.storage.model.DMNResultModel;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/executions/decisions")
public class DecisionsApi {

    @Inject
    IEventStorage storageService;

    @GET
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public ExecutionDetailResponse getExecutionByKey(@PathParam("key") String key) {
        DMNResultModel result = storageService.getEvent(key).data.result;
        return new ExecutionDetailResponse(ExecutionHeaderResponse.fromDMNResultModel(result));
    }

    @GET
    @Path("/{key}/inputs")
    @Produces(MediaType.APPLICATION_JSON)
    public DecisionInputsResponse getExecutionInputs(@PathParam("key") String key) {
        DMNResultModel result = storageService.getEvent(key).data.result;
        return new DecisionInputsResponse(ExecutionHeaderResponse.fromDMNResultModel(result), result.context);
    }

    @GET
    @Path("/{key}/outcomes")
    @Produces(MediaType.APPLICATION_JSON)
    public DecisionOutputsResponse getExecutionOutcome(@PathParam("key") String key) {
        DMNResultModel result = storageService.getEvent(key).data.result;
        return new DecisionOutputsResponse(ExecutionHeaderResponse.fromDMNResultModel(result), result.decisions);
    }

}
