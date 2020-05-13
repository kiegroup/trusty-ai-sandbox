package com.redhat.developer.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;

public class DataUtils {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generate a dataset of a certain size, given mean and standard deviation.
     * Samples are generated randomly, actual mean {@code m} and standard deviation {@code d} are calculated.
     * Then all numbers are multiplied by the same number so that the standard deviation also gets
     * multiplied by the same number, hence we multiply each random number by {@code stdDeviation / d}.
     * The resultant set has standard deviation {@code stdDeviation} and mean {@code m1=m*stdDeviation/d}.
     * If a same number is added to all values the mean also changes by the same number so we add {@code mean - m1} to
     * all numbers.
     *
     * @param mean         desired mean
     * @param stdDeviation desired standard deviation
     * @param size         size of the array
     * @return the generated data
     */
    public static double[] generateData(double mean, double stdDeviation, int size) {
        double[] data = new double[size];
        // generate random data
        for (int i = 0; i < size; i++) {
            double g = 1d / (1d + random.nextInt(10));
            data[i] = g;
        }

        // get the mean
        double m = getMean(data);

        // get the standard deviation
        double d = getStdDev(data, m);

        // force desired standard deviation
        double d1 = stdDeviation / d;
        for (int i = 0; i < size; i++) {
            data[i] *= d1;
        }
        // get the new mean
        double m1 = m * stdDeviation / d;

        // force desired mean
        for (int i = 0; i < size; i++) {
            data[i] += mean - m1;
        }
        return data;
    }

    private static double getMean(double[] data) {
        double m = 0;
        for (double datum : data) {
            m += datum;
        }
        m = m / (double) data.length;
        return m;
    }

    private static double getStdDev(double[] data, double mean) {
        double d = 0;
        for (double datum : data) {
            d += Math.pow(datum - mean, 2);
        }
        d /= data.length;
        d = Math.sqrt(d);
        return d;
    }

    /**
     * Generate equally {@code size} sampled values between {@code min} and {@code max}.
     *
     * @param min  minimum value
     * @param max  maximum value
     * @param size dataset size
     * @return the generated data
     */
    public static double[] generateSamples(double min, double max, int size) {
        double[] data = new double[size];
        double val = min;
        double sum = max / size;
        for (int i = 0; i < size; i++) {
            data[i] = val;
            val += sum;
        }
        return data;
    }

    public static double[] perturbDrop(double[] data) {
        double[] perturbed = new double[data.length];
        System.arraycopy(data, 0, perturbed, 0, data.length);
        for (int j = 0; j < random.nextInt(data.length / 2); j++) {
            perturbed[random.nextInt(data.length)] = 0;
        }
        return perturbed;
    }

    public static PredictionInput inputFrom(double[] doubles) {
        List<Feature> features = new ArrayList<>(doubles.length);
        for (double d : doubles) {
            Feature f = new Feature("", Type.NUMBER, new Value<>(d));
            features.add(f);
        }
        return new PredictionInput(features);
    }

    public static double[] toNumbers(PredictionInput input) {
        double[] doubles = new double[input.getFeatures().size()];
        int i = 0;
        for (Feature f : input.getFeatures()) {
            doubles[i] = f.getValue().asNumber();
            i++;
        }
        return doubles;
    }

    public static double[] toNumbers(PredictionOutput output) {
        double[] doubles = new double[output.getOutputs().size()];
        int i = 0;
        for (Output o : output.getOutputs()) {
            doubles[i] = o.getValue().asNumber();
            i++;
        }
        return doubles;
    }

    public static List<Feature> doublesToFeatures(double[] inputs) {
        return DoubleStream.of(inputs).mapToObj(DataUtils::doubleToFeature).collect(Collectors.toList());
    }

    public static List<BigDecimal> doublesToBigDecimals(double... doubles) {
        return DoubleStream.of(doubles).mapToObj(BigDecimal::new).collect(Collectors.toList());
    }

    public static Feature doubleToFeature(double d) {
        return new Feature(String.valueOf(d), Type.NUMBER, new Value<>(d));
    }

    public static PredictionInput perturbDrop(PredictionInput input) {
        List<Feature> originalFeatures = input.getFeatures();
        List<Feature> newFeatures = new ArrayList<>(originalFeatures);
        PredictionInput perturbedInput = new PredictionInput(newFeatures);
        // Extract 1 + random indexes to be perturbed
        int perturbationSize = Math.min(2, originalFeatures.size());
        int[] indexesToBePerturbed = new Random().ints(0, perturbedInput.getFeatures().size()).distinct().limit(perturbationSize).toArray();
        for (int i = 0; i < indexesToBePerturbed.length; i++) {
            perturbedInput.getFeatures().set(indexesToBePerturbed[i], featureDrop(perturbedInput.getFeatures().get(indexesToBePerturbed[i])));
        }
        return perturbedInput;
    }

