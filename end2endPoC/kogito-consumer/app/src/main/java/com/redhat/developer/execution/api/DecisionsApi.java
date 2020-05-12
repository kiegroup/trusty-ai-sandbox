package com.redhat.developer.execution.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.developer.dmn.IDmnService;
import com.redhat.developer.dmn.models.input.ModelInputStructure;
import com.redhat.developer.execution.IExecutionService;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.models.OutcomeModel;
import com.redhat.developer.execution.models.OutcomeModelWithInputs;
import com.redhat.developer.execution.responses.decisions.OutcomesResponse;
import com.redhat.developer.execution.responses.decisions.OutcomesStructuredResponse;
import com.redhat.developer.execution.responses.decisions.inputs.DecisionInputsResponse;
import com.redhat.developer.execution.responses.decisions.inputs.DecisionStructuredInputsResponse;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/executions/decisions")
public class DecisionsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecisionsApi.class);

    @Inject
    IDmnService dmnService;

    @Inject
    IExecutionService executionService;

    @GET
    @Path("/{key}")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision detail header.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExecutionHeaderResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets The decision header with details.", description = "Gets the decision detail header.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExecutionByKey(
            @Parameter(
                    name = "key",
                    description = "ID of the decision that needs to be fetched",
                    required = true,
                    schema = @Schema(implementation = String.class)
            ) @PathParam("key") String key) {
        List<DMNResultModel> event = executionService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        System.out.println(event.get(0).executionDate);
        return Response.ok(ExecutionHeaderResponse.fromDMNResultModel(event.get(0))).build();
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
        List<DMNResultModel> event = executionService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        Map<String, Object> context = event.get(0).context;

        ModelInputStructure dmnInputStructure = dmnService.getDmnInputStructure(event.get(0).modelId);

        List<String> decisionsOutput = context.keySet().stream().filter(x -> !dmnInputStructure.inputData.stream().anyMatch(y -> y.name.equals(x))).collect(Collectors.toList());

        decisionsOutput.forEach(x -> context.remove(x));

        return Response.ok(new DecisionInputsResponse(context)).build();
    }

    @GET
    @Path("/{key}/structuredInputs")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision inputs with structure.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = DecisionStructuredInputsResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the decision inputs with structure.", description = "Gets the decision inputs with structure.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExecutionStructuredInputs(
            @Parameter(
                    name = "key",
                    description = "ID of the decision that needs to be fetched",
                    required = true,
                    schema = @Schema(implementation = String.class)
            )
            @PathParam("key") String key) {
        DMNResultModel event = executionService.getEventsByMatchingId(key).get(0);

        DecisionStructuredInputsResponse response = executionService.getStructuredInputs(event);

        return Response.ok(response).build();
    }

    @GET
    @Path("/{key}/outcomes")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision outcomes.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = OutcomesResponse.class))),
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
        List<DMNResultModel> event = executionService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        DMNResultModel dmnEvent = event.get(0);

        List<SingleDecisionInputResponse> structuredOutcomesValues = executionService.getStructuredOutcomesValues(dmnEvent);
        Map<String, SingleDecisionInputResponse> mmap = new HashMap<>();
        structuredOutcomesValues.forEach(x -> mmap.put(x.inputName, x));

        dmnEvent.decisions.forEach(x -> x.result = mmap.get(x.outcomeName));
        OutcomeModel outcomeModel = dmnEvent.decisions.stream().filter(x -> x.outcomeName.equals(structuredOutcomesValues.get(0).inputName)).findFirst().get();
        dmnEvent.decisions.remove(outcomeModel);
        dmnEvent.decisions.add(0, outcomeModel);

        return Response.ok(new OutcomesResponse(ExecutionHeaderResponse.fromDMNResultModel(dmnEvent), dmnEvent.decisions)).build();
    }

    @GET
    @Path("/{key}/structuredOutcomes")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision outcomes.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = OutcomesStructuredResponse.class))),
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
        List<DMNResultModel> event = executionService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new OutcomesStructuredResponse(ExecutionHeaderResponse.fromDMNResultModel(event.get(0)), event.get(0).decisions)).build();
    }

    @GET
    @Path("/{key}/outcomes/{outcomeId}")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision outcome detail.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = OutcomeModelWithInputs.class))),
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
        // TODO HUGE REFACTORING
        List<DMNResultModel> event = executionService.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        OutcomeModelWithInputs response = executionService.getStructuredOutcome(outcomeId, event.get(0));

        return Response.ok(response).build();
    }
}