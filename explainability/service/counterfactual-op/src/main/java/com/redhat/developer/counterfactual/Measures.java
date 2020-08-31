package com.redhat.developer.counterfactual;

public class Measures {

    public static double manhattan(double[] valuesA, double[] valuesB) {
        double distance = 0.0;
        for (int i = 0 ; i < valuesA.length ; i++) {
            distance += Math.abs(valuesA[i] - valuesB[i]);
        }
        return distance;
    }

}
