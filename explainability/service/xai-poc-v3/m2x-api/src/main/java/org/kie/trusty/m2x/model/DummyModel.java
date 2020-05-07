package org.kie.trusty.m2x.model;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

class DummyModel implements Model {

    @Override
    public List<PredictionOutput> predict(PredictionInput... inputs) {
        List<PredictionOutput> predictionOutputs = new LinkedList<>();
        for (PredictionInput predictionInput : inputs) {
            double sum = predictionInput.getFeatures().stream().map(f -> f.getValue().asNumber())
                    .mapToDouble(d -> d.doubleValue()).sum();
            Output output = new Output(new Value<>(1 / (1 - Math.exp(sum))), Type.NUMBER, 1d);
            PredictionOutput predictionOutput = new PredictionOutput(List.of(output));
            predictionOutputs.add(predictionOutput);
        }
        return predictionOutputs;
    }

    @Override
    public ModelInfo getInfo() {
        return new ModelInfo() {
            @Override
            public UUID getId() {
                return UUID.randomUUID();
            }

            @Override
            public String getName() {
                return "dummy model";
            }

            @Override
            public String getVersion() {
                return "0.1";
            }

            @Override
            public URI getPredictionEndpoint() {
                return null;
            }

            @Override
            public DataDistribution getTrainingDataDistribution() {
                return new DataDistribution(List.of(new FeatureDistribution(0, 10, 5, 1)));
            }

            @Override
            public PredictionInput getInputShape() {
                return new PredictionInput(Collections.singletonList(new Feature("f1", Type.NUMBER, new StringValue(""))));
            }

            @Override
            public PredictionOutput getOutputShape() {
                return new PredictionOutput(List.of(new Output(new StringValue(""), Type.NUMBER, 1d)));
            }

            @Override
            public TaskType getTaskType() {
                return TaskType.REGRESSION;
            }
        };
    }
}