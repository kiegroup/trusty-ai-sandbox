package org.kie.trusty.m2x.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * the data distribution for a given feature
 */
@Schema(name="FeatureDistribution")
public class FeatureDistribution {

    private final double min;
    private final double max;
    private final double mean;
    private final double stdDev;

    @JsonCreator
    public FeatureDistribution(@JsonProperty("min") double min, @JsonProperty("max") double max,
                               @JsonProperty("mean") double mean, @JsonProperty("stdDev") double stdDev) {
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.stdDev = stdDev;
    }

    /**
     * get minimum value for this feature
     *
     * @return the min value
     */
    public double getMin() {
        return min;
    }

    /**
     * get the maximum value for this feature
     *
     * @return the max value
     */
    public double getMax() {
        return max;
    }

    /**
     * get the mean value for this feature
     *
     * @return the mean value
     */
    public double getMean() {
        return mean;
    }

    /**
     * get the standard deviation for this feature
     *
     * @return the standard deviation
     */
    public double getStdDev() {
        return stdDev;
    }
}
