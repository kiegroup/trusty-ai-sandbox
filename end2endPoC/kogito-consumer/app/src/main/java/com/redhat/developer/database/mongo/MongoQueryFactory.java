package com.redhat.developer.database.mongo;

import java.util.ArrayList;
import java.util.List;

import com.redhat.developer.database.TrustyStorageQuery;
import com.redhat.developer.database.operators.DateOperator;
import com.redhat.developer.database.operators.IntegerOperator;
import com.redhat.developer.database.operators.StringOperator;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.regex;

public class MongoQueryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoQueryFactory.class);

    public static Bson build(TrustyStorageQuery query, String entity) {
        List<Bson> conditions = new ArrayList<>();

        for (TrustyStorageQuery.InternalWhereDecision<IntegerOperator, Integer> condition : query.integerOperations) {
            conditions.add(eq(condition.property, condition.value));
        }

        for (TrustyStorageQuery.InternalWhereDecision<StringOperator, String> condition : query.stringOperations) {
            switch (condition.operator){
                case EQUALS:
                    conditions.add(eq(condition.property, condition.value));
                    break;
                case PREFIX:
                    conditions.add(regex(condition.property, "^" + condition.value + "*"));
                    break;
            }
        }

//        conditions = new ArrayList<>();
//        SimpleDateFormat formatterIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        SimpleDateFormat formatterOut = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // infinispan specific, look here https://github.com/infinispan/infinispan/blob/0cbb18f426c803a3b769047da10d520b8e8b5fea/object-filter/src/main/java/org/infinispan/objectfilter/impl/util/DateHelper.java#L13

        for (TrustyStorageQuery.InternalWhereDecision<DateOperator, String> condition : query.dateOperations) {
            switch (condition.operator) {
                case GTE:
                    conditions.add(gte(condition.property, condition.value));
                    break;
                case LTE:
                    conditions.add(lte(condition.property, condition.value));
                    break;
            }

        }

        if (conditions.size() > 1){
            return and(conditions);
        }
        return conditions.get(0);
    }
}
