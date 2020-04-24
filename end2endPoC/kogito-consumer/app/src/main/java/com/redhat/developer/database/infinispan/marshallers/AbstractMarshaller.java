package com.redhat.developer.database.infinispan.marshallers;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractMarshaller {

    private ObjectMapper mapper;

    public AbstractMarshaller(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ZonedDateTime dateToZonedDateTime(Date date) {
        return date == null ? null : ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }

    public Date zonedDateTimeToDate(ZonedDateTime date) {
        return date == null ? null : Date.from(date.toInstant());
    }

    public JsonNode jsonFromString(String value) throws IOException {
        return value == null ? null : mapper.readTree(value);
    }
}
