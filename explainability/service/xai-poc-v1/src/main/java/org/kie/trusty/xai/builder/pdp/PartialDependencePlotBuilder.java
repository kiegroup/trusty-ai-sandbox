package org.kie.trusty.xai.builder.pdp;

import org.kie.trusty.xai.explainer.global.viz.PartialDependencePlotProvider;

/**
 * Builder class for {@link PartialDependencePlotProvider}
 */
public class PartialDependencePlotBuilder {

    private final int featureIndex;
    private final int outputIndex;

    private PartialDependencePlotBuilder(int featureIndex, int outputIndex) {
        this.featureIndex = featureIndex;
        this.outputIndex = outputIndex;
    }

    public PartialDependencePlotProvider build() {
        return new PartialDependencePlotProvider(featureIndex, outputIndex);
    }

    public static FeaturePartialDependencePlotBuilder newPDPBuilder() {
        return new FeaturePartialDependencePlotBuilder();
    }

    public static class FeaturePartialDependencePlotBuilder {

        public OutputPartialDependencePlotBuilder onFeature(int index) {
            return new OutputPartialDependencePlotBuilder(index);
        }
    }

    public static class OutputPartialDependencePlotBuilder {

        private final int featureIndex;

        public OutputPartialDependencePlotBuilder(int featureIndex) {
            this.featureIndex = featureIndex;
        }

        public PartialDependencePlotBuilder onOutput(int index) {
            return new PartialDependencePlotBuilder(featureIndex, index);
        }
    }
}
