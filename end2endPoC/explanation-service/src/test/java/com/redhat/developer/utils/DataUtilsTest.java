package com.redhat.developer.utils;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataUtilsTest {

    private final static SecureRandom random = new SecureRandom();

    @Test
    public void testDataGeneration() {
        double mean = random.nextDouble();
        double stdDeviation = random.nextDouble();
        int size = random.nextInt(100);
        double[] data = DataUtils.generateData(mean, stdDeviation, size);
        assertEquals(DoubleStream.of(data).average().getAsDouble(), mean, 1e-4);
        double stdDev = Math.sqrt(DoubleStream.of(data).map(i -> i - mean).map(i -> i * i).average().getAsDouble());
        assertEquals(stdDev, stdDeviation, 1e-4);
    }

    @Test
    public void testGaussianKernelWithRandomPositive() {
        double x = Math.abs(random.nextDouble());
        double k = DataUtils.gaussianKernel(x);
        assertTrue(x >= 0.5 ? k < x : x < k);
    }

    @Test
    public void testEuclideanDistance() {
        double[] x = new double[]{1, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.euclideanDistance(x, y);
        assertEquals(2.236, distance, 1e-3);
    }

    @Test
    public void testGowerDistance() {
        double[] x = new double[]{2, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.gowerDistance(x, y, 0.5);
        assertEquals(2.5, distance, 1e-2);
    }

    @Test
    public void testHammingDistance() {
        double[] x = new double[]{2, 1};
        double[] y = new double[]{2, 3};
        double distance = DataUtils.hammingDistance(x, y);
        assertEquals(1, distance, 1e-1);
    }

    @Test
    public void testExponentialSmoothingKernelWithRandomPositive() {
        double x = Math.abs(random.nextDouble());
        double k = DataUtils.exponentialSmoothingKernel(x, 2);
        assertTrue(x >= 1 ? k < x : x < k);
    }

    @Test
    public void testEmptyDatasetEncoding() {
        List<PredictionInput> inputs = new LinkedList<>();
        List<Output> outputs = new LinkedList<>();
        List<Feature> features = new LinkedList<>();
        PredictionInput originalInput = new PredictionInput(features);
        Output originalOutput = new Output("foo", Type.NUMBER, new Value<>(1), 1d);
        Collection<Pair<double[], Double>> trainingSet = DataUtils.encodeTrainingSet(inputs, outputs, originalInput, originalOutput);
        assertNotNull(trainingSet);
        assertTrue(trainingSet.isEmpty());
    }

    @Test
    public void testDatasetEncodingWithNumericData() {
        List<PredictionInput> perturbedInputs = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            List<Feature> inputFeatures = new LinkedList<>();
            for (int j = 0; j < 3; j++) {
                inputFeatures.add(new Feature("f" + random.nextInt(), Type.NUMBER, new Value<>(random.nextInt())));
            }
            perturbedInputs.add(new PredictionInput(inputFeatures));
        }
        List<Output> outputs = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            outputs.add(new Output("o", Type.NUMBER, new Value<>(random.nextBoolean()), 1d));
        }
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            features.add(new Feature("f" + random.nextInt(), Type.NUMBER, new Value<>(random.nextInt())));
        }
        PredictionInput originalInput = new PredictionInput(features);
        Output originalOutput = new Output("o", Type.BOOLEAN, new Value<>(random.nextBoolean()), 1d);
        Collection<Pair<double[], Double>> trainingSet = DataUtils.encodeTrainingSet(perturbedInputs, outputs, originalInput, originalOutput);
        assertNotNull(trainingSet);
        assertEquals(10, trainingSet.size());
    }

    @Test
    public void testPerturbDrop() {
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            features.add(new Feature("f" + random.nextInt(), Type.NUMBER, new Value<>(random.nextInt())));
        }
        PredictionInput input = new PredictionInput(features);
        int noOfPerturbations = random.nextInt(3);
        PredictionInput perturbedInput = DataUtils.perturbDrop(input, 10, noOfPerturbations);
        int changedFeatures = 0;
        for (int i = 0; i < 3; i++) {
            double v = input.getFeatures().get(i).getValue().asNumber();
            double pv = perturbedInput.getFeatures().get(i).getValue().asNumber();
            if (v != pv) {
                changedFeatures++;
            }
        }
        assertEquals(changedFeatures, noOfPerturbations);
    }
}