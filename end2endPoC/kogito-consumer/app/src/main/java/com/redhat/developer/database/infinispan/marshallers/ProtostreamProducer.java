package com.redhat.developer.database.infinispan.marshallers;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.MessageMarshaller;

@ApplicationScoped
public class ProtostreamProducer {

    @Inject
    ObjectMapper mapper;

    @Produces
    FileDescriptorSource kogitoTypesDescriptor() throws IOException {
        FileDescriptorSource source = new FileDescriptorSource();
        source.addProtoFile("trusty.proto", Thread.currentThread().getContextClassLoader().getResourceAsStream("trusty.proto"));
        source.addProtoFile("dmnModel.proto", Thread.currentThread().getContextClassLoader().getResourceAsStream("dmnModel.proto"));
        return source;
    }

    @Produces
    MessageMarshaller executionMarshaller() {
        return new ExecutionMarshaller(mapper);
    }

    @Produces
    MessageMarshaller outcomeMarshaller() {
        return new OutcomeModelMarshaller(mapper);
    }

    @Produces
    MessageMarshaller dmnModelMarshaller() {
        return new DmnModelMarshaller(mapper);
    }
}
