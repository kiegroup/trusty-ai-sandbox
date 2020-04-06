package org.kie.trusty.v1.xai.explainer.local.saliency;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.kie.trusty.v1.Feature;
import org.kie.trusty.v1.Model;
import org.kie.trusty.v1.ModelInfo;
import org.kie.trusty.v1.DummyModelRegistry;
import org.kie.trusty.v1.Prediction;
import org.kie.trusty.v1.xai.explainer.local.saliency.utils.DataUtils;
import org.kie.trusty.v1.xai.explainer.local.saliency.utils.LinearClassifier;

/**
 * LIME-ish explanation implementation.
 * Perturbations to data inputs are performed by randomly dropping features (setting them to 0).
 */
public class LIMEishSaliencyExplanationProvider implements SaliencyExplanationProvider {

    /**
     * no. of samples to be generated for the local linear classifier model training
     */
    private final int noOfSamples;

    public LIMEishSaliencyExplanationProvider() {
        this.noOfSamples = 100;
    }

    public LIMEishSaliencyExplanationProvider(int noOfSamples) {
        this.noOfSamples = noOfSamples;
    }

    @Override
    public Saliency explain(Prediction prediction) {
        double[] input = prediction.getInput().asDoubles();
        ModelInfo modelInfo = prediction.getModelInfo();
        Model model = DummyModelRegistry.getModel(modelInfo.getId());
        Collection<Prediction> training = new LinkedList<>();
        for (int i = 0; i < noOfSamples; i++) {
            double[] perturbed = DataUtils.perturbDrop(input);
            Prediction perturbedDataPrediction = new Prediction(prediction.getModelInfo(), DataUtils.inputFrom(perturbed),
                                                                DataUtils.outputFrom(model.predict(perturbed)));
            training.add(perturbedDataPrediction);
        }
        LinearClassifier linearClassifier = new LinearClassifier(input.length);
        linearClassifier.fit(training);
        double[] weights = linearClassifier.getWeights();
        Map<Feature, Double> saliencyMap = new HashMap<>();
        for (int i = 0; i < weights.length; i++) {
            saliencyMap.put(prediction.getInput().asFeatureList().get(i),weights[i]);
        }
        return new Saliency(saliencyMap);
    }

}
