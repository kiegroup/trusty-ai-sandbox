/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.kie.pmml.kogito.quarkus.example.CommonTestUtils.testDescriptive;
import static org.kie.pmml.kogito.quarkus.example.CommonTestUtils.testResult;

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
        testResult(INPUT_DATA, BASE_PATH, TARGET, "0");
    }

    @Test
    void testEvaluateClusterBuyerPredictorDescriptive() {
        final Map<String, Object> expectedResultMap = Collections.singletonMap(TARGET, "0");
        testDescriptive(INPUT_DATA, BASE_PATH, TARGET, expectedResultMap);
    }

}
