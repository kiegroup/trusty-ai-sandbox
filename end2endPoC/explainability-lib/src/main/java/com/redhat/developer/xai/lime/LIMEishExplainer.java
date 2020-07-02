package com.redhat.developer.xai.lime;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureImportance;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.Type;
import com.redhat.developer.utils.DataUtils;
import com.redhat.developer.utils.ExplainabilityUtils;
import com.redhat.developer.utils.LinearModel;
import com.redhat.developer.xai.LocalExplainer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of LIME algorithm (Ribeiro et al., 2018) optimised for tabular data and decision models.
 * Differences with respect to the original paper implementation:
 * - the linear (interpretable) model is based on a perceptron algorithm instead of Lasso + Ridge regression
 * - perturbing numerical features is done by sampling from a normal distribution centered around the value of the feature value associated with the prediction to be explained
 * - numerical features are max-min scaled and clustered via a gaussian kernel
 */
public class LIMEishExplainer implements LocalExplainer<Saliency> {

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
        PredictionInput originalInput = prediction.getInput();
        List<Feature> inputFeatures = originalInput.getFeatures();
        PredictionInput targetInput = DataUtils.linearizeInputs(List.of(originalInput)).get(0);
        List<Feature> linearizedTargetInputFeatures = targetInput.getFeatures();
        List<Output> actualOutputs = prediction.getOutput().getOutputs();
        int noOfInputFeatures = inputFeatures.size();
        int noOfOutputFeatures = linearizedTargetInputFeatures.size();
        double[] weights = new double[noOfOutputFeatures];

