package com.redhat.developer.execution.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.execution.responses.execution.ExecutionDetailResponse;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/executions/processes")
public class ProcessesApi {

    @GET
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public ExecutionDetailResponse getExecutionByKey(@PathParam("key") String key) {
        return new ExecutionDetailResponse();
    }
}
