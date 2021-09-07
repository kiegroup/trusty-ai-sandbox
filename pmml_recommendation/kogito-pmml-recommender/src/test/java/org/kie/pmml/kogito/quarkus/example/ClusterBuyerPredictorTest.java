/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.pmml.kogito.quarkus.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ClusterBuyerPredictorTest {

    private static final String BASE_PATH = "/KMeans";
    private static final String TARGET = "targetKMeans";

    private static final String INPUT_DATA;

    static {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (int i = 0; i < 30; i ++) {
            int toAdd = i % 2 == 0 ? 0 : 1;
            String end = i < 29 ? ", " : "";
            builder.append(String.format("\"%s\": %s.0%s", i, toAdd, end));
        }
        builder.append("}");
        INPUT_DATA = builder.toString();
    }

    @Test
    void testEvaluateClusterBuyerPredictorResult() {
        given()
                .contentType(ContentType.JSON)
                .body(INPUT_DATA)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(200)
                .body(TARGET, is("0"));
    }

    @Test
    void testEvaluateClusterBuyerPredictorDescriptive() {
        final Map<String, Object> expectedResultMap = Collections.singletonMap(TARGET, "0");
        String path = BASE_PATH + "/descriptive";
        Object resultVariables = given()
                .contentType(ContentType.JSON)
                .body(INPUT_DATA)
                .when()
                .post(path)
                .then()
                .statusCode(200)
                .body("correlationId", is(new IsNull()))
                .body("segmentationId", is(new IsNull()))
                .body("segmentId", is(new IsNull()))
                .body("segmentIndex", is(0)) // as JSON is not schema aware, here we assert the RAW string
                .body("resultCode", is("OK"))
                .body("resultObjectName", is(TARGET))
                .extract()
                .path("resultVariables");
        assertNotNull(resultVariables);
        assertTrue(resultVariables instanceof Map);
        Map<String, Object> mappedResultVariables = (Map) resultVariables;
        expectedResultMap.forEach((key, value) -> {
            assertTrue(mappedResultVariables.containsKey(key));
            assertEquals(value, mappedResultVariables.get(key));
        });
    }

}
