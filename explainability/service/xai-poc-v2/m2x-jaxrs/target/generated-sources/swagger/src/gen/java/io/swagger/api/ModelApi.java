package io.swagger.api;

import org.kie.trusty.xai.model.*;
import io.swagger.api.ModelApiService;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import org.kie.trusty.xai.model.DataDistribution;
import org.kie.trusty.xai.model.ModelInfo;
import org.kie.trusty.xai.model.PredictionInput;
import org.kie.trusty.xai.model.PredictionOutput;


import java.util.Map;
import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.inject.Inject;


import javax.validation.constraints.*;


@Path("/model")


@io.swagger.annotations.Api(description = "the model API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaResteasyServerCodegen", date = "2020-05-04T14:59:35.742503+02:00[Europe/Prague]")

public class ModelApi  {

    @Inject ModelApiService service;


    @GET
    @Path("/data/distribution")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get the distribution of the data used to train the model", notes = "", response = DataDistribution.class, tags={ "global", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The data distribution", response = DataDistribution.class) })
    public Response dataDistribution(@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.dataDistribution(securityContext);
    }

    @GET
    @Path("/info")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get general information about the model", notes = "", response = ModelInfo.class, tags={ "global", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Information about the model", response = ModelInfo.class) })
    public Response info(@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.info(securityContext);
    }

    @POST
    @Path("/predict")
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/xml", "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Execute model prediction function", notes = "", response = PredictionOutput.class, responseContainer = "List", tags={ "local","global", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Prediction Outputs", response = PredictionOutput.class, responseContainer = "List") })
    public Response predict(@ApiParam(value = "Prediction inputs" ,required=true) List<PredictionInput> body,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.predict(body,securityContext);
    }

}

