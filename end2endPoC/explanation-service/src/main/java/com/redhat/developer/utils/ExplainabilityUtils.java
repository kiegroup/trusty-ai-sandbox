package com.redhat.developer.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureImportance;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;

public class ExplainabilityUtils {

    /**
     * measure the explainability of an explanation as per paper "Towards Quantification of Explainability in Explainable
     * Artificial Intelligence Methods" by Islam et al.
     *
     * @param inputCognitiveChunks  the no. of cognitive chunks (pieces of information) required to generate the
     *                              explanation (e.g. the no. of explanation inputs)
     * @param outputCognitiveChunks the no. of cognitive chunks generated within the explanation itself
     * @param interactionRatio      the ratio of interaction (between 0 and 1) required by the explanation
     * @return the quantitative explainability measure
     */
    public static double quantifyExplainability(int inputCognitiveChunks, int outputCognitiveChunks, double interactionRatio) {
        return inputCognitiveChunks + outputCognitiveChunks > 0 ? 0.333 / (double) inputCognitiveChunks
                + 0.333 / (double) outputCognitiveChunks + 0.333 * (1d - interactionRatio) : 0;
    }

    /**
     * Calculate the impact of dropping the most important features (given by {@link Saliency#getTopFeatures(int)} from the input.
     * Highly important features would have rather high impact.
     *
     * @param model the model to be explained
     * @param prediction a prediction
     * @param saliency the saliency calculated for the given prediction
     * @param k the number of features to drop
     * @return the saliency impact
     */
    public static double saliencyImpact(Model model, Prediction prediction, Saliency saliency, int k) {
        List<FeatureImportance> topFeatures = saliency.getTopFeatures(k);
        String[] importantFeatureNames = topFeatures.stream().map(f -> f.getFeature().getName()).toArray(String[]::new);

        List<Feature> newFeatures = new LinkedList<>();
        for (Feature feature : prediction.getInput().getFeatures()) {
            Feature newFeature = DataUtils.dropFeature(feature, importantFeatureNames);
            newFeatures.add(newFeature);
        }
        PredictionInput predictionInput = new PredictionInput(newFeatures);
        List<PredictionOutput> predictionOutputs = model.predict(List.of(predictionInput));
        PredictionOutput predictionOutput = predictionOutputs.get(0);
        String modified = predictionOutput.getOutputs().stream().map(o -> o.getValue() + "-" + o.getScore()).collect(Collectors.joining());
        String original = prediction.getOutput().getOutputs().stream().map(o -> o.getValue() + "-" + o.getScore()).collect(Collectors.joining());
        return DataUtils.hammingDistance(original, modified);
    }
}
