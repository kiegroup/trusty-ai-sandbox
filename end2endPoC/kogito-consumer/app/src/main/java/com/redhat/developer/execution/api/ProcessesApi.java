package com.redhat.developer.execution.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.execution.responses.execution.ExecutionDetailResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/executions/processes")
public class ProcessesApi {

    @GET
    @Path("/{key}")
    @Operation(summary = "Gets the execution header of the process only. To be implemented.", description = "Gets the execution header of the processe only.")
    @Produces(MediaType.APPLICATION_JSON)
    public ExecutionDetailResponse getExecutionByKey(@PathParam("key") String key) {
        return new ExecutionDetailResponse();
    }
}
