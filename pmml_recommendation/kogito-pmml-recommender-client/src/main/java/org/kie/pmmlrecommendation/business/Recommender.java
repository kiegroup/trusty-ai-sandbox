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
package org.kie.pmmlrecommendation.business;

import org.kie.pmmlrecommendation.dto.Customer;
import org.kie.pmmlrecommendation.enums.ItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.kie.pmmlrecommendation.business.Converter.getBuyedItemsIndexes;
import static org.kie.pmmlrecommendation.utils.PMMLUtils.getClusterId;

public class Recommender {

    private static final Logger logger = LoggerFactory.getLogger(Recommender.class);

    private Recommender() {
    }

    public static String getRecommendation(Customer customer) {
        logger.info("getRecommendation {}", customer);
        int[] buyedItems = getBuyedItemsIndexes(customer);
        int clusterId = getClusterId(buyedItems);
        ItemType itemType = ItemType.byClusterId(clusterId);
        return getRecommendation(itemType, customer.getBuyedItems());
    }

    private static String getRecommendation(ItemType itemType, List<String> buyedItems) {
        logger.info("getRecommendation {} {}", itemType, buyedItems);
        List<String> alreadyBuyed = buyedItems
                .stream()
                .filter(buyed -> buyed.startsWith(itemType.getClusterName()))
                .collect(Collectors.toList());
        if (alreadyBuyed.size() == 10) {
            return null;
        }
        return IntStream.range(0, 10)
                .mapToObj(i -> itemType.getClusterName() + "-" + i)
                .filter(itemName -> !alreadyBuyed.contains(itemName))
                .findFirst()
                .orElse(null);

    }
}
