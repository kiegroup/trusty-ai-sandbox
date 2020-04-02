package com.redhat.developer.dmn.api;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.dmn.responses.AvailableModelsResponse;
import com.redhat.developer.dmn.responses.EvaluationResponse;
import com.redhat.developer.dmn.responses.ModelResponse;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/models")
public class DmnApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AvailableModelsResponse getModels() {
        return new AvailableModelsResponse(new ArrayList<>());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelResponse getModelById(@PathParam("id") String id) {
        return new ModelResponse();
    }

    @POST
    @Path("/{id}/evaluate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public EvaluationResponse evaluateDecision(@PathParam("id") String id, Object inputs) {
        return new EvaluationResponse();
    }
}