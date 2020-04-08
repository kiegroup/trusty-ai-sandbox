package org.kie.trusty.v1.xai.explainer.global.viz;

public class TabularData {

    private final double[] x;
    private final double[] y;

    public TabularData(double[] x, double[] y) {
        this.x = x;
        this.y = y;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }
}
