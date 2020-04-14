package com.redhat.developer.execution.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.developer.dmn.IDmnService;
import com.redhat.developer.dmn.models.input.InputData;
import com.redhat.developer.dmn.models.input.ModelInputStructure;
import com.redhat.developer.dmn.models.input.TypeComponent;
import com.redhat.developer.dmn.models.input.TypeDefinition;
import com.redhat.developer.execution.models.DMNEventModel;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.responses.decisions.DecisionOutputsResponse;
import com.redhat.developer.execution.responses.decisions.DecisionOutputsStructuredResponse;
import com.redhat.developer.execution.responses.decisions.SingleDecisionOutputResponse;
import com.redhat.developer.execution.responses.decisions.inputs.DecisionInputsResponse;
import com.redhat.developer.execution.responses.decisions.inputs.DecisionStructuredInputsResponse;
import com.redhat.developer.execution.responses.decisions.inputs.SingleDecisionInputResponse;
import com.redhat.developer.execution.responses.execution.ExecutionDetailResponse;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.storage.IExecutionsStorageExtension;
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
    IExecutionsStorageExtension storageExtension;

    @Inject
    IDmnService dmnService;

    @GET
    @Path("/{key}")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision detail header.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExecutionDetailResponse.class))),
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
        List<DMNResultModel> event = storageExtension.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new ExecutionDetailResponse(ExecutionHeaderResponse.fromDMNResultModel(event.get(0)))).build();
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
        List<DMNResultModel> event = storageExtension.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        Map<String, Object> context = event.get(0).context;

        ModelInputStructure dmnInputStructure = dmnService.getDmnInputStructure(event.get(0).modelId);

        List<String> decisionsOutput = context.keySet().stream().filter(x -> !dmnInputStructure.inputData.stream().anyMatch(y -> y.name.equals(x))).collect(Collectors.toList());

        decisionsOutput.stream().forEach(x -> context.remove(x));

        return Response.ok(new DecisionInputsResponse(context)).build();
    }

    @GET
    @Path("/{key}/structuredInputs")
    @APIResponses(value = {
            @APIResponse(description = "Gets the decision inputs with structure.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = DecisionInputsResponse.class))),
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
        DMNResultModel event = storageExtension.getEventsByMatchingId(key).get(0);
        String modelId = event.modelId;
        Map<String, Object> context = event.context;

        ModelInputStructure dmnInputStructure = dmnService.getDmnInputStructure(modelId);

        DecisionStructuredInputsResponse response = new DecisionStructuredInputsResponse();

        response.input = new ArrayList<>();

        for (String inputName : context.keySet()) {
            Optional<InputData> modelInputDataOpt = dmnInputStructure.inputData.stream().filter(x -> x.name.equals(inputName)).findFirst();

            if (!modelInputDataOpt.isPresent()) { // It's a decision
                continue;
            }

            InputData modelInputData = modelInputDataOpt.get();

            if (!modelInputData.isComposite) {
                response.input.add(new SingleDecisionInputResponse(inputName, modelInputData.typeRef, modelInputData.isComposite, modelInputData.isCollection, null, context.get(inputName)));
            } else {
                response.input.add(new SingleDecisionInputResponse(inputName,
                                                                   modelInputData.typeRef,
                                                                   modelInputData.isComposite,
                                                                   modelInputData.isCollection,
                                                                   analyzeComponents(context.get(inputName), modelInputData.typeRef, dmnInputStructure.customTypes),
                                                                   null
                                   )
                );
            }
        }

        return Response.ok(response).build();
    }

    private List<List<SingleDecisionInputResponse>> analyzeComponents(Object value, String typeRef, List<TypeDefinition> definitions) {
        TypeDefinition typeDefinition = definitions.stream().filter(x -> x.typeName.equals(typeRef)).findFirst().orElseThrow(() -> new NoSuchElementException());
        List<List<SingleDecisionInputResponse>> components = new ArrayList<>();

        if (typeDefinition.isCollection) {
            List<Map<String, Object>> ss = (List) value;
            for (Map<String, Object> aa : ss) {
                List<SingleDecisionInputResponse> component = buildComponent(aa, typeDefinition, definitions);
                components.add(component);
            }
        } else {
            Map<String, Object> aa = (Map) value;
            List<SingleDecisionInputResponse> component = buildComponent(aa, typeDefinition, definitions);
            components.add(component);
        }
        return components;
    }

    private List<SingleDecisionInputResponse> buildComponent(Map<String, Object> aa, TypeDefinition typeDefinition, List<TypeDefinition> definitions) {
        List<SingleDecisionInputResponse> component = new ArrayList<>();
        for (Map.Entry<String, Object> entry : aa.entrySet()) {
            String componentInputName = entry.getKey();
            Object componentValue = entry.getValue();
            TypeComponent componentType = typeDefinition.components.stream().filter(x -> x.name.equals(componentInputName)).findFirst().orElseThrow(() -> new NoSuchElementException());
            if (!componentType.isComposite) {
                component.add(new SingleDecisionInputResponse(componentInputName, componentType.typeRef, componentType.isComposite, componentType.isCollection, null, componentValue));
            } else {
                List<List<SingleDecisionInputResponse>> myComponent = analyzeComponents(componentValue, componentType.typeRef, definitions);
                component.add(new SingleDecisionInputResponse(componentInputName, componentType.typeRef, componentType.isComposite, componentType.isCollection, myComponent, null));
            }
        }
        return component;
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
        List<DMNResultModel> event = storageExtension.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new DecisionOutputsResponse(ExecutionHeaderResponse.fromDMNResultModel(event.get(0)), event.get(0).decisions)).build();
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
        List<DMNResultModel> event = storageExtension.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new DecisionOutputsStructuredResponse(ExecutionHeaderResponse.fromDMNResultModel(event.get(0)), event.get(0).decisions)).build();
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
        List<DMNResultModel> event = storageExtension.getEventsByMatchingId(key);

        if (event == null) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Event with id {} does not exist.", key)).build();
        }

        if (event.size() > 1) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), String.format("Multiple events have been retrieved with this ID.", key)).build();
        }

        return Response.ok(new SingleDecisionOutputResponse(event.get(0).decisions.stream().filter(x -> x.decisionId.equals(outcomeId)).findFirst().get())).build();
    }
}