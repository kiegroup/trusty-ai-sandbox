package com.redhat.developer.execution.api;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.developer.database.IEventStorage;
import com.redhat.developer.execution.responses.decisions.DecisionInputsResponse;
import com.redhat.developer.execution.responses.decisions.DecisionOutputsResponse;
import com.redhat.developer.execution.responses.decisions.DecisionOutputsStructuredResponse;
import com.redhat.developer.execution.responses.decisions.SingleDecisionOutputResponse;
import com.redhat.developer.execution.responses.execution.ExecutionDetailResponse;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.storage.model.DMNEventModel;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("/executions/decisions")
public class DecisionsApi {

    @Inject
    IEventStorage storageService;

    @GET
    @Path("/{key}")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision detail header.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExecutionDetailResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets The decision header with details.", description = "Gets the decision detail header.")
    @Produces(MediaType.APPLICATION_JSON)
    @Parameter(
            name = "key",
            description = "ID of the decision that needs to be fetched",
            required = true,
            schema = @Schema(implementation = String.class)
    )
    @PathParam("key")
    public Response getExecutionByKey(String key) {
        List<DMNEventModel> event = storageService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if(event.size() > 1){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new ExecutionDetailResponse(ExecutionHeaderResponse.fromDMNResultModel(event.get(0).data.result))).build();
    }

    @GET
    @Path("/{key}/inputs")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision inputs.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = DecisionInputsResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the decision inputs.", description = "Gets the decision inputs.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExecutionInputs(
            @Parameter(
                    name = "key",
                    description = "ID of the decision that needs to be fetched",
                    required = true,
                    schema = @Schema(implementation = String.class)
            )
            @PathParam("key") String key) {
        List<DMNEventModel> event = storageService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if(event.size() > 1){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new DecisionInputsResponse(ExecutionHeaderResponse.fromDMNResultModel(event.get(0).data.result), event.get(0).data.result)).build();
    }

    @GET
    @Path("/{key}/outcomes")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision outcomes.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = DecisionOutputsResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the decision outcomes.", description = "Gets the decision outcomes.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExecutionOutcome(
            @Parameter(
                    name = "key",
                    description = "ID of the decision that needs to be fetched",
                    required = true,
                    schema = @Schema(implementation = String.class)
            )
            @PathParam("key") String key) {
        List<DMNEventModel> event = storageService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if(event.size() > 1){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new DecisionOutputsResponse(ExecutionHeaderResponse.fromDMNResultModel(event.get(0).data.result), event.get(0).data.result.decisions)).build();
    }

    @GET
    @Path("/{key}/structuredOutcomes")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision outcomes.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = DecisionOutputsStructuredResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the decision outcomes with model structure. To be implemented.", description = "Gets the decision outcomes in a structure that reflects the dmn model structure.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExecutionGraphOutcomes(
            @Parameter(
                    name = "key",
                    description = "ID of the decision that needs to be fetched",
                    required = true,
                    schema = @Schema(implementation = String.class)
            )
            @PathParam("key") String key) {
        List<DMNEventModel> event = storageService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if(event.size() > 1){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new DecisionOutputsStructuredResponse(ExecutionHeaderResponse.fromDMNResultModel(event.get(0).data.result), event.get(0).data.result.decisions)).build();
    }

    @GET
    @Path("/{key}/outcomes/{outcomeId}")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision outcome detail.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = DecisionOutputsResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the decision outcomes with model structure.", description = "Gets the decision outcomes in a structure that reflects the dmn model structure.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExecutionOutcomeById(
            @Parameter(
                    name = "key",
                    description = "ID of the decision that needs to be fetched",
                    required = true,
                    schema = @Schema(implementation = String.class)
            )
            @PathParam("key") String key,
            @Parameter(
                    name = "outcomeId",
                    description = "ID of the decision that needs to be fetched",
                    required = true,
                    schema = @Schema(implementation = String.class)
            )
            @PathParam("outcomeId") String outcomeId) {
        List<DMNEventModel> event = storageService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if(event.size() > 1){
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new SingleDecisionOutputResponse(event.get(0).data.result.decisions.stream().filter(x -> x.decisionId.equals(outcomeId)).findFirst().get())).build();
    }
}