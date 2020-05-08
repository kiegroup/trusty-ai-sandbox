package com.redhat.developer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExplanabilityTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/explain")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}