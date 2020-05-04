package org.kie.trusty.xai.explainer.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.kie.trusty.xai.model.DataDistribution;
import org.kie.trusty.xai.model.Feature;
import org.kie.trusty.xai.model.FeatureDistribution;
import org.kie.trusty.xai.model.Output;
import org.kie.trusty.xai.model.PredictionInput;
import org.kie.trusty.xai.model.PredictionOutput;

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
        for (int i = 0; i < data.length; i++) {
            d += Math.pow(data[i] - mean, 2);
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
        DataDistribution dataDistribution = new DataDistribution();
        for (int i = 0; i < noOfFeatures; i++) {
            FeatureDistribution featureDistribution = new FeatureDistribution();
            featureDistribution.setMin(new BigDecimal(random.nextInt(10)));
            featureDistribution.setMax(new BigDecimal(10 + random.nextInt(1000)));
            featureDistribution.setMean(new BigDecimal(featureDistribution.getMin().intValue() + random.nextInt(
                    (int) (featureDistribution.getMax().doubleValue() * 0.75))));
            featureDistribution.setStdDev(new BigDecimal(random.nextInt(featureDistribution.getMean().intValue())));
            dataDistribution.addFeatureDistributionsItem(featureDistribution);
        }
        return dataDistribution;
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
        PredictionInput predictionInput = new PredictionInput();
        for (double d : doubles) {
            Feature featuresItem = new Feature();
            featuresItem.setType(Feature.TypeEnum.NUMBER);
            featuresItem.setValue(String.valueOf(d));
            predictionInput.addFeaturesItem(featuresItem);
        }
        return predictionInput;
    }

    public static PredictionOutput outputFrom(double[] doubles) {
        PredictionOutput predictionOutput = new PredictionOutput();
        for (double d : doubles) {
            Output outputsItem = new Output();
            outputsItem.setLabel(String.valueOf(d));
            outputsItem.setScore(new BigDecimal(d));
            predictionOutput.addOutputsItem(outputsItem);
        }
        return predictionOutput;
    }

    public static double[] toNumbers(PredictionInput input) {
        double[] doubles = new double[input.getFeatures().size()];
        int i = 0;
        for (Feature f : input.getFeatures()) {
            doubles[i] = Double.parseDouble(f.getValue());
            i++;
        }
        return doubles;
    }

    public static double[] toNumbers(PredictionOutput output) {
        double[] doubles = new double[output.getOutputs().size()];
        int i = 0;
        for (Output o : output.getOutputs()) {
            doubles[i] = o.getScore().doubleValue();
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
        Feature f = new Feature();
        f.setValue(String.valueOf(d));
        f.setType(Feature.TypeEnum.NUMBER);
        return f;
    }

    public static PredictionInput perturbDrop(PredictionInput input) {
        PredictionInput perturbedInput = new PredictionInput();
        perturbedInput.setFeatures(input.getFeatures());
        int droppedFeatures = 1 + Math.min(random.nextInt(3), perturbedInput.getFeatures().size() / 2);
        random.ints(0, droppedFeatures).mapToObj(i -> perturbedInput.getFeatures().get(i)).map(DataUtils::featureDrop).close();
        return perturbedInput;
    }

    private static Feature featureDrop(Feature feature) {
        Feature.TypeEnum type = feature.getType();
        switch (type) {
            case STRING:
                // randomly drop entire string or parts of it
                if (random.nextBoolean()) {
                    String stringValue = feature.getValue();
                    if (stringValue.indexOf(' ') != -1) {
                        List<String> words = Arrays.asList(stringValue.split(" "));
                        for (int i = 0; i < 1 + random.nextInt(Math.min(2, words.size() / 2)); i++) {
                            int dropIdx = random.nextInt(words.size());
                            words.remove(dropIdx);
                        }
                        String newStringValue = String.join(" ", words);
                        feature.setValue(newStringValue);
                    } else {
                        feature.setValue("");
                    }
                } else {
                    feature.setValue("");
                }
                break;
            case NUMBER:
                // set the number to 0
                if (!"0".equals(feature.getValue())) {
                    feature.setValue("0");
                } else { // or subtract one to the current value
                    feature.setValue(new BigDecimal(feature.getValue()).subtract(new BigDecimal(1)).toPlainString());
                }
                break;
            case BOOLEAN:
                // flip the boolean value
                feature.setValue(String.valueOf(!Boolean.getBoolean(feature.getValue())));
                break;
            case DATE:
                // set to initial value of Java date in the current TZ
                feature.setValue(new Date(0).toLocalDate().toString());
                break;
            case TIME:
                // set to midnight
                feature.setValue(LocalTime.MIDNIGHT.toString());
                break;
            case DURATION:
                // set the duration to 0
                feature.setValue(Duration.of(0, ChronoUnit.SECONDS).toString());
                break;
            case CURRENCY:
                // set the currency to 0
                feature.setValue("0.0");
                break;
        }
        return feature;
    }
}
