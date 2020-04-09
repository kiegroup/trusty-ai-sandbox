package org.kie.trusty.v1.xai.explainer.global.viz.utils;

import java.security.SecureRandom;

public class DataGenerationUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

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
            double g = SECURE_RANDOM.nextDouble();
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
}
