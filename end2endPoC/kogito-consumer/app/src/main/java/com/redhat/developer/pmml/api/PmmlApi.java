package com.redhat.developer.pmml.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.pmml.responses.AvailablePmmlModelsResponse;

@Path("/pmml")
public class PmmlApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AvailablePmmlModelsResponse getModels() {
        return new AvailablePmmlModelsResponse();
    }
}
