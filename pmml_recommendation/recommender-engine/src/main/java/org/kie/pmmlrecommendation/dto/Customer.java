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
package org.kie.pmmlrecommendation.dto;

import org.kie.pmmlrecommendation.enums.ItemType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Customer {

    private final List<String> buyedItems;

    public Customer() {
        ItemType itemType = ItemType.byClusterId(new Random().nextInt(3));
        buyedItems = mainBuyedItems(itemType);
        buyedItems.addAll(casualBuys(itemType));
    }

    public List<String> getBuyedItems() {
        return buyedItems;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("buyedItems=" + buyedItems)
                .toString();
    }

    private List<String> mainBuyedItems(ItemType itemType) {
        List<Integer> list = IntStream.range(0, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(list);
        List<Integer> buyed = list.subList(0, 6);
        return buyed.stream().map(i -> itemType.getClusterName() + "-" + i).collect(Collectors.toList());
    }

    private List<String> casualBuys(ItemType itemType) {
        return IntStream.range(0, 10)
                .mapToObj(i -> casualBuy(itemType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String casualBuy(ItemType itemType) {
        List<ItemType> otherTypes = ItemType.otherTypes(itemType);
        Random random = new Random();
        int rnd = random.nextInt(100);
        if (rnd < 10) {
            ItemType otherType = otherTypes.get(random.nextInt(otherTypes.size()));
            return otherType.getClusterName() + "-" + random.nextInt(10);
        } else {
            return null;
        }
    }
}
