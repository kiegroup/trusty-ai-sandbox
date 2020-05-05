package com.redhat.developer.explainability.api;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/local")
public class LocalExplanabilityApi {

    @POST
    @Path("/saliency/lime")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response lime() {
        return Response.ok().build();
    }
}