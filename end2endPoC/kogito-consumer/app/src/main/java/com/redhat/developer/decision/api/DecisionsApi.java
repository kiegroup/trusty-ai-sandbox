package com.redhat.developer.decision.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.decision.responses.DecisionDetailResponse;
import com.redhat.developer.decision.responses.DecisionInputsResponse;
import com.redhat.developer.decision.responses.DecisionOutputsResponse;
import com.redhat.developer.decision.responses.ExecutionHeaderResponse;
import com.redhat.developer.decision.responses.ExecutionResponse;
import com.redhat.developer.decision.storage.model.DMNResultModel;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

@Path("/executions")
public class DecisionsApi {

    @Inject
    IEventStorage storageService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ExecutionResponse getExecutions(@DefaultValue("yesterday") @QueryParam("from") String from,
                                           @DefaultValue("now") @QueryParam("to") String to,
                                           @DefaultValue("100") @QueryParam("limit") int limit,
                                           @DefaultValue("0") @QueryParam("offset") int offset) {
        // Testing hack
        if (from.equals("yesterday")){
            from = java.time.LocalDateTime.now().minusDays(1).toString();
        }

        List<DMNResultModel> results = storageService.getDecisions(from, to);
        int totalResults = results.size();
        if (totalResults >= limit){
            results.sort(Comparator.comparing(DMNResultModel::getExecutionDate));
            results = results.subList(offset, Math.min(results.size(), offset + limit));
        }

        List<ExecutionHeaderResponse> executionResponses = new ArrayList<>();
        results.forEach(x -> executionResponses.add(buildHeaderResponse(x)));
        return new ExecutionResponse(totalResults, limit, offset, executionResponses);
    }

    @GET
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public DecisionDetailResponse getExecutionByKey(@PathParam("key") String key) {
        DMNResultModel result = storageService.getEvent(key).data.result;
        return new DecisionDetailResponse(buildHeaderResponse(result));
    }

    @GET
    @Path("/{key}/inputs")
    @Produces(MediaType.APPLICATION_JSON)
    public DecisionInputsResponse getExecutionInputs(@PathParam("key") String key) {
        DMNResultModel result = storageService.getEvent(key).data.result;
        return new DecisionInputsResponse(buildHeaderResponse(result), result.context);
    }

    @GET
    @Path("/{key}/outcomes")
    @Produces(MediaType.APPLICATION_JSON)
    public DecisionOutputsResponse getExecutionOutcome(@PathParam("key") String key) {
        DMNResultModel result = storageService.getEvent(key).data.result;
        return new DecisionOutputsResponse(buildHeaderResponse(result), result.decisions);
    }

    private ExecutionHeaderResponse buildHeaderResponse(DMNResultModel result) {
        return new ExecutionHeaderResponse(result.executionId, result.executionDate, result.decisions.stream().anyMatch(y -> y.hasErrors == true), "testUser");
    }
}
