package com.redhat.developer.utils;

public class ExplainabilityUtils {

    /**
     * measure the explainability of an explanation as per paper "Towards Quantification of Explainability in Explainable
     * Artificial Intelligence Methods" by Islam et al.
     *
     * @param inputCognitiveChunks  the no. of cognitive chunks (pieces of information) required to generate the
     *                              explanation (e.g. the no. of explanation inputs)
     * @param outputCognitiveChunks the no. of cognitive chunks generated within the explanation itself
     * @param interactionRatio the ratio of interaction (between 0 and 1) required by the explanation
     * @return the quantitative explainability measure
     */
    public static double quantifyExplainability(int inputCognitiveChunks, int outputCognitiveChunks, double interactionRatio) {
        return inputCognitiveChunks + outputCognitiveChunks > 0 ? 0.333 / (double) inputCognitiveChunks
                + 0.333 / (double) outputCognitiveChunks + 0.333 * (1d - interactionRatio) : 0;
    }
}
