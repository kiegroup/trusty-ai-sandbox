package org.kie.trusty;

import java.util.Collection;

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
import org.kie.trusty.xai.explainer.global.viz.PartialDependencePlotProvider;
import org.kie.trusty.xai.model.ModelInfo;
import org.kie.trusty.xai.model.TabularData;

@Path("/global")
@Api(description = "the global API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2020-04-24T15:20:56.942+02:00")
public class PDPEndpoint {

    @POST
    @Path("/tabular/pdp")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Generate PDP explanation method on a model", notes = "", response = TabularData.class, authorizations = {
            @Authorization(value = "trusty_auth", scopes = {
                    @AuthorizationScope(scope = "write:exp", description = "execute explanations"),
                    @AuthorizationScope(scope = "read:exp", description = "read explanations")
            })
    }, tags = {"global"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful explanation", response = TabularData.class)})
    public Response pdp(@Valid ModelInfo modelInfo) {
        PartialDependencePlotProvider partialDependencePlotProvider = ExplanationProviderBuilder.newExplanationProviderBuilder()
                .global()
                .partialDependence()
                .build();
        Collection<TabularData> pdps = partialDependencePlotProvider.explain(modelInfo);
        return Response.ok().entity(pdps).build();
    }
}
