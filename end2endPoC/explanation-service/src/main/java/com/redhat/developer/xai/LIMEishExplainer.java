package com.redhat.developer.xai;

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
import com.redhat.developer.model.Value;
import com.redhat.developer.utils.DataUtils;
import com.redhat.developer.utils.ExplainabilityUtils;
import com.redhat.developer.utils.LinearModel;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
        List<Feature> inputFeatures = predictionInput.getFeatures();
        List<Feature> outputFeatures = getOutputFeatures(inputFeatures);
        List<Output> actualOutputs = prediction.getOutput().getOutputs();
        int noOfInputFeatures = inputFeatures.size();
        int noOfOutputFeatures = outputFeatures.size();
        double[] weights = new double[noOfOutputFeatures];

        for (int o = 0; o < actualOutputs.size(); o++) {
            boolean separableDataset = false;

            List<PredictionInput> perturbedInputs = new LinkedList<>();
            List<PredictionOutput> predictionOutputs = new LinkedList<>();

            boolean classification = false;
            int sampleSize = noOfSamples;
            int tries = 3;
            Map<Double, Long> rawClassesBalance = new HashMap<>();
            while (!separableDataset && tries > 0) {
                List<PredictionInput> perturbed = getPerturbedInputs(predictionInput, noOfInputFeatures, sampleSize);
                List<PredictionOutput> perturbedOutputs = model.predict(perturbed);

                rawClassesBalance = perturbedOutputs.stream().map(p -> p.getOutputs().get(0).getValue()
                        .asNumber()).collect(Collectors.groupingBy(Double::doubleValue, Collectors.counting()));
                logger.debug("raw samples per class: {}", rawClassesBalance);

                if (rawClassesBalance.size() > 1) {
                    Long max = rawClassesBalance.values().stream().max(Long::compareTo).get();
                    if ((double) max / (double) perturbed.size() < 0.9) {
                        separableDataset = true;
                        classification = rawClassesBalance.size() == 2;
                    } else {
                        sampleSize *= 2;
                        tries--;
                    }
                } else {
                    sampleSize *= 2;
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

            Collection<Pair<double[], Double>> trainingSet = encodeTrainingSet(perturbedInputs, predictedOutputs, predictionInput, originalOutput);

            double[] sampleWeights = getSampleWeights(prediction, noOfInputFeatures, trainingSet);

            LinearModel linearModel = new LinearModel(outputFeatures.size(), classification);
            linearModel.fit(trainingSet, sampleWeights);
            for (int i = 0; i < weights.length; i++) {
                weights[i] += linearModel.getWeights()[i] / (double) actualOutputs.size();
            }
            logger.debug("weights updated for output {}", actualOutputs.get(o).getValue());
        }
        for (int i = 0; i < weights.length; i++) {
            FeatureImportance featureImportance = new FeatureImportance(outputFeatures.get(i), weights[i]);
            saliencies.add(featureImportance);
        }
        long end = System.currentTimeMillis();
        logger.info("explanation time: {}ms", (end - start));
        logger.info("quantified explainability measure: {}",
                    ExplainabilityUtils.quantifyExplainability(noOfInputFeatures, saliencies.size(), 0));
        return new Saliency(saliencies);
    }

    static Collection<Pair<double[], Double>> encodeTrainingSet(List<PredictionInput> perturbedInputs, List<Output> predictedOutputs, PredictionInput predictionInput, Output originalOutput) {
        Collection<Pair<double[], Double>> trainingSet = new LinkedList<>();
        List<List<Double>> columnData;
        if (!perturbedInputs.isEmpty() && !predictedOutputs.isEmpty() && !predictionInput.getFeatures().isEmpty() && originalOutput != null) {
            columnData = getColumnData(perturbedInputs, predictionInput);

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
                    y = originalOutput.getValue().getUnderlyingObject().equals(output.getValue().getUnderlyingObject()) ? 1d : 0d;
                }
                Pair<double[], Double> sample = new ImmutablePair<>(x, y);
                trainingSet.add(sample);

                pi++;
            }
        }
        return trainingSet;
    }

    private List<Feature> getOutputFeatures(List<Feature> inputFeatures) {
        List<Feature> outputFeatures = new LinkedList<>();
        for (Feature f : inputFeatures) {
            if (Type.STRING.equals(f.getType())) {
                for (String w : f.getValue().asString().split(" ")) {
                    Feature outputFeature = new Feature(w + " (" + f.getName() + ")", Type.STRING, new Value<>(w));
                    outputFeatures.add(outputFeature);
                }
            } else {
                Feature outputFeature = new Feature(f.getName(), f.getType(), new Value<>(f.getValue().getUnderlyingObject()));
                outputFeatures.add(outputFeature);
            }
        }
        return outputFeatures;
    }

    static List<List<Double>> getColumnData(List<PredictionInput> predictionInputs, PredictionInput originalInputs) {
        List<Type> featureTypes = predictionInputs.stream().findFirst().get().getFeatures().stream().map(Feature::getType).collect(Collectors.toList());
        List<List<Double>> columnData = new LinkedList<>();

        for (int t = 0; t < featureTypes.size(); t++) {
            if (!Type.NUMBER.equals(featureTypes.get(t))) {
                // convert values for this feature into numbers
                Feature originalFeature = originalInputs.getFeatures().get(t);
                switch (featureTypes.get(t)) {
                    case STRING:
                        String originalString = originalFeature.getValue().asString();
                        String[] words = originalString.split(" ");
                        for (String word : words) {
                            List<Double> featureValues = new LinkedList<>();
                            for (PredictionInput pi : predictionInputs) {
                                String perturbedString = pi.getFeatures().get(t).getValue().asString();
                                String[] perturbedWords = perturbedString.split(" ");
                                Arrays.sort(perturbedWords);
                                double featureValue = Arrays.binarySearch(perturbedWords, word) >= 0 ? 1d : 0d;
                                featureValues.add(featureValue);
                            }
                            columnData.add(featureValues);
                        }
                        break;
                    case BINARY:
                        encodeEquals(predictionInputs, columnData, t, originalFeature);
                        break;
                    case BOOLEAN:
                        // boolean are automatically encoded as 1s or 0s
                        List<Double> featureValues = new LinkedList<>();
                        for (PredictionInput pi : predictionInputs) {
                            featureValues.add(pi.getFeatures().get(t).getValue().asNumber());
                        }
                        columnData.add(featureValues);
                        break;
                    case DATE:
                        encodeEquals(predictionInputs, columnData, t, originalFeature);
                        break;
                    case URI:
                        encodeEquals(predictionInputs, columnData, t, originalFeature);
                        break;
                    case TIME:
                        encodeEquals(predictionInputs, columnData, t, originalFeature);
                        break;
                    case DURATION:
                        encodeEquals(predictionInputs, columnData, t, originalFeature);
                        break;
                    case VECTOR:
                        encodeEquals(predictionInputs, columnData, t, originalFeature);
                        break;
                    case CURRENCY:
                        encodeEquals(predictionInputs, columnData, t, originalFeature);
                        break;
                    case UNDEFINED:
                        break;
                }
            } else {
                // max - min scaling
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
                double threshold = DataUtils.gaussianKernel((originalValue - min) / (max - min));
                List<Double> featureValues = DoubleStream.of(doubles).map(d -> (d - min) / (max - min))
                        .map(d -> Double.isNaN(d) ? 1 : d).boxed().map(DataUtils::gaussianKernel)
                        .map(d -> (d - threshold < 1e-3) ? 1d : 0d).collect(Collectors.toList());
                columnData.add(featureValues);
            }
        }
        return columnData;
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
