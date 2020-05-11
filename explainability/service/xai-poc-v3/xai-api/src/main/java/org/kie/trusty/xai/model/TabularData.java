package org.kie.trusty.xai.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kie.trusty.m2x.model.Feature;

@Schema
public class TabularData {

    private double[] x;
    private double[] y;
    private Feature feature;

    public TabularData() {
        this.x = new double[0];
        this.y = new double[0];
        this.feature = new Feature();
    }

    public TabularData(Feature feature, double[] x, double[] y) {
        assert x.length == y.length : "x and y lenghts do not match";
        this.feature = feature;
        this.x = x;
        this.y = y;
    }

    public Feature getFeature() {
        return feature;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public void setY(double[] y) {
        this.y = y;
    }

}
