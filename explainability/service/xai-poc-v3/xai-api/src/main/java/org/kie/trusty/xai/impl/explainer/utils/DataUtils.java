package org.kie.trusty.xai.impl.explainer.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.kie.trusty.m2x.model.DataDistribution;
import org.kie.trusty.m2x.model.Feature;
import org.kie.trusty.m2x.model.FeatureDistribution;
import org.kie.trusty.m2x.model.Output;
import org.kie.trusty.m2x.model.PredictionInput;
import org.kie.trusty.m2x.model.PredictionOutput;
import org.kie.trusty.m2x.model.Type;
import org.kie.trusty.m2x.model.Value;

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
            double g = random.nextDouble();
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

    public static DataDistribution generateDistribution(int noOfFeatures) {
        List<FeatureDistribution> featureDistributions = new ArrayList<>(noOfFeatures);
        for (int i = 0; i < noOfFeatures; i++) {
            double min = random.nextInt(10);
            double max = 10 + random.nextInt(1000);
            double mean = min + +random.nextInt((int) (max * 0.75));
            double stdDev = random.nextInt((int) mean);
            FeatureDistribution featureDistribution = new FeatureDistribution(min, max, mean, stdDev);
            featureDistributions.add(featureDistribution);
        }
        return new DataDistribution(featureDistributions);
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

    public static PredictionOutput outputFrom(double[] doubles) {
        List<Output> outputs = new ArrayList<>(doubles.length);
        for (double d : doubles) {
            outputs.add(new Output(new Value<>(d), Type.NUMBER, d));
        }
        return new PredictionOutput(outputs);
    }

    public static double[] toNumbers(PredictionInput input) {
        double[] doubles = new double[input.getFeatures().size()];
        int i = 0;
        // TODO : strings 
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
        PredictionInput perturbedInput = new PredictionInput(input.getFeatures());
        int droppedFeatures = 1 + Math.min(random.nextInt(3), perturbedInput.getFeatures().size() / 2);
        boolean dropped = false;
        for (int i = 0; i < droppedFeatures; i++) {
            if (!dropped && random.nextBoolean()) {
                perturbedInput.getFeatures().set(i, featureDrop(perturbedInput.getFeatures().get(i)));
                dropped = true;
            }
        }
        if (!dropped) {
            perturbedInput.getFeatures().set(droppedFeatures, featureDrop(perturbedInput.getFeatures().get(droppedFeatures)));
        }
        return perturbedInput;
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
                // set the number to 0
                if (0 != feature.getValue().asNumber()) {
                    value = new Value<>(0);
                } else { // or subtract one to the current value
                    value = new Value<>(feature.getValue().asNumber() - 1);
                }
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
}
