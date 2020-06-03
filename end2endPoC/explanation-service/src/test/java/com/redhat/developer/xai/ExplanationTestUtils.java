package com.redhat.developer.xai;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;

public class ExplanationTestUtils {

    public static Model getFeaturePassModel(int featureIndex) {
        return inputs -> {
            List<PredictionOutput> predictionOutputs = new LinkedList<>();
            for (PredictionInput predictionInput : inputs) {
                List<Feature> features = predictionInput.getFeatures();
                Feature feature = features.get(featureIndex);
                PredictionOutput predictionOutput = new PredictionOutput(
                        List.of(new Output("feature-" + featureIndex, feature.getType(), feature.getValue(),
                                           1d)));
                predictionOutputs.add(predictionOutput);
            }
            return predictionOutputs;
        };
    }

    public static Model getSumSkipModel(int skipFeatureIndex) {
        return inputs -> {
            List<PredictionOutput> predictionOutputs = new LinkedList<>();
            for (PredictionInput predictionInput : inputs) {
                List<Feature> features = predictionInput.getFeatures();
                double result = 0;
                for (int i = 0; i < features.size(); i++) {
                    if (skipFeatureIndex != i) {
                        result += features.get(i).getValue().asNumber();
                    }
                }
                PredictionOutput predictionOutput = new PredictionOutput(
                        List.of(new Output("sum-but" + skipFeatureIndex, Type.NUMBER, new Value<>(result), 1d)));
                predictionOutputs.add(predictionOutput);
            }
            return predictionOutputs;
        };
    }

    public static Model getEvenFeatureModel(int featureIndex) {
        return inputs -> {
            List<PredictionOutput> predictionOutputs = new LinkedList<>();
            for (PredictionInput predictionInput : inputs) {
                List<Feature> features = predictionInput.getFeatures();
                Feature feature = features.get(featureIndex);
                double v = feature.getValue().asNumber();
                PredictionOutput predictionOutput = new PredictionOutput(
                        List.of(new Output("feature-" + featureIndex, Type.BOOLEAN, new Value<>(v % 2 == 0), 1d)));
                predictionOutputs.add(predictionOutput);
            }
            return predictionOutputs;
        };
    }

    public static Model getEvenSumModel(int skipFeatureIndex) {
        return inputs -> {
            List<PredictionOutput> predictionOutputs = new LinkedList<>();
            for (PredictionInput predictionInput : inputs) {
                List<Feature> features = predictionInput.getFeatures();
                double result = 0;
                for (int i = 0; i < features.size(); i++) {
                    if (skipFeatureIndex != i) {
                        result += features.get(i).getValue().asNumber();
                    }
                }
                PredictionOutput predictionOutput = new PredictionOutput(
                        List.of(new Output("sum-even-but" + skipFeatureIndex, Type.BOOLEAN, new Value<>(((int) result) % 2 == 0), 1d)));
                predictionOutputs.add(predictionOutput);
            }
            return predictionOutputs;
        };
    }

    public static Model getTextClassifier() {
        return inputs -> {
            List<PredictionOutput> outputs = new LinkedList<>();
            for (PredictionInput input : inputs) {
                boolean spam = false;
                for (Feature f : input.getFeatures()) {
                    if (!spam && Type.STRING.equals(f.getType())) {
                        String s = f.getValue().asString();
                        String[] words = s.split(" ");
                        Arrays.sort(words);
                        if (Arrays.binarySearch(words, "money") >= 0 || Arrays.binarySearch(words, "$") >= 0 ||
                                Arrays.binarySearch(words, "loan") >= 0) {
                            spam = true;
                        }
                    }
                }
                Output output = new Output("spam-classification", Type.BOOLEAN, new Value<>(spam), 1d);
                outputs.add(new PredictionOutput(List.of(output)));
            }
            return outputs;
        };
    }
}
