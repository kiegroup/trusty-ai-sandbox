package com.redhat.developer.utils;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;

public class LinearClassifier {

    private double[] weights;

    public LinearClassifier(int noOfWeights) {
        weights = new double[noOfWeights];
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1f / (double) secureRandom.nextInt(100);
        }
    }

    public void fit(Collection<Prediction> trainingData) {
        assert !trainingData.isEmpty() : "cannot fit on an empty dataset";

        encodeFeatures(trainingData);

        for (Prediction prediction : trainingData) {
            PredictionInput input = prediction.getInput();
            PredictionOutput output = prediction.getOutput();
            double predictedOutput = predict(input);
            double targetOutput = DataUtils.toNumbers(output)[0]; // assume the output has always one element (by previous label encoding construction)
            double diff = targetOutput - predictedOutput;
            if (diff != 0) { // avoid null update to save computation
                weights = DoubleStream.of(weights).map(d -> d + 1e-2 * diff).map(d -> Double.isNaN(d) ? 0d : d).toArray();
            }
        }
    }

    private void encodeFeatures(Collection<Prediction> trainingData) {
        Prediction firstItem = trainingData.stream().findFirst().get();
        List<Type> featureTypes = firstItem.getInput().getFeatures().stream().map(Feature::getType).collect(Collectors.toList());

        for (int t = 0; t < featureTypes.size(); t++) {
            if (!Type.NUMBER.equals(featureTypes.get(t))) {
                // convert values for this feature into a number
                switch (featureTypes.get(t)) {
                    case STRING:
                        // encode categorical data
                        List<String> strings = new LinkedList<>();
                        strings.add("");
                        for (Prediction p : trainingData) {
                            Feature feature = p.getInput().getFeatures().get(t);
                            String s = feature.getValue().asString();
                            strings.add(s);
                        }
                        for (Prediction p : trainingData) {
                            Feature originalFeature = p.getInput().getFeatures().get(t);
                            Feature newFeature = new Feature(originalFeature.getName(), Type.NUMBER,
                                                             new Value<>(strings.indexOf(originalFeature.getValue().asString())));
                            p.getInput().getFeatures().set(t, newFeature);
                        }
                        break;
                    case BINARY:
                        break;
                    case BOOLEAN:
                        break;
                    case DATE:
                        break;
                    case URI:
                        break;
                    case TIME:
                        break;
                    case DURATION:
                        break;
                    case VECTOR:
                        break;
                    case UNDEFINED:
                        break;
                    case CURRENCY:
                        break;
                }
            } else {
                // max - min scaling
                double[] doubles = new double[trainingData.size()];
                int i = 0;
                for (Prediction p : trainingData) {
                    Feature feature = p.getInput().getFeatures().get(t);
                    doubles[i] = feature.getValue().asNumber();
                    i++;
                }
                double min = DoubleStream.of(doubles).min().getAsDouble();
                double max = DoubleStream.of(doubles).max().getAsDouble();
                doubles = DoubleStream.of(doubles).map(d -> (d - min) / (max - min)).toArray();

                int j = 0;
                for (Prediction p : trainingData) {
                    Feature originalFeature = p.getInput().getFeatures().get(t);
                    Feature newFeature = new Feature(originalFeature.getName(), Type.NUMBER,
                                                     new Value<>(doubles[j]));
                    p.getInput().getFeatures().set(t, newFeature);
                    j++;
                }
            }
        }
    }

    private double predict(PredictionInput input) {
        return IntStream.range(0, DataUtils.toNumbers(input).length).mapToDouble(i -> DataUtils.toNumbers(input)[i] * weights[i]).sum();
    }

    public double[] getWeights() {
        return weights;
    }
}
