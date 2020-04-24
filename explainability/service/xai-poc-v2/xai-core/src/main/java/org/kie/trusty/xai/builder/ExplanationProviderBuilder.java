package org.kie.trusty.xai.builder;

import org.kie.trusty.xai.builder.pdp.PartialDependencePlotBuilder;
import org.kie.trusty.xai.explainer.global.saliency.PredictionFeatureImportanceProvider;
import org.kie.trusty.xai.explainer.global.saliency.SaliencyGlobalExplanationProvider;
import org.kie.trusty.xai.explainer.global.saliency.VariableFeatureImportanceProvider;
import org.kie.trusty.xai.explainer.local.saliency.LIMEishSaliencyExplanationProvider;
import org.kie.trusty.xai.explainer.local.saliency.RandomSaliencyExplainerProvider;
import org.kie.trusty.xai.explainer.local.saliency.SaliencyLocalExplanationProvider;

/**
 * Builder class for different kinds of explanation.
 *
 * Current implementation is temporarily keeping all different builder types as internal classes, but they will have to
 * be "exploded" to finite state machine builders in separate classes.
 */
public class ExplanationProviderBuilder {

    public static GlobalVSLocalBuilder newExplanationProviderBuilder() {
        return new GlobalVSLocalBuilder();
    }

    public static class GlobalVSLocalBuilder {

        private GlobalVSLocalBuilder() {

        }

        public LocalBuilder local() {
            return new LocalBuilder();
        }

        public GlobalBuilder global() {
            return new GlobalBuilder();
        }
    }

    public static class LocalBuilder {

        public SaliencyBuilder saliency() {
            return new SaliencyBuilder();
        }

    }

    public static class SaliencyBuilder {

        private SaliencyLocalExplanationProvider provider = null;

        public SaliencyLocalExplanationProvider build() {
            return provider;
        }

        public SaliencyBuilder random() {
            provider = new RandomSaliencyExplainerProvider();
            return this;
        }

        public SaliencyBuilder lime() {
            provider = new LIMEishSaliencyExplanationProvider();
            return this;
        }

        public SaliencyBuilder lime(int noOfSamples) {
            provider = new LIMEishSaliencyExplanationProvider(noOfSamples);
            return this;
        }
    }

    public static class GlobalBuilder {

        public VariableFeatureImportanceBuilder vfi() {
            return new VariableFeatureImportanceBuilder();
        }

        public PredictionFeatureImportanceBuilder prediction() {
            return new PredictionFeatureImportanceBuilder();
        }

        public PartialDependencePlotBuilder partialDependence() {
            return PartialDependencePlotBuilder.newPDPBuilder();
        }
    }

    public static class VariableFeatureImportanceBuilder {

        public SaliencyGlobalExplanationProvider build() {
            return new VariableFeatureImportanceProvider();
        }
    }

    public static class PredictionFeatureImportanceBuilder {

        public SaliencyGlobalExplanationProvider build() {
            return new PredictionFeatureImportanceProvider();
        }
    }
}
