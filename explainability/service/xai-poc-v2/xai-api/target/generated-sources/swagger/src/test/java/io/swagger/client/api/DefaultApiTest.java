/*
 * TrustyXAI
 * Trusty XAI explainability API.
 *
 * OpenAPI spec version: 1.0.0
 * Contact: tteofili@redhat.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.api;

import org.kie.trusty.xai.handler.ApiException;
import org.kie.trusty.xai.model.Prediction;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DefaultApi
 */
@Ignore
public class DefaultApiTest {

    private final DefaultApi api = new DefaultApi();

    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void headTest() throws ApiException {
        Prediction response = api.head();

        // TODO: test validations
    }
    
}