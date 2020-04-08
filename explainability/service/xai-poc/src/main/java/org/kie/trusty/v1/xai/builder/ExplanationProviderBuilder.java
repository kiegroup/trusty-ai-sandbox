package org.kie.trusty.v1.xai.builder;

import org.kie.trusty.v1.xai.explainer.global.saliency.PredictionFeatureImportanceProvider;
import org.kie.trusty.v1.xai.explainer.global.saliency.SaliencyGlobalExplanationProvider;
import org.kie.trusty.v1.xai.explainer.global.saliency.VariableFeatureImportanceProvider;
import org.kie.trusty.v1.xai.explainer.global.viz.GlobalVizExplanationProvider;
import org.kie.trusty.v1.xai.explainer.global.viz.PartialDependenceProvider;
import org.kie.trusty.v1.xai.explainer.local.saliency.LIMEishSaliencyExplanationProvider;
import org.kie.trusty.v1.xai.explainer.local.saliency.RandomSaliencyExplainerProvider;
import org.kie.trusty.v1.xai.explainer.local.saliency.SaliencyLocalExplanationProvider;

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
            return null;
        }

        public PartialDependenceBuilder partialDependence(int featureIndex) {
            return new PartialDependenceBuilder(featureIndex);
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

    public static class PartialDependenceBuilder {

        private final PartialDependenceProvider provider;

        public PartialDependenceBuilder(int featureIndex) {
            this.provider = new PartialDependenceProvider(featureIndex);
        }

        public GlobalVizExplanationProvider build() {
            return provider;
        }
    }
}
