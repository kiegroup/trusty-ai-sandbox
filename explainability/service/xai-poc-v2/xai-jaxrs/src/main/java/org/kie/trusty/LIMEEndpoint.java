package org.kie.trusty;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.kie.trusty.xai.builder.ExplanationProviderBuilder;
import org.kie.trusty.xai.explainer.local.saliency.SaliencyLocalExplanationProvider;
import org.kie.trusty.xai.model.Prediction;
import org.kie.trusty.xai.model.Saliency;

@Path("/local")
@Api(description = "the local API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2020-04-24T16:42:02.512+02:00")
public class LIMEEndpoint {

    @POST
    @Path("/saliency/lime")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Execute LIME explanation method on a prediction", notes = "", response = Saliency.class, authorizations = {
        @Authorization(value = "trusty_auth", scopes = {
            @AuthorizationScope(scope = "write:exp", description = "execute explanations"),
            @AuthorizationScope(scope = "read:exp", description = "read explanations")
        })
    }, tags={ "local" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful explanation", response = Saliency.class) })
    public Response lime(@Valid Prediction prediction) {
        SaliencyLocalExplanationProvider saliencyLocalExplanationProvider = ExplanationProviderBuilder.newExplanationProviderBuilder()
                .local()
                .saliency()
                .lime()
                .build();
        return Response.ok().entity(saliencyLocalExplanationProvider.explain(prediction)).build();
    }
}
