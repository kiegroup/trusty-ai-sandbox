package org.kie.trusty.m2x.model;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Information about distribution of data used for training a model
 */
@Schema(name="DataDistribution", description="Information about distribution of data used for training a model")
public class DataDistribution {

    private final List<FeatureDistribution> featureDistributions;

    @JsonCreator
    public DataDistribution(@JsonProperty("featureDistributions") List<FeatureDistribution> featureDistributions) {
        this.featureDistributions = Collections.unmodifiableList(featureDistributions);
    }

    /**
     * Get each feature data distribution
     *
     * @return feature distributions
     */
    @Schema
    public List<FeatureDistribution> getFeatureDistributions() {
        return featureDistributions;
    }
}
