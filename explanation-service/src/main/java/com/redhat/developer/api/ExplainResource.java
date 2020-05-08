package com.redhat.developer.api;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.developer.LIMEishSaliencyExplanationProvider;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.requests.TypedData;
import com.redhat.developer.requests.LocalExplanationRequest;

@Path("/xai")
public class ExplainResource {

    @Inject
    LIMEishSaliencyExplanationProvider explanationProvider;

    @POST
    @Path("/saliency/lime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response lime(LocalExplanationRequest request) {
        List<TypedData> inputs = request.input;

        Saliency saliency = explanationProvider.explain(inputs, request.outputs, request.modelName);

        return Response.ok(saliency).build();
    }
}
