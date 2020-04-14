package org.kie.trusty.v1.xai.builder.pdp;

import org.kie.trusty.v1.xai.explainer.global.viz.PartialDependenceProvider;

/**
 * Builder class for {@link PartialDependenceProvider}
 */
public class PartialDependenceBuilder {

    private final int featureIndex;
    private final int outputIndex;

    private PartialDependenceBuilder(int featureIndex, int outputIndex) {
        this.featureIndex = featureIndex;
        this.outputIndex = outputIndex;
    }

    public PartialDependenceProvider build() {
        return new PartialDependenceProvider(featureIndex, outputIndex);
    }

    public static FeaturePartialDependenceBuilder newPDPBuilder() {
        return new FeaturePartialDependenceBuilder();
    }

    public static class FeaturePartialDependenceBuilder {

        public OutputPartialDependenceBuilder onFeature(int index) {
            return new OutputPartialDependenceBuilder(index);
        }
    }

    public static class OutputPartialDependenceBuilder {

        private final int featureIndex;

        public OutputPartialDependenceBuilder(int featureIndex) {
            this.featureIndex = featureIndex;
        }

        public PartialDependenceBuilder onOutput(int index) {
            return new PartialDependenceBuilder(featureIndex, index);
        }
    }
}
