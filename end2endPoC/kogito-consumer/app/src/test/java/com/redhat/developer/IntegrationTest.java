package com.redhat.developer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.developer.database.IStorageManager;
import com.redhat.developer.execution.responses.execution.ExecutionHeaderResponse;
import com.redhat.developer.execution.responses.execution.ExecutionResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static String decisionId;

    @Inject
    IStorageManager storageManager;

    @Test
    @Order(1)
    public void createModel() throws IOException {
        InputStream resourceAsStream = IntegrationTest.class.getResourceAsStream("/modelRequest.json");
        StringWriter writer = new StringWriter();
        IOUtils.copy(resourceAsStream, writer, "UTF-8");
        String body = writer.toString();

        given().contentType(ContentType.JSON).body(body)
                .when().post("/models")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    public void executeDecision() throws IOException {
        String body = "{\"Client\": {\"age\": 43,\"salary\": 1950,\"existing payments\": 100},\"Loan\": {\"duration\": 15,\"installment\": 180}, \"God\" : \"Yes\", \"Bribe\": 1000}";

        given().contentType(ContentType.JSON).body(body)
                .when().post("http://localhost:1336/LoanEligibility")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(3)
    public void getAvailableDecisions() throws IOException, InterruptedException {
        ExecutionResponse response = null;

        // Wait until the event is processed and stored
        for (int i = 0; i <= 15; i++) {
            response = given().when().get("/executions?from=yesterday&limit=100&to=now")
                    .then().contentType(ContentType.JSON).extract().response().jsonPath().getObject("$", ExecutionResponse.class);
            if (response.headers.size() > 0) {
                break;
            }
            Thread.sleep(1000);
        }

        Assertions.assertNotNull(response.headers);
        Assertions.assertEquals(1, response.headers.size());
        Assertions.assertEquals(100, response.limit);
        Assertions.assertEquals(1, response.total);

        ExecutionHeaderResponse executionHeaderResponse = response.headers.get(0);
        Assertions.assertTrue(executionHeaderResponse.executionSucceeded);

        decisionId = executionHeaderResponse.executionId;
    }

    @Test
    @Order(4)
    public void getDecisionByKey() {
        given().when().get("/executions/decisions/" + decisionId)
                .then().statusCode(200);
        given().when().get("/executions/decisions/" + decisionId + "/inputs")
                .then().statusCode(200);
        given().when().get("/executions/decisions/" + decisionId + "/outcomes")
                .then().statusCode(200);
        given().when().get("/executions/decisions/" + decisionId + "/structuredInputs")
                .then().statusCode(200);
    }

    @Test
    @Order(5)
    public void cleanUp() {
        storageManager.deleteIndex("dmneventdata");
        storageManager.deleteIndex("dmnmodeldata");
    }
}
