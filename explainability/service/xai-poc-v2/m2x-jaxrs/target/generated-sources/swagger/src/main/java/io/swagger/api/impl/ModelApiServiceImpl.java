package io.swagger.api.impl;

import io.swagger.api.*;
import org.kie.trusty.xai.model.*;


import org.kie.trusty.xai.model.DataDistribution;
import org.kie.trusty.xai.model.ModelInfo;
import org.kie.trusty.xai.model.PredictionInput;
import org.kie.trusty.xai.model.PredictionOutput;


import java.util.List;
import io.swagger.api.NotFoundException;

import java.io.InputStream;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaResteasyServerCodegen", date = "2020-05-04T14:59:35.742503+02:00[Europe/Prague]")

public class ModelApiServiceImpl implements ModelApiService {
  
      public Response dataDistribution(SecurityContext securityContext)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
      public Response info(SecurityContext securityContext)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
      public Response predict(List<PredictionInput> body,SecurityContext securityContext)
      throws NotFoundException {
      // do some magic!
      return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
  }
  
}

