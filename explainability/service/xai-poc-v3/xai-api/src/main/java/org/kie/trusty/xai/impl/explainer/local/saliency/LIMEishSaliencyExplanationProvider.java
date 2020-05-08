package org.kie.trusty.xai.impl.explainer.local.saliency;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.kie.trusty.m2x.api.LocalApi;
import org.kie.trusty.m2x.handler.ApiException;
import org.kie.trusty.m2x.handler.Configuration;
import org.kie.trusty.m2x.model.Feature;
import org.kie.trusty.m2x.model.ModelInfo;
import org.kie.trusty.m2x.model.Prediction;
import org.kie.trusty.m2x.model.PredictionInput;
import org.kie.trusty.m2x.model.PredictionOutput;
import org.kie.trusty.xai.impl.explainer.utils.DataUtils;
import org.kie.trusty.xai.impl.explainer.utils.LinearClassifier;
import org.kie.trusty.xai.model.FeatureImportance;
import org.kie.trusty.xai.model.Saliency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LIME-ish explanation implementation.
 * Perturbations to data inputs are performed by randomly dropping features (setting them to 0).
 */
public class LIMEishSaliencyExplanationProvider implements SaliencyLocalExplanationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * no. of samples to be generated for the local linear classifier model training
     */
    private final int noOfSamples;
    private final LocalApi apiInstance;

    LIMEishSaliencyExplanationProvider(LocalApi apiInstance, int noOfSamples) {
        this.apiInstance = apiInstance;
        this.noOfSamples = noOfSamples;
    }

    public LIMEishSaliencyExplanationProvider() {
        this(100);
    }

    public LIMEishSaliencyExplanationProvider(int noOfSamples) {
        this(new LocalApi(Configuration.getDefaultApiClient()), noOfSamples);
    }

    @Override
    public Saliency explain(Prediction prediction) {
        long start = System.currentTimeMillis();
        ModelInfo info = prediction.getModelInfo();
        List<FeatureImportance> saliencies = new LinkedList<>();
        try {
            apiInstance.getApiClient().setBasePath(info.getPredictionEndpoint().toString());
            Collection<Prediction> training = new LinkedList<>();
            List<PredictionInput> perturbedInputs = new LinkedList<>();
            for (int i = 0; i < noOfSamples; i++) {
                perturbedInputs.add(DataUtils.perturbDrop(prediction.getPredictionInput()));
            }
            List<PredictionOutput> predictionOutputs = apiInstance.predict(perturbedInputs);

            for (int i = 0; i < perturbedInputs.size(); i++) {
                Prediction perturbedDataPrediction = new Prediction(info, perturbedInputs.get(i), predictionOutputs.get(i));
                training.add(perturbedDataPrediction);
            }

            List<Feature> features = prediction.getPredictionInput().getFeatures();
            LinearClassifier linearClassifier = new LinearClassifier(features.size());
            linearClassifier.fit(training);
            double[] weights = linearClassifier.getWeights();

            for (int i = 0; i < weights.length; i++) {
                FeatureImportance featureImportance = new FeatureImportance(features.get(i), weights[i]);
                saliencies.add(featureImportance);
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        logger.info("explanation time: {}ms", (end - start));
        return new Saliency(saliencies);
    }
}
