package com.redhat.developer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.ws.rs.NotFoundException;

import com.redhat.developer.models.ExecutionResponse;
import com.redhat.developer.models.decisions.DecisionInputsResponse;
import com.redhat.developer.models.decisions.DecisionStructuredInputsResponse;
import com.redhat.developer.models.decisions.FeatureImportance;
import com.redhat.developer.models.decisions.OutcomeModelWithInputs;
import com.redhat.developer.models.decisions.OutcomesResponse;
import com.redhat.developer.models.decisions.Saliency;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TrustyIntegrationTest {
    private static final String kogitoAppEndpoint = "http://localhost:1337";
    private static final String trustyEndpoint = "http://localhost:1336";
    private static final Logger LOGGER = LoggerFactory.getLogger(TrustyIntegrationTest.class);

    private static String executionId = null;
    private static String riskScoreOutcomeId = null;

    @Test
    @Order(1)
    public void createModel() throws IOException {
        LOGGER.info("Test 1: Create model");
        InputStream resourceAsStream = TrustyIntegrationTest.class.getResourceAsStream("/modelRequest.json");
        StringWriter writer = new StringWriter();
        IOUtils.copy(resourceAsStream, writer, "UTF-8");
        String body = writer.toString();

        given().contentType(ContentType.JSON).body(body)
                .when().post(trustyEndpoint + "/models")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    public void createDecision() {
        LOGGER.info("Test 2: create decision");
        String body = new JsonObject()
                .put("Applicant", new JsonObject().put("First Name", "jacopo").put("Last Name", "r00ta").put("Age" , 28).put("Email", "s3cret"))
                .put("Mortgage Request", new JsonObject().put("TotalRequired", 100000).put("NumberInstallments", 100))
                .put("Finantial Situation", new JsonObject().put("MonthlySalary", 10000).put("TotalAsset", 100000)).toString();

        given().contentType(ContentType.JSON).body(body).when().post(kogitoAppEndpoint + "/myMortgage").then().statusCode(200);
    }

    @Test
    @Order(3)
    public void retrieveDecisionFromTrustyService() throws InterruptedException {
        LOGGER.info("Test 3: retrieve execution list");
        LOGGER.info(given().when().get(trustyEndpoint + "/executions")
                .then().contentType(ContentType.JSON).extract().response().jsonPath().prettyPrint());

        retryUntilSuccess(
                () -> given().when().get(trustyEndpoint + "/executions")
                        .then().contentType(ContentType.JSON).extract().response().jsonPath().getObject("$", ExecutionResponse.class).headers.size() >= 1);

        executionId = given()
                .when().get(trustyEndpoint + "/executions").then().extract().jsonPath().getObject("$", ExecutionResponse.class).headers.get(0).executionId;

        Assertions.assertNotNull(executionId);
    }

    @Test
    @Order(4)
    public void retrieveInputs() throws InterruptedException {
        LOGGER.info("Test 4: retrieve inputs of decision " + executionId);
        LOGGER.info(given().when().get(trustyEndpoint + "/executions/decisions/" + executionId + "/inputs")
                            .then().extract().response().asString());

        DecisionInputsResponse response =  executeUntilSuccess(
                    () -> given().when().get(trustyEndpoint + "/executions/decisions/" + executionId + "/inputs")
                ).then().extract().jsonPath().getObject("$", DecisionInputsResponse.class);


        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.inputs);
        Assertions.assertTrue(response.inputs.keySet().size() > 0);

        DecisionStructuredInputsResponse structuredInputsResponse =  given()
                .when().get(trustyEndpoint + "/executions/decisions/" + executionId + "/structuredInputs").then().extract().jsonPath().getObject("$", DecisionStructuredInputsResponse.class);

        Assertions.assertNotNull(structuredInputsResponse);
        Assertions.assertTrue(structuredInputsResponse.input.size() >= 3);
    }

    @Test
    @Order(5)
    public void retrieveDecisionOutcomes() throws InterruptedException {
        LOGGER.info("Test 5: retrieve outcomes");
        LOGGER.info(given().when().get(trustyEndpoint + "/executions/decisions/" + executionId + "/outcomes")
                            .then().extract().response().asString());

        OutcomesResponse outcomes =  given()
                .when().get(trustyEndpoint + "/executions/decisions/" + executionId + "/outcomes").then().extract().jsonPath().getObject("$", OutcomesResponse.class);

        Assertions.assertNotNull(outcomes);
        Assertions.assertNotNull(outcomes.decisions);
        Assertions.assertNotNull(outcomes.decisions.get(0).outcomeName.equals("Risk Score"));
        riskScoreOutcomeId = outcomes.decisions.get(0).outcomeId;
    }

    @Test
    @Order(6)
    public void retrieveDecisionOucomesInputs() throws InterruptedException {
        LOGGER.info("Test 6: retrieve inputs of outcome");
        OutcomeModelWithInputs outcomes =  given()
                .when().get(trustyEndpoint + "/executions/decisions/" + executionId + "/outcomes/" + riskScoreOutcomeId).then().extract().jsonPath().getObject("$", OutcomeModelWithInputs.class);

        Assertions.assertNotNull(outcomes);
        Assertions.assertNotNull(outcomes.decisionInputs);
        Assertions.assertNotNull(outcomes.decisionInputs.size() >= 3);
    }

    @Test
    @Order(7)
    public void retrieveFeatureImportance() throws InterruptedException {
        LOGGER.info("Test 7: retrieve feature importance");

        retryUntilSuccess(
                () -> given().when().get(trustyEndpoint + "/executions/decisions/" + executionId + "/featureImportance")
                        .then().contentType(ContentType.JSON).extract().response().jsonPath().getObject("$", Saliency.class).featureImportance.size() >= 1);

        Saliency saliency =  given()
                .when().get(trustyEndpoint + "/executions/decisions/" + executionId + "/featureImportance").then().extract().jsonPath().getObject("$", Saliency.class);

        Assertions.assertNotNull(saliency);
        Assertions.assertNotNull(saliency.featureImportance);
        for (FeatureImportance fi : saliency.featureImportance){
            Assertions.assertNotNull(fi.featureScore);
        }
    }

    private Response executeUntilSuccess(Callable callable) throws InterruptedException {
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        for (int i = 0; i <= 60; i++) {
            try {
                Future<Response> future = executor.submit(callable);
                Response response = future.get();
                if (response.statusCode() == 200) {
                    return response;
                }
            } catch (Exception e) {
                LOGGER.info("Request failed due to " + e.getMessage());
            }
            Thread.sleep(1000);
        }
        throw new NotFoundException("Request did not success.");
    }

    private boolean retryUntilSuccess(Callable callable) throws InterruptedException {
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        for (int i = 0; i <= 60; i++) {
            try {
                Future<Boolean> future = executor.submit(callable);
                Boolean assertionResult = future.get();
                if (assertionResult) {
                    return true;
                }
            } catch (Exception e) {
                LOGGER.info("Request failed due to " + e.getMessage());
            }
            Thread.sleep(1000);
        }
        throw new NotFoundException("Can not retrieve the right data after 20 seconds.");
    }
}