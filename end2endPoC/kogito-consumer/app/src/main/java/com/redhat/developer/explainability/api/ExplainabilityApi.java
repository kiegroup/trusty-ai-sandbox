package com.redhat.developer.explainability.api;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.explainability.responses.AvailableAlgorithmsClassResponse;
import com.redhat.developer.explainability.responses.AvailableAlgorithmsResponse;
import com.redhat.developer.explainability.responses.global.ModelExplainationResponse;
import com.redhat.developer.explainability.responses.local.DecisionExplanationResponse;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/explainability")
public class ExplainabilityApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AvailableAlgorithmsClassResponse getAlgorithmClasses() {
        return new AvailableAlgorithmsClassResponse();
    }

    @GET
    @Path("/global")
    @Produces(MediaType.APPLICATION_JSON)
    public AvailableAlgorithmsResponse getAvailableGlobalAlgorithms() {
        return new AvailableAlgorithmsResponse(new String[]{"LIME", "TEST"});
    }

    @GET
    @Path("/local")
    @Produces(MediaType.APPLICATION_JSON)
    public AvailableAlgorithmsResponse getAvailableLocalAlgorithms() {
        return new AvailableAlgorithmsResponse(new String[]{"DUNNO", "TEST"});
    }

    @GET
    @Path("/global/{algorithmName}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelExplainationResponse evaluateModel(@PathParam("algorithmName") String algorithmName) {
        return new ModelExplainationResponse();
    }

    @GET
    @Path("/local/{algorithmName}")
    @Produces(MediaType.APPLICATION_JSON)
    public ModelExplainationResponse getExplainationDetails(@PathParam("algorithmName") String algorithmName) {
        return new ModelExplainationResponse();
    }

    @POST
    @Path("/local/{algorithmName}/explain")
    @Produces(MediaType.APPLICATION_JSON)
    public DecisionExplanationResponse explainDecision(@PathParam("algorithmName") String algorithmName, Map<String, Object> input) {
        return new DecisionExplanationResponse();
    }
}