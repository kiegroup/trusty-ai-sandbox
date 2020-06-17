package com.redhat.developer.utils;

import java.math.BigDecimal;
import java.net.URI;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.redhat.developer.model.DataDistribution;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureDistribution;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;

public class DataUtils {

    private final static SecureRandom random = new SecureRandom();

    public static void seed(long seed) {
        random.setSeed(seed);
    }

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

    public static PredictionInput perturbDrop(PredictionInput input, int noOfSamples, int noOfPerturbations) {
        List<Feature> originalFeatures = input.getFeatures();
        List<Feature> newFeatures = new ArrayList<>(originalFeatures);
        PredictionInput perturbedInput = new PredictionInput(newFeatures);
        int perturbationSize = Math.min(noOfPerturbations, originalFeatures.size());
        int[] indexesToBePerturbed = random.ints(0, perturbedInput.getFeatures().size()).distinct().limit(perturbationSize).toArray();
        for (int value : indexesToBePerturbed) {
            perturbedInput.getFeatures().set(value, featureDrop(
                    perturbedInput.getFeatures().get(value), noOfSamples));
        }
        return perturbedInput;
    }

    private static Feature featureDrop(Feature feature, int noOfSamples) {
        Value<?> value = null;
        Type type = feature.getType();
        switch (type) {
            case TEXT:
                // randomly drop entire string or parts of it
                if (random.nextBoolean()) {
                    String stringValue = feature.getValue().asString();
                    if (stringValue.indexOf(' ') != -1) {
                        List<String> words = new ArrayList<>(Arrays.asList(stringValue.split(" ")));
                        int featuresToDrop = random.nextInt(Math.min(2, words.size() / 2));
                        for (int i = 0; i < 1 + featuresToDrop; i++) {
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
                double ov = feature.getValue().asNumber();
                // sample from normal distribution and center around feature value
                int pickIdx = random.nextInt(noOfSamples - 1);
                double v = DataUtils.generateData(0, 1, noOfSamples)[pickIdx] * ov + ov;
                value = new Value<>(v);
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
            case BINARY:
                value = new Value<>(new byte[0]);
                break;
            case URI:
                value = new Value<>(URI.create(""));
                break;
            case VECTOR:
                value = new Value<>(new double[0]);
                break;
            case UNDEFINED:
                // do nothing
                break;
        }
        return new Feature(feature.getName(), feature.getType(), value);
    }

    public static double hammingDistance(double[] x, double[] y) {
        int h = 0;
        for (int i = 0; i < Math.min(x.length, y.length); i++) {
            if (x[i] != y[i]) {
                h++;
            }
        }
        return h + (x.length - y.length);
    }

    public static double euclideanDistance(double[] x, double[] y) {
        double e = 0;
        for (int i = 0; i < Math.min(x.length, y.length); i++) {
            e += Math.pow(x[i] - y[i], 2);
        }
        return Math.sqrt(e);
    }

    public static double gowerDistance(double[] x, double[] y, double lambda) {
        return euclideanDistance(x, y) + lambda * hammingDistance(x, y);
    }

    public static double gaussianKernel(double x) {
        return Math.exp(-Math.pow(x, 2) / 2) / Math.sqrt(3.14);
    }

    public static double exponentialSmoothingKernel(double x, double sigma) {
        return Math.sqrt(Math.exp(-(Math.pow(x, 2)) / Math.pow(sigma, 2)));
    }

    public static FeatureDistribution getFeatureDistribution(double[] doubles) {
        double min = DoubleStream.of(doubles).min().getAsDouble();
        double max = DoubleStream.of(doubles).max().getAsDouble();
        double mean = getMean(doubles);
        double stdDev = getStdDev(doubles, mean);
        return new FeatureDistribution(min, max, mean, stdDev);
    }

    public static DataDistribution generateRandomDataDistribution(int size) {
        List<FeatureDistribution> featureDistributions = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            double[] doubles = generateData(random.nextDouble(), random.nextDouble(), 1000);
            FeatureDistribution featureDistribution = DataUtils.getFeatureDistribution(doubles);
            featureDistributions.add(featureDistribution);
        }
        return new DataDistribution(featureDistributions);
    }
}
