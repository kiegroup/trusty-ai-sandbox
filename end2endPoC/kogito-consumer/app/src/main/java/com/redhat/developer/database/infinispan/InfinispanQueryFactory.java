package com.redhat.developer.database.infinispan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.redhat.developer.database.InfinispanStorageManager;
import com.redhat.developer.database.TrustyStorageQuery;
import com.redhat.developer.database.operators.DateOperator;
import com.redhat.developer.database.operators.IntegerOperator;
import com.redhat.developer.database.operators.StringOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanQueryFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanQueryFactory.class);

    public static String build(TrustyStorageQuery query, String entity) {

        String qq = "from " + entity + " b";

        if (query.dateOperations.size() == 0 && query.integerOperations.size() == 0 && query.stringOperations.size() == 0) {
            return qq;
        }

        qq = qq + " where ";

        List<String> conditions = new ArrayList<>();
        for (TrustyStorageQuery.InternalWhereDecision<IntegerOperator, Integer> condition : query.integerOperations) {
            conditions.add("b." + condition.property + "=" + condition.value);
        }
        qq += String.join(" and ", conditions);

        conditions = new ArrayList<>();
        for (TrustyStorageQuery.InternalWhereDecision<StringOperator, String> condition : query.stringOperations) {
            conditions.add("b." + condition.property + "=" + "\"" + condition.value + "\"");
        }
        LOGGER.info(qq);
        if (conditions.size() > 0){
            qq = qq + (conditions.size() > 1 ?  String.join(" and ", conditions) : conditions.get(0));
        }
        LOGGER.info(qq);

        conditions = new ArrayList<>();
        SimpleDateFormat formatterIn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat formatterOut = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // infinispan specific, look here https://github.com/infinispan/infinispan/blob/0cbb18f426c803a3b769047da10d520b8e8b5fea/object-filter/src/main/java/org/infinispan/objectfilter/impl/util/DateHelper.java#L13

        for (TrustyStorageQuery.InternalWhereDecision<DateOperator, String> condition : query.dateOperations) {
            try {
                condition.value = formatterOut.format(formatterIn.parse(condition.value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            conditions.add("b." + condition.property + InfinispanOperatorFactory.convert(condition.operator) +
                                   "\"" + condition.value + "\"");
        }
        qq += String.join(" and ", conditions);

        return qq;
    }
}
