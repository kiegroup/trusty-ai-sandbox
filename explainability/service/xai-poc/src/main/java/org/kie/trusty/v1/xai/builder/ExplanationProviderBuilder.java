package org.kie.trusty.v1.xai.builder;

import org.kie.trusty.v1.xai.explainer.local.saliency.LIMEishSaliencyExplanationProvider;
import org.kie.trusty.v1.xai.explainer.local.saliency.SaliencyExplanationProvider;
import org.kie.trusty.v1.xai.explainer.local.saliency.RandomSaliencyExplainerProvider;

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
    }

    public static class LocalBuilder {

        public SaliencyBuilder saliency() {
            return new SaliencyBuilder();
        }

    }

    public static class SaliencyBuilder {

        private SaliencyExplanationProvider provider = null;

        public SaliencyExplanationProvider build() {
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
            provider = new LIMEishSaliencyExplanationProvider();
            return this;
        }
    }
}