    public static void encodeFeatures(Collection<Prediction> trainingData, Prediction original) {
        Prediction firstItem = trainingData.stream().findFirst().get();
        List<Type> featureTypes = firstItem.getInput().getFeatures().stream().map(Feature::getType).collect(Collectors.toList());

        for (int t = 0; t < featureTypes.size(); t++) {
            if (!Type.NUMBER.equals(featureTypes.get(t))) {
                // convert values for this feature into a number
                switch (featureTypes.get(t)) {
                    case STRING:
                        for (Prediction p : trainingData) {
                            Feature originalFeature = original.getInput().getFeatures().get(t);
                            String originalString = originalFeature.getValue().asString();
                            String perturbedString = p.getInput().getFeatures().get(t).getValue().asString();
                            Feature newFeature = new Feature(originalFeature.getName(), Type.NUMBER,
                                                             new Value<>(originalString.equals(perturbedString) ? 1 : 0));
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
                doubles = DoubleStream.of(doubles).map(d -> (d - min) / (max - min)).map(d -> Double.isNaN(d) ? 1 : d).toArray();

                int j = 0;
                for (Prediction p : trainingData) {
                    Feature originalFeature = p.getInput().getFeatures().get(t);
                    double perturbedNumber = doubles[j];
                    double originalNumber = 1d;
                    double newValue = Math.abs(perturbedNumber - originalNumber) < 0.1 ? 1 : 0;
                    Feature newFeature = new Feature(originalFeature.getName(), Type.NUMBER,
                                                     new Value<>(newValue));
                    p.getInput().getFeatures().set(t, newFeature);
                    j++;
                }
            }
        }
    }

    private static Feature featureDrop(Feature feature) {
        Value<?> value = null;
        Type type = feature.getType();
        switch (type) {
            case STRING:
                // randomly drop entire string or parts of it
                if (random.nextBoolean()) {
                    String stringValue = feature.getValue().asString();
                    if (stringValue.indexOf(' ') != -1) {
                        List<String> words = Arrays.asList(stringValue.split(" "));
                        for (int i = 0; i < 1 + random.nextInt(Math.min(2, words.size() / 2)); i++) {
                            int dropIdx = random.nextInt(words.size());
                            words.remove(dropIdx);
                        }
                        String newStringValue = String.join(" ", words);
                        value = new Value<>(newStringValue);
                    } else {
                        value = new Value<>("");
                    }
                } else {
                    value = new Value<>("");
                }
                break;
            case NUMBER:
                double mean = feature.getValue().asNumber();
                double stdDev = feature.getValue().asNumber() * 0.33;
                int size = 10;
                value = new Value<>(DataUtils.generateData(mean, stdDev, size)[random.nextInt(size - 1)]);
                break;
            case BOOLEAN:
                // flip the boolean value
                value = new Value<>(!Boolean.getBoolean(feature.getValue().asString()));
                break;
            case DATE:
                // set to initial value of Java date in the current TZ
                value = new Value<>(new Date(0).toLocalDate().toString());
                break;
            case TIME:
                // set to midnight
                value = new Value<>(LocalTime.MIDNIGHT.toString());
                break;
            case DURATION:
                // set the duration to 0
                value = new Value<>(Duration.of(0, ChronoUnit.SECONDS).toString());
                break;
            case CURRENCY:
                // set the currency to 0
                value = new Value<>("0.0");
                break;
        }
        return new Feature(feature.getName(), feature.getType(), value);
    }

    public static Output labelEncodeOutputValue(List<Output> actualOutputs, int o, List<PredictionOutput> predictionOutputs, int i) {
        PredictionOutput generatedOutput = predictionOutputs.get(i);
        Output predictedOutput = generatedOutput.getOutputs().get(o);
        Object target = actualOutputs.get(o).getValue().getUnderlyingObject();
        Object observed = predictedOutput.getValue().getUnderlyingObject();
        Value<Integer> predictedValue = new Value<>(target.equals(observed) ? 1 : 0);
        return new Output("target", Type.NUMBER, predictedValue, predictedOutput.getScore());
    }

    public static double hamming(double[] x, double[] y) {
        int h = 0;
        for (int i = 0; i < Math.min(x.length, y.length); i++) {
            if (x[i] != y[i]) {
                h++;
            }
        }
        return h + (x.length - y.length);
    }

    public static double euclidean(double[] x, double[] y) {
        double e = 0;
        for (int i = 0; i < Math.min(x.length, y.length); i++) {
            e += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(e);
    }

    public static double gower(double[] x, double[] y, double lambda) {
        return euclidean(x, y) + lambda * hamming(x, y);
    }

    public static double gaussianKernel(double x) {
        return Math.exp(-Math.pow(x, 2) / 2) / Math.sqrt(3.14);
    }

    public static double exponentialSmoothingKernel(double x, double sigma) {
        return Math.sqrt(Math.exp(-(Math.pow(x, 2)) / Math.pow(sigma, 2)));
    }
}
