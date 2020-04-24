package org.kie.trusty.xai.builder.pdp;

import org.kie.trusty.xai.explainer.global.viz.PartialDependencePlotProvider;

/**
 * Builder class for {@link PartialDependencePlotProvider}
 */
public class PartialDependencePlotBuilder {

    private PartialDependencePlotBuilder() {
    }

    public PartialDependencePlotProvider build() {
        return new PartialDependencePlotProvider();
    }

    public static PartialDependencePlotBuilder newPDPBuilder() {
        return new PartialDependencePlotBuilder();
    }
}
