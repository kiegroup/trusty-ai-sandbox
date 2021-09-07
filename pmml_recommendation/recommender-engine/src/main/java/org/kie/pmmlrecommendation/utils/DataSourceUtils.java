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

import org.kie.pmmlrecommendation.dto.Customer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataSourceUtils {

    public static List<Customer> getAllCustomers() {
        return IntStream.range(0, 5).mapToObj(i -> new Customer()).collect(Collectors.toList());
    }
}
