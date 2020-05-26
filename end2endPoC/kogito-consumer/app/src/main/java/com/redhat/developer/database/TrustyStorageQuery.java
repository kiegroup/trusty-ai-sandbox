package com.redhat.developer.database;

import java.util.ArrayList;
import java.util.List;

import com.redhat.developer.database.operators.DateOperator;
import com.redhat.developer.database.operators.IntegerOperator;
import com.redhat.developer.database.operators.StringOperator;

public class TrustyStorageQuery {

    public List<InternalWhereDecision<StringOperator, String>> stringOperations = new ArrayList<>();

    public List<InternalWhereDecision<IntegerOperator, Integer>> integerOperations = new ArrayList<>();

    public List<InternalWhereDecision<DateOperator, Long>> dateOperations = new ArrayList<>();

    public TrustyStorageQuery() {
    }

    public TrustyStorageQuery where(String property, StringOperator operator, String value) {
        stringOperations.add(new InternalWhereDecision(property, operator, value));
        return this;
    }

    public TrustyStorageQuery where(String property, IntegerOperator operator, Integer value) {
        integerOperations.add(new InternalWhereDecision(property, operator, value));
        return this;
    }

    public TrustyStorageQuery where(String property, DateOperator operator, Long value) {
        System.out.println(value);
        dateOperations.add(new InternalWhereDecision(property, operator, value));
        return this;
    }

    public class InternalWhereDecision<T, K> {

        public String property;
        public T operator;
        public K value;

        public InternalWhereDecision(String property, T operator, K value) {
            this.property = property;
            this.operator = operator;
            this.value = value;
        }
    }
}
