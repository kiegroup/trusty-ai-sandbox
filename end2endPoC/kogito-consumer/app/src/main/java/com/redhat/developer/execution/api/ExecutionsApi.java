package com.redhat.developer.execution.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.execution.IExecutionService;
import com.redhat.developer.execution.models.DMNResultModel;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.responses.execution.ExecutionResponse;
import com.redhat.developer.execution.storage.ExecutionsStorageExtension;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/executions")
public class ExecutionsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionsApi.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Inject
    IExecutionService executionService;

    @GET
    @APIResponses(value = {
            @APIResponse(description = "Returns the execution headers.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExecutionResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the execution headers", description = "Gets the execution headers.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExecutions(
            @Parameter(
                    name = "from",
//                    description = "Start datetime for the lookup. Date in the format \"yyyy-MM-dd'T'HH:mm:ss.SSS\"",
                    description = "Start datetime for the lookup. Date in the format \"yyyy-MM-dd\"",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("yesterday") @QueryParam("from") String from,
            @Parameter(
                    name = "to",
//                    description = "End datetime for the lookup. Date in the format \"yyyy-MM-dd'T'HH:mm:ss.SSS\"",
                    description = "End datetime for the lookup. Date in the format \"yyyy-MM-dd\"",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("now") @QueryParam("to") String to,
            @Parameter(
                    name = "limit",
                    description = "Maximum number of results to return.",
                    required = false,
                    schema = @Schema(implementation = Integer.class)
            ) @DefaultValue("100") @QueryParam("limit") int limit,
            @Parameter(
                    name = "offset",
                    description = "Offset for the pagination.",
                    required = false,
                    schema = @Schema(implementation = Integer.class)
            ) @DefaultValue("0") @QueryParam("offset") int offset,
            @Parameter(
                    name = "search",
                    description = "Execution ID prefix to be matched",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("") @QueryParam("search") String prefix) throws ParseException {

        Long fromTimestamp;
        if (from.equals("yesterday")) {
            fromTimestamp = LocalDate.now().atStartOfDay().minusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli();
        }
        else{
            fromTimestamp = LocalDate.parse(from, formatter).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        }

        Long toTimestamp;
        if (to.equals("now")) {
            toTimestamp = LocalDate.now().atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli();
        } else {
            toTimestamp = LocalDate.parse(to, formatter).atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli();
        }

        LOGGER.info("From: " + fromTimestamp);
        LOGGER.info("To: " + toTimestamp);

        List<DMNResultModel> results;

        try {
            results = executionService.getDecisions(fromTimestamp, toTimestamp, prefix);
            results.sort(Comparator.comparing(DMNResultModel::getExecutionTimestamp).reversed());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.status(400, String.format("Bad request: {}", e.getMessage())).build();
        }

        int totalResults = results.size();
        if (totalResults >= limit) {
            results = results.subList(offset, Math.min(results.size(), offset + limit));
        }

        results.forEach(x -> {
            try {
                System.out.println(new ObjectMapper().writeValueAsString(x));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        List<ExecutionHeaderResponse> executionResponses = new ArrayList<>();
        results.forEach(x -> executionResponses.add(ExecutionHeaderResponse.fromDMNResultModel(x)));
        return Response.ok(new ExecutionResponse(totalResults, limit, offset, executionResponses)).build();
    }

    @GET
    @Path("/decisions")
    @APIResponses(value = {
            @APIResponse(description = "Returns the execution headers of decisions.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExecutionResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the execution headers of the decisions only. To be implemented", description = "Gets the execution headers of the decisions only.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDecisions(
            @Parameter(
                    name = "from",
                    description = "Start datetime for the lookup. Date in the format \"yyyy-MM-dd'T'HH:mm:ss.SSS\"",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("yesterday") @QueryParam("from") String from,
            @Parameter(
                    name = "to",
                    description = "End datetime for the lookup. Date in the format \"yyyy-MM-dd'T'HH:mm:ss.SSS\"",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("now") @QueryParam("to") String to,
            @Parameter(
                    name = "limit",
                    description = "Maximum number of results to return.",
                    required = false,
                    schema = @Schema(implementation = Integer.class)
            ) @DefaultValue("100") @QueryParam("limit") int limit,
            @Parameter(
                    name = "offset",
                    description = "Offset for the pagination.",
                    required = false,
                    schema = @Schema(implementation = Integer.class)
            ) @DefaultValue("0") @QueryParam("offset") int offset,
            @Parameter(
                    name = "search",
                    description = "Execution ID prefix to be matched",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("") @QueryParam("search") String prefix) {
        return Response.status(500, "NOT IMPLEMENTED").build();
    }

    @GET
    @Path("/processes")
    @APIResponses(value = {
            @APIResponse(description = "Returns the execution headers of the processes only.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExecutionResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets the execution headers of the processes only. To be implemented.", description = "Gets the execution headers of the processes only.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProcesses(
            @Parameter(
                    name = "from",
                    description = "Start datetime for the lookup. Date in the format \"yyyy-MM-dd'T'HH:mm:ss.SSS\"",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("yesterday") @QueryParam("from") String from,
            @Parameter(
                    name = "to",
                    description = "End datetime for the lookup. Date in the format \"yyyy-MM-dd'T'HH:mm:ss.SSS\"",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("now") @QueryParam("to") String to,
            @Parameter(
                    name = "limit",
                    description = "Maximum number of results to return.",
                    required = false,
                    schema = @Schema(implementation = Integer.class)
            ) @DefaultValue("100") @QueryParam("limit") int limit,
            @Parameter(
                    name = "offset",
                    description = "Offset for the pagination.",
                    required = false,
                    schema = @Schema(implementation = Integer.class)
            ) @DefaultValue("0") @QueryParam("offset") int offset,
            @Parameter(
                    name = "search",
                    description = "Execution ID prefix to be matched",
                    required = false,
                    schema = @Schema(implementation = String.class)
            ) @DefaultValue("") @QueryParam("search") String prefix) {
        return Response.status(500, "NOT IMPLEMENTED").build();
    }

    @GET
    @Path("/search")
    @APIResponses(value = {
            @APIResponse(description = "Returns the execution headers filtered by ID partial match.", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(type = SchemaType.OBJECT, implementation = ExecutionResponse.class))),
            @APIResponse(description = "Bad Request", responseCode = "400", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    }
    )
    @Operation(summary = "Gets  the execution headers filtered by ID partial match.", description = "Gets the execution headers filtered by ID partial match.")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(
            @Parameter(
                    name = "limit",
                    description = "Maximum number of results to return.",
                    required = false,
                    schema = @Schema(implementation = Integer.class)
            ) @DefaultValue("100") @QueryParam("limit") int limit,
            @Parameter(
                    name = "offset",
                    description = "Offset for the pagination.",
                    required = false,
                    schema = @Schema(implementation = Integer.class)
            ) @DefaultValue("0") @QueryParam("offset") int offset,
            @Parameter(
                    name = "id",
                    description = "ID substring to match",
                    required = true,
                    schema = @Schema(implementation = String.class)
            ) @NotNull @QueryParam("id") String id) {
        List<DMNResultModel> results = executionService.getEventsByMatchingId(id);

        int totalResults = results.size();
        if (totalResults >= limit) {
            results.sort(Comparator.comparing(DMNResultModel::getExecutionTimestamp));
            results = results.subList(offset, Math.min(results.size(), offset + limit));
        }

        List<ExecutionHeaderResponse> executionResponses = new ArrayList<>();
        results.forEach(x -> executionResponses.add(ExecutionHeaderResponse.fromDMNResultModel(x)));
        return Response.ok(new ExecutionResponse(totalResults, limit, offset, executionResponses)).build();
    }
}
