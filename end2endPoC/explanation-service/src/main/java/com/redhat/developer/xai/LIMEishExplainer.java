package com.redhat.developer.xai;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureImportance;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.utils.DataUtils;
import com.redhat.developer.utils.ExplainabilityUtils;
import com.redhat.developer.utils.LinearModel;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LIMEishExplainer implements Explainer<Saliency> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * no. of samples to be generated for the local linear model training
     */
    private final int noOfSamples;

    /**
     * no. of perturbations to perform on a prediction
     */
    private final int noOfPerturbations;

    public LIMEishExplainer(int noOfSamples, int noOfPerturbations) {
        this.noOfSamples = noOfSamples;
        this.noOfPerturbations = noOfPerturbations;
    }

    @Override
    public Saliency explain(Prediction prediction, Model model) {

        long start = System.currentTimeMillis();

        List<FeatureImportance> saliencies = new LinkedList<>();
        PredictionInput predictionInput = prediction.getInput();
        List<Feature> features = predictionInput.getFeatures();
        List<Output> actualOutputs = prediction.getOutput().getOutputs();
        int noOfFeatures = features.size();
        double[] weights = new double[noOfFeatures];
        for (int o = 0; o < actualOutputs.size(); o++) {
            boolean separableDataset = false;

            List<PredictionInput> perturbedInputs = new LinkedList<>();
            List<PredictionOutput> predictionOutputs = new LinkedList<>();

            boolean classification = false;
            int sampleSize = noOfSamples;
            int tries = 3;
            Map<Double, Long> rawClassesBalance = new HashMap<>();
            while (!separableDataset && tries > 0) {
                List<PredictionInput> perturbed = getPerturbedInputs(predictionInput, noOfFeatures, sampleSize);
                List<PredictionOutput> perturbedOutputs = model.predict(perturbed);

                rawClassesBalance = perturbedOutputs.stream().map(p -> p.getOutputs().get(0).getValue()
                        .asNumber()).collect(Collectors.groupingBy(Double::doubleValue, Collectors.counting()));
                logger.debug("raw samples per class: {}", rawClassesBalance);

                if (rawClassesBalance.size() > 1) {
                    Long max = rawClassesBalance.values().stream().max(Long::compareTo).get();
                    if ((double) max / (double) perturbed.size() < 0.9) {
                        separableDataset = true;
                        perturbedInputs.addAll(perturbed);
                        predictionOutputs.addAll(perturbedOutputs);
                        classification = rawClassesBalance.size() == 2;
                    }
                } else {
                    sampleSize *= 2;
                    tries--;
                }
            }
            if (!separableDataset) {
                logger.warn("the perturbed inputs / outputs dataset is not (easily) separable: {}", rawClassesBalance);
            }
            List<Output> predictedOutputs = new LinkedList<>();
            for (int i = 0; i < perturbedInputs.size(); i++) {
                Output output = predictionOutputs.get(i).getOutputs().get(o);
                predictedOutputs.add(output);
            }

            Output originalOutput = prediction.getOutput().getOutputs().get(o);

            Collection<Pair<double[], Double>> trainingSet = DataUtils.encodeTrainingSet(perturbedInputs, predictedOutputs,
                                                                                         prediction.getInput(),
                                                                                         originalOutput);

            double[] sampleWeights = getSampleWeights(prediction, noOfFeatures, trainingSet);

            LinearModel linearModel = new LinearModel(noOfFeatures, classification);
            linearModel.fit(trainingSet, sampleWeights);
            for (int i = 0; i < weights.length; i++) {
                weights[i] += linearModel.getWeights()[i] / (double) actualOutputs.size();
            }
            logger.debug("weights updated for output {}", actualOutputs.get(o).getValue());
        }
        for (int i = 0; i < weights.length; i++) {
            FeatureImportance featureImportance = new FeatureImportance(features.get(i), weights[i]);
            saliencies.add(featureImportance);
        }
        long end = System.currentTimeMillis();
        logger.info("explanation time: {}ms", (end - start));
        logger.info("quantified explainability measure: {}",
                    ExplainabilityUtils.quantifyExplainability(noOfFeatures, saliencies.size(), 0));
        return new Saliency(saliencies);
    }

    private List<PredictionInput> getPerturbedInputs(PredictionInput predictionInput, int noOfFeatures, int noOfSamples) {
        List<PredictionInput> perturbedInputs = new LinkedList<>();
        double perturbedDataSize = Math.max(noOfSamples, Math.pow(2, noOfFeatures));
        for (int i = 0; i < perturbedDataSize; i++) {
            perturbedInputs.add(DataUtils.perturbDrop(predictionInput, noOfSamples, noOfPerturbations));
        }
        return perturbedInputs;
    }

    private double[] getSampleWeights(Prediction prediction, int noOfFeatures, Collection<Pair<double[], Double>> training) {
        double[] x = new double[noOfFeatures];
        Arrays.fill(x, 1);

        return training.stream().map(Pair::getLeft).map(
                d -> DataUtils.euclideanDistance(x, d)).map(d -> DataUtils.exponentialSmoothingKernel(d, 0.75 *
                Math.sqrt(noOfFeatures))).mapToDouble(Double::doubleValue).toArray();
    }
}
