package org.kie.trusty.xai.resource;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.kie.trusty.m2x.model.ModelInfo;
import org.kie.trusty.m2x.model.Prediction;
import org.kie.trusty.xai.impl.builder.ExplanationProviderBuilder;
import org.kie.trusty.xai.impl.explainer.global.viz.PartialDependencePlotProvider;
import org.kie.trusty.xai.impl.explainer.local.saliency.SaliencyLocalExplanationProvider;
import org.kie.trusty.xai.model.Saliency;
import org.kie.trusty.xai.model.TabularData;

@Path("/xai")
public class ExplainResource {

    @POST
    @Path("/saliency/lime")
    @Operation(summary = "Generate a LIME local explanation", tags = "local",
            responses = {
                    @ApiResponse(description = "The saliency explanation",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(
                                    schema = @Schema(implementation = Saliency.class))))})
    public Response lime(
            @Parameter(required = true) Prediction... predictions) {
        List<Saliency> saliencies = new LinkedList<>();
        for (Prediction prediction : predictions) {
            SaliencyLocalExplanationProvider saliencyLocalExplanationProvider = ExplanationProviderBuilder.newExplanationProviderBuilder()
                    .local()
                    .saliency()
                    .lime()
                    .build();
            saliencies.add(saliencyLocalExplanationProvider.explain(prediction));
        }
        return Response.ok().entity(saliencies).build();
    }

    @POST
    @Path("/tabular/pdp")
    @Operation(summary = "Generate PDP plots model explanation", tags = "global",
            responses = {
                    @ApiResponse(description = "PDPs for all features in the model",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TabularData.class)))})
    public Response pdp(
            @Parameter(required = true) ModelInfo modelInfo) {
        PartialDependencePlotProvider partialDependencePlotProvider = ExplanationProviderBuilder.newExplanationProviderBuilder()
                .global()
                .partialDependence()
                .build();
        Collection<TabularData> pdps = partialDependencePlotProvider.explain(modelInfo);
        return Response.ok().entity(pdps).build();
    }
}
