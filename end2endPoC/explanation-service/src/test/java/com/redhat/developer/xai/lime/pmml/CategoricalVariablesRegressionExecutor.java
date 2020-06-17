/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package com.redhat.developer.xai.lime.pmml;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.evaluator.api.executor.PMMLRuntime;

public class CategoricalVariablesRegressionExecutor extends AbstractPMMLExecutor {

    private static final String MODEL_NAME = "categoricalVariables_Model";

    private final String x;
    private final String y;

    public CategoricalVariablesRegressionExecutor(String x, String y) {
        this.x = x;
        this.y = y;
    }

    private static double regressionFunction(String x, String y) {
        final Map<String, Double> categoriesMapX = new HashMap<>();
        categoriesMapX.put("red", 5.5);
        categoriesMapX.put("green", 15.0);
        categoriesMapX.put("blue", 12.0);
        categoriesMapX.put("orange", 5.5);
        categoriesMapX.put("yellow", -100.25);

        final Map<String, Double> categoriesMapY = new HashMap<>();
        categoriesMapY.put("classA", 0.0);
        categoriesMapY.put("classB", 20.0);
        categoriesMapY.put("classC", 40.0);

        return categoriesMapX.get(x) + categoriesMapY.get(y) - 22.1;
    }

    @Override
    public PMML4Result execute(final PMMLRuntime pmmlRuntime) {
        final Map<String, Object> inputData = new HashMap<>();
        inputData.put("x", x);
        inputData.put("y", y);
        return evaluate(pmmlRuntime, inputData, MODEL_NAME);
    }
}
