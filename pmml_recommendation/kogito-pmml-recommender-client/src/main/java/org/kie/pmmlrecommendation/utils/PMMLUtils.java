/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.pmmlrecommendation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class PMMLUtils {

    private static final Logger logger = LoggerFactory.getLogger(PMMLUtils.class);
    private static final String BASE_PATH = "http://localhost:8080/KMeans";
    private static final String OUTPUT_FIELD = "targetKMeans";


    private PMMLUtils() {
    }

    public static int getClusterId(int[] buyedItems) {
        logger.info("getClusterId {}", buyedItems);
        Map<String, Object> inputData = new HashMap<>();
        for (int i = 0; i < buyedItems.length ; i ++) {
            inputData.put(String.valueOf(i), (double) buyedItems[i]);
        }
        String inputDataString = getInputDataString(inputData);
        try {
            String clusterIdName = evaluate(inputDataString);
            logger.info("clusterIdName {}", clusterIdName);
            return Integer.parseInt(clusterIdName);
        } catch (Exception e) {
            logger.error("Failed to retrieve clusterIdName", e);
            throw new RuntimeException(e);
        }
    }

    private static String evaluate(final String inputData) throws URISyntaxException, IOException, InterruptedException {
        logger.info("evaluate with PMMLRuntime {}", inputData);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_PATH))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputData))
                .build();
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("response {}", response);
        if (response.statusCode() != 200) {
            String message = String.format("Status code: %s; body: %s", response.statusCode(), response.body());
            throw new RuntimeException(message);
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.body());
        return jsonNode.get(OUTPUT_FIELD).asText();
    }


    private static String getInputDataString(final Map<String, Object> inputData) {
        return inputData.keySet().stream()
                .map(key -> "\"" + key + "\"" + ": " + inputData.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
