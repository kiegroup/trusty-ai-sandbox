package org.kie.trusty.xai.explainer.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.kie.trusty.xai.model.Feature;
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
     * @param mean desired mean
     * @param stdDeviation desired standard deviation
     * @param size size of the array
     * @return the generated data
     */
    public static double[] generateData(double mean, double stdDeviation, int size) {
        double[] data = new double[size];
        // generate random data and get the mean
        double m = 0;
        for (int i = 0; i < size; i++) {
            double g = random.nextDouble();
            data[i] = g;
            m += g;
        }
        m /= size;

        // get the standard deviation
        double d = 0;
        for (int i = 0; i < size; i++) {
            d += Math.pow(data[i] - m, 2);
        }
        d /= size;
        d = Math.sqrt(d);

        // force desired standard deviation
        double d1 = stdDeviation / d;
        for (int i = 0; i < size; i++) {
            data[i] *= d1;
        }
        // get the new mean
        double m1 = m * d1 / d;

        // force desired mean
        for (int i = 0; i < size; i++) {
            data[i] += mean - m1;
        }
        return data;
    }

    /**
     * Generate equally {@code size} sampled values between {@code min} and {@code max}.
     * @param min minimum value
     * @param max maximum value
     * @param size dataset size
     * @return the generated data
     */
    public static double[] generatedSamples(double min, double max, int size) {
        double[] data = new double[size];
        double val = min;
        double sum = max / min;
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
        return new double[0];
    }

    public static double[] toNumbers(PredictionOutput output) {
        return new double[0];
    }

    public static List<BigDecimal> doublesToFeatures(double... doubles) {
        return DoubleStream.of(doubles).mapToObj(BigDecimal::new).collect(Collectors.toList());
    }

    public static Feature doubleToFeature(double d) {
        Feature f = new Feature();
        f.setValue(String.valueOf(d));
        f.setType(Feature.TypeEnum.NUMBER);
        return f;
    }
}
