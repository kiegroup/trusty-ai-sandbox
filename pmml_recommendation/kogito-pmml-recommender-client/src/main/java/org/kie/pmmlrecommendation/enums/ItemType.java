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
package org.kie.pmmlrecommendation.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ItemType {

    BOOK(2, 0,"Book"),
    CAR(1, 1, "Car"),
    PC(0, 2, "PC");

    private final int clusterId;
    private final int arrayPosition;
    private final String clusterName;

    ItemType(int clusterId, int arrayPosition, String clusterName) {
        this.clusterId = clusterId;
        this.arrayPosition = arrayPosition;
        this.clusterName = clusterName;
    }

    public static ItemType byClusterId(int clusterId) {
        return Arrays.stream(ItemType.values())
                .filter(value -> value.clusterId == clusterId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to find ItemType with clusterId: " + clusterId));
    }

    public static ItemType byClusterName(String clusterName) {
        return Arrays.stream(ItemType.values())
                .filter(value -> value.clusterName.equals(clusterName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to find ItemType with clusterName: " + clusterName));
    }

    public static List<ItemType> otherTypes(ItemType toExclude) {
        return Arrays.stream(ItemType.values())
                .filter(value -> value != toExclude)
                .collect(Collectors.toList());
    }

    public int getClusterId() {
        return clusterId;
    }

    public int getArrayPosition() {
        return arrayPosition;
    }

    public String getClusterName() {
        return clusterName;
    }
}
