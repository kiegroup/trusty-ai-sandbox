//package com.redhat.developer.explainability.api;
//
//import java.util.Map;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//import com.redhat.developer.explainability.responses.AvailableAlgorithmsClassResponse;
//import com.redhat.developer.explainability.responses.AvailableAlgorithmsResponse;
//import com.redhat.developer.explainability.responses.global.ModelExplainationResponse;
//import com.redhat.developer.explainability.responses.local.DecisionExplanationResponse;
//import org.jboss.resteasy.annotations.jaxrs.PathParam;
//import org.jboss.resteasy.annotations.jaxrs.QueryParam;
//
//@Path("/explainability")
//public class ExplainabilityApi {
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public AvailableAlgorithmsClassResponse getAlgorithmClasses() {
//        return new AvailableAlgorithmsClassResponse();
//    }
//
//    @GET
//    @Path("/global")
//    @Produces(MediaType.APPLICATION_JSON)
//    public AvailableAlgorithmsResponse getAvailableGlobalAlgorithms() {
//        return new AvailableAlgorithmsResponse(new String[]{"LIME", "TEST"});
//    }
//
//    @GET
//    @Path("/local")
//    @Produces(MediaType.APPLICATION_JSON)
//    public AvailableAlgorithmsResponse getAvailableLocalAlgorithms() {
//        return new AvailableAlgorithmsResponse(new String[]{"DUNNO", "TEST"});
//    }
//
//    @GET
//    @Path("/global/{explanabilityType}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public ModelExplainationResponse evaluateModel(@PathParam("explanabilityType") String explanabilityType) {
//        return new ModelExplainationResponse();
//    }
//
//    @GET
//    @Path("/local/{explanabilityType}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public ModelExplainationResponse getExplainationDetails(@PathParam("explanabilityType") String explanabilityType) {
//        return new ModelExplainationResponse();
//    }
//
//    @POST
//    @Path("/local/{explanabilityType}/featureInteraction")
//    @Produces(MediaType.APPLICATION_JSON)
//    public DecisionExplanationResponse explainDecision(@PathParam("explanabilityType") String explanabilityType, @QueryParam("modelId") String modelId, Map<String, Object> input) {
//        return new DecisionExplanationResponse();
//    }
//
//    @POST
//    @Path("/local/{explanabilityType}/score")
//    @Produces(MediaType.APPLICATION_JSON)
//    public DecisionExplanationResponse getDecisionScore(@PathParam("explanabilityType") String explanabilityType, @QueryParam("modelId") String modelId, Map<String, Object> input) {
//        return new DecisionExplanationResponse();
//    }
//}