package com.redhat.developer.api;

import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.developer.LocalSaliencyExplanationProvider;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.requests.LocalExplanationRequest;
import com.redhat.developer.responses.FeatureImportanceResponse;
import com.redhat.developer.responses.SaliencyResponse;

@Path("/xai")
public class ExplainResource {

    @Inject
    LocalSaliencyExplanationProvider explanationProvider;

    @POST
    @Path("/saliency/lime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response lime(LocalExplanationRequest request) {
        Saliency saliency = explanationProvider.explain(request.input, request.outputs, request.modelName);

        SaliencyResponse response = new SaliencyResponse(saliency.getPerFeatureImportance().stream().map(x -> new FeatureImportanceResponse(x.getFeature().getName(), x.getScore())).collect(Collectors.toList()));
        return Response.ok(response).build();
    }
}
