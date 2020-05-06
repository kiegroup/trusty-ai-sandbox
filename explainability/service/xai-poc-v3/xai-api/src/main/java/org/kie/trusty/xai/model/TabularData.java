package org.kie.trusty.xai.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.kie.trusty.m2x.model.Feature;

@Schema
public class TabularData {

    private final double[] x;
    private final double[] y;
    private final Feature feature;

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
}
