package com.redhat.developer.explainability.api;

import java.util.Collection;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.developer.execution.IExecutionService;
import com.redhat.developer.execution.models.DMNResultModel;


@Path("/global")
public class GlobalExplanabilityApi {

    @Inject
    IExecutionService executionService;

    @POST
    @Path("/tabular/pdp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pdp(@QueryParam("executionId") String executionId) {
        DMNResultModel dmnResultModel = executionService.getEventsByMatchingId(executionId).get(0);
        return Response.ok().build();
    }
}