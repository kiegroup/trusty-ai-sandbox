package com.redhat.developer.execution.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.responses.execution.ExecutionResponse;
import com.redhat.developer.execution.storage.model.DMNResultModel;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

@Path("/executions")
public class ExecutionsApi {

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
        results.forEach(x -> executionResponses.add(ExecutionHeaderResponse.fromDMNResultModel(x)));
        return new ExecutionResponse(totalResults, limit, offset, executionResponses);
    }
}
