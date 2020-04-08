package org.kie.trusty.v1.xai.explainer.global.viz.utils;

import java.security.SecureRandom;

public class DataGenerationUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * We also know that if all numbers are multiplied by the same number,
     * then the standard deviation also gets multiplied by the same number.
     * So if D is the desired standard deviation multiply all numbers by D/d.
     * The resultant set would have standard deviation D and mean m1=m*D/d.
     * <p>
     * We know that that if same number is added to all values the mean also changes by the same number.
     * So if the desired mean is M, then add the difference M-m1 to all the numbers of the new set.
     * We now have a set of numbers with mean = M. As variance or standard deviation is unaffected by such transformation,
     * the new series has the same variance/standard deviation as the one obtained in the previous step.
     *
     * @param mean
     * @param stdDeviation
     * @param size
     * @return
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
