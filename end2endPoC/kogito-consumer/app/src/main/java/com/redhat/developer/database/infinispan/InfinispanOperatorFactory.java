package com.redhat.developer.database.infinispan;

import java.util.HashMap;
import java.util.Map;

import com.redhat.developer.database.operators.DateOperator;

public class InfinispanOperatorFactory {

    private static Map<DateOperator, String> dateOperators = createDateOperators();

    static Map<DateOperator, String> createDateOperators() {
        HashMap<DateOperator, String> map = new HashMap<>();
        map.put(DateOperator.LTE, "<=");
        map.put(DateOperator.GTE, ">=");
        return map;
    }

    public static String convert(DateOperator operator) {
        return dateOperators.get(operator);
    }
}
