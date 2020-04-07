package com.redhat.developer.dmn.api;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.dmn.requests.EvaluationRequestBody;
import com.redhat.developer.dmn.responses.AvailableModelsResponse;
import com.redhat.developer.dmn.responses.EvaluationResponse;
import com.redhat.developer.dmn.responses.ModelResponse;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
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
    public EvaluationResponse evaluateDecision(@Parameter(
                                                        name = "id",
                                                        description = "ID of the DMN Model to evaluate.",
                                                        required = true,
                                                        schema = @Schema(implementation = String.class)
                                                ) @PathParam("id") String id, EvaluationRequestBody inputs) {
        return new EvaluationResponse();
    }
}