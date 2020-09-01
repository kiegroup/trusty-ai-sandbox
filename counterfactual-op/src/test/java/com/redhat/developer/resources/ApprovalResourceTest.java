package com.redhat.developer.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class ApprovalResourceTest {

  @Test
  public void testDeclinedPrediction() {
    given()
        .body(
            "{\n"
                + "\t\"age\": 20,\n"
                + "\t\"income\": 30000,\n"
                + "\t\"children\": 0,\n"
                + "\t\"daysEmployed\": 100,\n"
                + "\t\"ownRealty\": false,\n"
                + "\t\"workPhone\": true,\n"
                + "\t\"ownCar\": false\n"
                + "}")
        .contentType(ContentType.JSON)
        .when()
        .post("/creditcard/prediction")
        .then()
        .statusCode(200)
        .body("APPROVED", is(0));
  }

  @Test
  public void testApprovedPrediction() {
    given()
        .body(
            "{\n"
                + "\t\"age\": 40,\n"
                + "\t\"income\": 200000,\n"
                + "\t\"children\": 0,\n"
                + "\t\"daysEmployed\": 1500,\n"
                + "\t\"ownRealty\": false,\n"
                + "\t\"workPhone\": true,\n"
                + "\t\"ownCar\": false\n"
                + "}")
        .contentType(ContentType.JSON)
        .when()
        .post("/creditcard/prediction")
        .then()
        .statusCode(200)
        .body("APPROVED", is(1));
  }

  /**
   * Test inputs for which the counterfactual is very likely to be found without breaking the
   * "fixed" field constraints
   */
  @Test
  public void testConstrainedCounterfactual() {
    given()
        .body(
            "{\n"
                + "\t\"age\": 20,\n"
                + "\t\"income\": 50000,\n"
                + "\t\"children\": 0,\n"
                + "\t\"daysEmployed\": 100,\n"
                + "\t\"ownRealty\": false,\n"
                + "\t\"workPhone\": true,\n"
                + "\t\"ownCar\": false\n"
                + "}")
        .contentType(ContentType.JSON)
        .when()
        .post("/creditcard/counterfactual")
        .then()
        .statusCode(200)
        .body("approvalsList[0].age", is(20))
        .and()
        .body("approvalsList[0].children", is(0))
        .and()
        .body("approvalsList[0].income", not(50000));
  }

  /**
   * Test nonsensical inputs for which the counterfactual is impossible to be found without breaking
   * the "fixed" field constraints
   */
  @Test
  public void testUnconstrainedCounterfactual() {
    given()
        .body(
            "{\n"
                + "\t\"age\": 1,\n"
                + "\t\"income\": 0,\n"
                + "\t\"children\": 10,\n"
                + "\t\"daysEmployed\": 0,\n"
                + "\t\"ownRealty\": false,\n"
                + "\t\"workPhone\": true,\n"
                + "\t\"ownCar\": false\n"
                + "}")
        .contentType(ContentType.JSON)
        .when()
        .post("/creditcard/counterfactual")
        .then()
        .statusCode(200)
        .body("approvalsList[0].age", not(1));
  }

  @Test
  public void testScoreBreakdown() {
    given()
        .body(
            "{\n"
                + "\t\"age\": 20,\n"
                + "\t\"income\": 30000,\n"
                + "\t\"children\": 0,\n"
                + "\t\"daysEmployed\": 100,\n"
                + "\t\"ownRealty\": false,\n"
                + "\t\"workPhone\": true,\n"
                + "\t\"ownCar\": false\n"
                + "}")
        .contentType(ContentType.JSON)
        .when()
        .post("/creditcard/breakdown")
        .then()
        .statusCode(200);
  }
}
