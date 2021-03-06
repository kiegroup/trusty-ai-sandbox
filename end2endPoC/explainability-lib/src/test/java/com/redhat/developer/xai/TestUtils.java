package com.redhat.developer.xai;

import java.net.URI;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.redhat.developer.model.DataDistribution;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureFactory;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.utils.DataUtils;
import org.apache.commons.lang3.RandomStringUtils;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtils {

    private final static SecureRandom random = new SecureRandom();

    static {
        random.setSeed(4);
    }

    public static Model getFeaturePassModel(int featureIndex) {
        return new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
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
            }

            @Override
            public DataDistribution getDataDistribution() {
                return DataUtils.generateRandomDataDistribution(featureIndex + 1);
            }

            @Override
            public PredictionInput getInputShape() {
                List<Feature> features = new LinkedList<>();
                features.add(FeatureFactory.newNumericalFeature("f1", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f2", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f3", Double.NaN));
                return new PredictionInput(features);
            }

            @Override
            public PredictionOutput getOutputShape() {
                return new PredictionOutput(List.of(new Output("feature-" + featureIndex, Type.NUMBER, new Value<>(Double.NaN), 1d)));
            }
        };
    }

    public static Model getSumSkipModel(int skipFeatureIndex) {
        return new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
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
            }

            @Override
            public DataDistribution getDataDistribution() {
                return DataUtils.generateRandomDataDistribution(skipFeatureIndex + 1);
            }

            @Override
            public PredictionInput getInputShape() {
                List<Feature> features = new LinkedList<>();
                features.add(FeatureFactory.newNumericalFeature("f1", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f2", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f3", Double.NaN));
                return new PredictionInput(features);
            }

            @Override
            public PredictionOutput getOutputShape() {
                return new PredictionOutput(List.of(new Output("o", Type.NUMBER, new Value<>(Double.NaN), 1d)));
            }
        };
    }

    public static Model getEvenFeatureModel(int featureIndex) {
        return new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
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
            }

            @Override
            public DataDistribution getDataDistribution() {
                return DataUtils.generateRandomDataDistribution(featureIndex + 1);
            }

            @Override
            public PredictionInput getInputShape() {
                List<Feature> features = new LinkedList<>();
                features.add(FeatureFactory.newNumericalFeature("f1", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f2", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f3", Double.NaN));
                return new PredictionInput(features);
            }

            @Override
            public PredictionOutput getOutputShape() {
                return new PredictionOutput(List.of(new Output("o", Type.NUMBER, new Value<>(Double.NaN), 1d)));
            }
        };
    }

    public static Model getEvenSumModel(int skipFeatureIndex) {
        return new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
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
            }

            @Override
            public DataDistribution getDataDistribution() {
                return DataUtils.generateRandomDataDistribution(skipFeatureIndex + 1);
            }

            @Override
            public PredictionInput getInputShape() {
                List<Feature> features = new LinkedList<>();
                features.add(FeatureFactory.newNumericalFeature("f1", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f2", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f3", Double.NaN));
                return new PredictionInput(features);
            }

            @Override
            public PredictionOutput getOutputShape() {
                return new PredictionOutput(List.of(new Output("o", Type.NUMBER, new Value<>(Double.NaN), 1d)));
            }
        };
    }

    public static Model getTextClassifier() {
        return new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    boolean spam = false;
                    for (Feature f : input.getFeatures()) {
                        if (!spam && Type.TEXT.equals(f.getType())) {
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
            }

            @Override
            public DataDistribution getDataDistribution() {
                return DataUtils.generateRandomDataDistribution(3);
            }

            @Override
            public PredictionInput getInputShape() {
                List<Feature> features = new LinkedList<>();
                features.add(FeatureFactory.newNumericalFeature("f1", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f2", Double.NaN));
                features.add(FeatureFactory.newNumericalFeature("f3", Double.NaN));
                return new PredictionInput(features);
            }

            @Override
            public PredictionOutput getOutputShape() {
                return new PredictionOutput(List.of(new Output("o", Type.NUMBER, new Value<>(Double.NaN), 1d)));
            }
        };
    }

    public static Feature getRandomFeature() {
        Feature f;
        int r = random.nextInt(12);
        String name = "f-" + random.nextFloat();
        if (r == 0) {
            ByteBuffer buffer = ByteBuffer.allocate(random.nextInt(256));
            f = FeatureFactory.newBinaryFeature(name, buffer);
        } else if (r == 1) {
            f = FeatureFactory.newTextFeature(name, randomString());
        } else if (r == 2) {
            Map<String, Object> map = new HashMap<>();
            while (random.nextBoolean()) {
                map.put("s-" + random.nextInt(), randomString());
            }
            f = FeatureFactory.newCompositeFeature(name, map);
        } else if (r == 3) {
            f = FeatureFactory.newCategoricalFeature(name, randomString());
        } else if (r == 4) {
            f = FeatureFactory.newObjectFeature(name, getRandomFeature());
        } else if (r == 5) {
            f = FeatureFactory.newBooleanFeature(name, random.nextBoolean());
        } else if (r == 6) {
            f = FeatureFactory.newNumericalFeature(name, random.nextDouble());
        } else if (r == 7) {
            f = FeatureFactory.newDurationFeature(name, Duration.ofDays(random.nextInt(30)));
        } else if (r == 8) {
            f = FeatureFactory.newCurrencyFeature(name, Currency.getInstance(Locale.getDefault()));
        } else if (r == 9) {
            f = FeatureFactory.newTimeFeature(name, LocalTime.now());
        } else if (r == 10) {
            f = FeatureFactory.newURIFeature(name, URI.create(randomString().replaceAll(" ", "")));
        } else if (r == 11) {
            double[] doubles = new double[random.nextInt(10) + 1];
            for (int i = 0; i < doubles.length; i++) {
                doubles[i] = random.nextDouble();
            }
            f = FeatureFactory.newVectorFeature(name, doubles);
        } else {
            fail("unexpected feature type selector");
            f = null;
        }
        return f;
    }

    public static String randomString() {
        return RandomStringUtils.random(random.nextInt(5));
    }
}
