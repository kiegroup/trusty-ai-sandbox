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
package org.kie.pmmlrecommendation;

import org.kie.pmmlrecommendation.dto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.kie.pmmlrecommendation.business.Recommender.getRecommendation;
import static org.kie.pmmlrecommendation.utils.DataSourceUtils.getAllCustomers;

public class MainApp {

    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        List<Customer> customers = getAllCustomers();
        customers.forEach(customer -> {
            logger.info("For customer {}", customer);
            String recommendation = getRecommendation(customer);
            logger.info("we recommend: {}", recommendation);
        });
    }
}
