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

import java.util.List;

public class Converter {

    private Converter() {
    }

    public static int[] getBuyedItemsIndexes(final Customer customer) {
        int[] toReturn = new int[30];
        final List<String> buyedItems = customer.getBuyedItems();
        buyedItems.forEach(buyedItem -> {
            String[] parts = buyedItem.split("-");
            String clusterName = parts[0];
            int itemId = Integer.parseInt(parts[1]);
            ItemType itemType = ItemType.byClusterName(clusterName);
            itemId += (itemType.getArrayPosition() * 10);
            toReturn[itemId] = 1;
        });
        return toReturn;
    }
}
