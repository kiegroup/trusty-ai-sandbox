package com.redhat.developer.decision.api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.decision.dto.DMNResult;
import com.redhat.developer.decision.responses.DecisionDetailResponse;
import com.redhat.developer.decision.responses.DecisionsResponse;
import com.redhat.developer.decision.responses.EvaluationResponse;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/events")
public class DecisionsApi {

    @Inject
    IEventStorage storageService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DecisionsResponse getDecisions() {
        List<DMNResult> results = storageService.getDecisions();
        List<EvaluationResponse> evaluationResponses = new ArrayList<>();
        results.forEach(x -> evaluationResponses.add(new EvaluationResponse(x.evaluationId, x.evaluationDate, x.decisions)));
        return new DecisionsResponse(0, 0, 0, evaluationResponses);
    }

    @GET
    @Path("/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public DecisionDetailResponse getDecisionByKey(@PathParam("key") String key) {
        return new DecisionDetailResponse(key, storageService.getEvent(key));
    }
}