        for (int o = 0; o < actualOutputs.size(); o++) {
            boolean separableDataset = false;

            List<PredictionInput> perturbedInputs = new LinkedList<>();
            List<PredictionOutput> predictionOutputs = new LinkedList<>();

            boolean classification = false;
            int tries = 3;
            Map<Double, Long> rawClassesBalance = new HashMap<>();
            Output currentOutput = actualOutputs.get(o);
            while (!separableDataset && tries > 0) {
                List<PredictionInput> perturbed = getPerturbedInputs(originalInput, noOfInputFeatures, noOfSamples);
                List<PredictionOutput> perturbedOutputs = model.predict(perturbed);

                Object fv = currentOutput != null && currentOutput.getValue() != null ? currentOutput.getValue().getUnderlyingObject() : null;

                int finalO = o;
                rawClassesBalance = perturbedOutputs.stream().map(p -> p.getOutputs().get(finalO)).map(output -> (Type.NUMBER
                        .equals(output.getType())) ? output.getValue().asNumber() : (((output.getValue().getUnderlyingObject() == null
                        && fv == null) || output.getValue().getUnderlyingObject().equals(fv)) ? 1d : 0d))
                        .collect(Collectors.groupingBy(Double::doubleValue, Collectors.counting()));
                logger.debug("raw samples per class: {}", rawClassesBalance);

                if (rawClassesBalance.size() > 1) {
                    Long max = rawClassesBalance.values().stream().max(Long::compareTo).get();
                    if ((double) max / (double) perturbed.size() < 0.9) {
                        separableDataset = true;
                        classification = rawClassesBalance.size() == 2;
                    } else {
                        tries--;
                    }
                } else {
                    tries--;
                }
                if (tries == 0 || separableDataset) {
                    perturbedInputs.addAll(perturbed);
                    predictionOutputs.addAll(perturbedOutputs);
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

            List<Pair<double[], Double>> trainingSet = encodeTrainingSet(perturbedInputs, predictedOutputs, targetInput, originalOutput);

            double[] sampleWeights = getSampleWeights(prediction, noOfOutputFeatures, trainingSet);

            LinearModel linearModel = new LinearModel(linearizedTargetInputFeatures.size(), classification);
            linearModel.fit(trainingSet, sampleWeights);
            for (int i = 0; i < weights.length; i++) {
                weights[i] += linearModel.getWeights()[i] / (double) actualOutputs.size();
            }
            logger.debug("weights updated for output {}", currentOutput);
        }
        for (int i = 0; i < weights.length; i++) {
            FeatureImportance featureImportance = new FeatureImportance(linearizedTargetInputFeatures.get(i), weights[i]);
            saliencies.add(featureImportance);
        }
        long end = System.currentTimeMillis();
        logger.info("explanation time: {}ms", (end - start));
        logger.info("quantified explainability measure: {}",
                    ExplainabilityUtils.quantifyExplainability(noOfInputFeatures, saliencies.size(), 0));
        return new Saliency(saliencies);
    }

    static List<Pair<double[], Double>> encodeTrainingSet(List<PredictionInput> perturbedInputs, List<Output> predictedOutputs,
                                                          PredictionInput targetInput, Output originalOutput) {
        List<Pair<double[], Double>> trainingSet = new LinkedList<>();
        List<List<Double>> columnData;
        List<PredictionInput> flatInputs = DataUtils.linearizeInputs(perturbedInputs);
        if (!flatInputs.isEmpty() && !predictedOutputs.isEmpty() && !targetInput.getFeatures().isEmpty() && originalOutput != null) {
            columnData = getColumnData(flatInputs, targetInput);

            int pi = 0;
            for (Output output : predictedOutputs) {
                double[] x = new double[columnData.size()];
                int i = 0;
                for (List<Double> column : columnData) {
                    x[i] = column.get(pi);
                    i++;
                }
                double y;
                if (Type.NUMBER.equals(originalOutput.getType()) || Type.BOOLEAN.equals(originalOutput.getType())) {
                    y = output.getValue().asNumber();
                } else {
                    Object originalObject = originalOutput.getValue().getUnderlyingObject();
                    Object outputObject = output.getValue().getUnderlyingObject();
                    if (originalObject == null || outputObject == null) {
                        y = originalObject == outputObject ? 1d : 0d;
                    } else {
                        y = originalObject.equals(outputObject) ? 1d : 0d;
                    }
                }
                Pair<double[], Double> sample = new ImmutablePair<>(x, y);
                trainingSet.add(sample);

                pi++;
            }
        }
        return trainingSet;
    }

    static List<List<Double>> getColumnData(List<PredictionInput> perturbed, PredictionInput target) {
        List<Type> featureTypes = target.getFeatures().stream().map(Feature::getType).collect(Collectors.toList());
        List<List<Double>> columnData = new LinkedList<>();

        for (int t = 0; t < featureTypes.size(); t++) {
            if (!Type.NUMBER.equals(featureTypes.get(t))) {
                // convert values for this feature into numbers
                Feature originalFeature = target.getFeatures().get(t);
                switch (featureTypes.get(t)) {
                    case TEXT:
                        encodeText(perturbed, columnData, t, originalFeature);
                        break;
                    case CATEGORICAL:
                    case BINARY:
                    case TIME:
                    case URI:
                    case DATE:
                    case DURATION:
                    case VECTOR:
                    case CURRENCY:
                        encodeEquals(perturbed, columnData, t, originalFeature);
                        break;
                    case BOOLEAN:
                        // boolean are automatically encoded as 1s or 0s
                        List<Double> featureValues = new LinkedList<>();
                        for (PredictionInput pi : perturbed) {
                            featureValues.add(pi.getFeatures().get(t).getValue().asNumber());
                        }
                        columnData.add(featureValues);
                        break;
                    case NESTED:
                        break;
                }
            } else {
                encodeNumbers(perturbed, target, columnData, t);
            }
        }
        return columnData;
    }

    private static void encodeNumbers(List<PredictionInput> predictionInputs, PredictionInput originalInputs, List<List<Double>> columnData, int t) {
        // find maximum and minimum values
        double[] doubles = new double[predictionInputs.size() + 1];
        int i = 0;
        for (PredictionInput pi : predictionInputs) {
            Feature feature = pi.getFeatures().get(t);
            doubles[i] = feature.getValue().asNumber();
            i++;
        }
        Feature feature = originalInputs.getFeatures().get(t);
        double originalValue = feature.getValue().asNumber();
        doubles[i] = originalValue;
        double min = DoubleStream.of(doubles).min().getAsDouble();
        double max = DoubleStream.of(doubles).max().getAsDouble();
        // feature scaling + kernel based binning
        double threshold = DataUtils.gaussianKernel((originalValue - min) / (max - min));
        List<Double> featureValues = DoubleStream.of(doubles).map(d -> (d - min) / (max - min))
                .map(d -> Double.isNaN(d) ? 1 : d).boxed().map(DataUtils::gaussianKernel)
                .map(d -> (d - threshold < 1e-3) ? 1d : 0d).collect(Collectors.toList());
        columnData.add(featureValues);
    }

    private static void encodeText(List<PredictionInput> predictionInputs, List<List<Double>> columnData, int t, Feature originalFeature) {
        String originalString = originalFeature.getValue().asString();
        String[] words = originalString.split(" ");
        for (String word : words) {
            List<Double> featureValues = new LinkedList<>();
            for (PredictionInput pi : predictionInputs) {
                Feature feature = pi.getFeatures().stream().filter(f -> f.getName().equals(originalFeature.getName())).findFirst().orElse(null);
                double featureValue;
                if (feature != null && feature.getName().equals(originalFeature.getName())) {
                    String perturbedString = feature.getValue().asString();
                    String[] perturbedWords = perturbedString.split(" ");
                    Arrays.sort(perturbedWords);
                    featureValue = Arrays.binarySearch(perturbedWords, word) >= 0 ? 1d : 0d;
                } else {
                    featureValue = 0d;
                }
                featureValues.add(featureValue);
            }
            columnData.add(featureValues);
        }
    }

    private static void encodeEquals(List<PredictionInput> predictionInputs, List<List<Double>> columnData, int t, Feature originalFeature) {
        Object originalObject = originalFeature.getValue().getUnderlyingObject();
        List<Double> featureValues = new LinkedList<>();
        for (PredictionInput pi : predictionInputs) {
            double featureValue = originalObject.equals(pi.getFeatures().get(t).getValue().getUnderlyingObject()) ? 1d : 0d;
            featureValues.add(featureValue);
        }
        columnData.add(featureValues);
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
