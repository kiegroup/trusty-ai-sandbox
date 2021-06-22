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

import org.apache.commons.io.FileUtils;
import org.kie.api.pmml.PMML4Result;
import org.kie.api.pmml.PMMLRequestData;
import org.kie.pmml.api.runtime.PMMLContext;
import org.kie.pmml.api.runtime.PMMLRuntime;
import org.kie.pmml.evaluator.assembler.factories.PMMLRuntimeFactoryImpl;
import org.kie.pmml.evaluator.core.PMMLContextImpl;
import org.kie.pmml.evaluator.core.utils.PMMLRequestDataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class PMMLUtils {

    private static final Logger logger = LoggerFactory.getLogger(PMMLUtils.class);
    private static final String PMML_FILENAME = "cluster_buyer_predictor.pmml";
    private static final String MODEL_NAME = "KMeans";
    private static final String OUTPUT_FIELD = "cluster";

    private static final PMMLRuntime PMML_RUNTIME;


    static {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final URL pmmlUrl = classloader.getResource(PMML_FILENAME);
        File pmmlFile = FileUtils.getFile(pmmlUrl.getFile());
        PMML_RUNTIME = new PMMLRuntimeFactoryImpl().getPMMLRuntimeFromFile(pmmlFile);
    }

    private PMMLUtils() {
    }

    public static int getClusterId(int[] buyedItems) {
        logger.info("getClusterId {}", buyedItems);
        Map<String, Object> inputData = new HashMap<>();
        for (int i = 0; i < buyedItems.length ; i ++) {
            inputData.put(String.valueOf(i), (double) buyedItems[i]);
        }
        PMML4Result pmml4Result = evaluate(PMML_RUNTIME, inputData, MODEL_NAME);
        logger.info("pmml4Result {}", pmml4Result);
        String clusterIdName = (String) pmml4Result.getResultVariables().get(OUTPUT_FIELD);
        return Integer.parseInt(clusterIdName);
    }

    private static PMML4Result evaluate(final PMMLRuntime pmmlRuntime, final Map<String, Object> inputData, final String modelName) {
        logger.info("evaluate with PMMLRuntime {}", pmmlRuntime);
        final PMMLRequestData pmmlRequestData = getPMMLRequestData(modelName, inputData);
        final PMMLContext pmmlContext = new PMMLContextImpl(pmmlRequestData);
        return pmmlRuntime.evaluate(modelName, pmmlContext);
    }

    private static PMMLRequestData getPMMLRequestData(String modelName, Map<String, Object> parameters) {
        String correlationId = "CORRELATION_ID";
        PMMLRequestDataBuilder pmmlRequestDataBuilder = new PMMLRequestDataBuilder(correlationId, modelName);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object pValue = entry.getValue();
            Class class1 = pValue.getClass();
            pmmlRequestDataBuilder.addParameter(entry.getKey(), pValue, class1);
        }
        return pmmlRequestDataBuilder.build();
    }
}
