package org.kie.trusty.m2x.model;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/model")
public class ModelResource {

    public ModelProvider getModelProvider() {
        return new ModelProvider() {
            @Override
            public Model getModel() {
                return new DummyModel();
            }
        };
    }

    @POST
    @Path("/predict")
    @Operation(summary = "Perform a prediction",
            responses = {
                    @ApiResponse(description = "The prediction outputs",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(
                                    schema = @Schema(implementation = PredictionOutput.class))))})
    public Response predict(
            @Parameter(required = true) PredictionInput... inputs) {
        List<PredictionOutput> outputs = getModelProvider().getModel().predict(inputs);
        Gson gson = new Gson();
        String s = gson.toJson(outputs);
        return Response.ok().entity(s).build();
    }

    @GET
    @Path("/info")
    @Operation(summary = "Get information about the model",
            responses = {
                    @ApiResponse(description = "Information about the model",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ModelInfo.class))
                    )
            }
    )
    public Response info() {
        return Response.ok().entity(getModelProvider().getModel().getInfo()).build();
    }
}
