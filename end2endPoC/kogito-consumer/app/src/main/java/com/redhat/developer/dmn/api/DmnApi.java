package com.redhat.developer.dmn.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.dmn.IDmnService;
import com.redhat.developer.dmn.requests.EvaluationRequestBody;
import com.redhat.developer.dmn.requests.NewDmnModelRequest;
import com.redhat.developer.dmn.responses.AvailableModelsResponse;
import com.redhat.developer.dmn.responses.EvaluationResponse;
import com.redhat.developer.dmn.responses.FullModelResponse;
import com.redhat.developer.dmn.responses.ModelDetail;
import com.redhat.developer.dmn.responses.inputs.ModelInputResponse;
import com.redhat.developer.dmn.storage.dto.DmnModel;
import com.redhat.developer.dmn.utils.MyMD5;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/models")
@RequestScoped
public class DmnApi {

    @Inject
    IStorageManager storageManager;

    @Inject
    IDmnService dmnService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AvailableModelsResponse getModels() {
        List<DmnModel> ids = storageManager.getModelIds();
        return new AvailableModelsResponse(ids.stream().map(x -> ModelDetail.fromStorageModel(x)).collect(Collectors.toList()));
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setModel(NewDmnModelRequest request) {
        DmnModel model = DmnModel.fromNewDmnModelRequest(request, MyMD5.getMd5(request.nameSpace + request.name));
        storageManager.storeModel(model);
        return Response.ok(model.modelId).build();
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FullModelResponse getModelById(@PathParam("id") String id) {
        DmnModel model = storageManager.getModel(id);
        return FullModelResponse.fromStorageModel(model);
    }

    @GET
    @Path("/{id}/inputs")
    @APIResponses(value = {
            @APIResponse(description = "Returns the input structure of the dmn model.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ModelInputResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the structure of the dmn inputs.", description = "Gets the structure of the dmn inputs.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModelInputs(@PathParam("id") String id) {
        return Response.ok(new ModelInputResponse()).build();
    }

    @POST
    @Path("/{id}/evaluate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public EvaluationResponse evaluateDecision(@Parameter(
            name = "id",
            description = "ID of the DMN Model to evaluate.",
            required = true,
            schema = @Schema(implementation = String.class)
    ) @PathParam("id") String id, EvaluationRequestBody inputs) {
        Object o = dmnService.evaluateModel(id, inputs.inputs);
        return new EvaluationResponse(o);
    }
}