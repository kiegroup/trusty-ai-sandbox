package com.redhat.developer.database.elastic;

import java.util.ArrayList;
import java.util.List;

import com.redhat.developer.database.TrustyStorageQuery;
import com.redhat.developer.database.operators.DateOperator;
import com.redhat.developer.database.operators.IntegerOperator;
import com.redhat.developer.database.operators.StringOperator;

public class ElasticQueryFactory {

    public static String build(TrustyStorageQuery query) {
        String prefix = "{\"size\": 10000, \"query\" : ";
        String suffix = "}";

        if (query.dateOperations.size() == 0 && query.integerOperations.size() == 0 && query.dateOperations.size() == 0) {
            return prefix + "{\"match_all\" : {}}" + suffix;
        }

        List<String> conditions = new ArrayList<>();
        for (TrustyStorageQuery.InternalWhereDecision<IntegerOperator, Integer> condition : query.integerOperations) {
            switch (condition.operator) {
                case EQUALS:
                    conditions.add("{\"match\" : {\"" + condition.property + "\" : " + condition.value + "}}");
                    break;
                case GTE:
                    conditions.add("{\"range\" : {\"" + condition.property + "\" : { \"gte\" : " + condition.value + "}}}");
                    break;
                case LTE:
                    conditions.add("{\"range\" : {\"" + condition.property + "\" : { \"lte\" : " + condition.value + "}}}");
                    break;
            }
        }

        for (TrustyStorageQuery.InternalWhereDecision<StringOperator, String> condition : query.stringOperations) {
            switch (condition.operator) {
                case EQUALS:
                    conditions.add("{\"match\" : {\"" + condition.property + "\" : \"" + condition.value + "\"}}");
                    break;
                case PREFIX:
                    conditions.add("{\"match_phrase_prefix\" : {\"" + condition.property + "\" : \"" + condition.value + "\"}}");
                    break;
            }
        }

        for (TrustyStorageQuery.InternalWhereDecision<DateOperator, String> condition : query.dateOperations) {
            switch (condition.operator) {
                case GTE:
                    conditions.add("{\"range\" : {\"" + condition.property + "\" : { \"gte\" : \"" + condition.value + "\"}}}");
                    break;
                case LTE:
                    conditions.add("{\"range\" : {\"" + condition.property + "\" : { \"lte\" : \"" + condition.value + "\"}}}");
                    break;
            }
        }

        if (conditions.size() == 1) {
            return prefix + conditions.get(0) + suffix;
        }

        return prefix + "{\"bool\" : { \"must\" : [" + String.join(", ", conditions) + "]}}" + suffix;
    }
}
