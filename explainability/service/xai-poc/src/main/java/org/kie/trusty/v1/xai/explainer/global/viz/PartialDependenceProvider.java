package org.kie.trusty.v1.xai.explainer.global.viz;

import java.util.stream.DoubleStream;

import org.kie.trusty.v1.DummyModelRegistry;
import org.kie.trusty.v1.Model;
import org.kie.trusty.v1.ModelInfo;
import org.kie.trusty.v1.xai.explainer.global.viz.utils.DataGenerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates the partial dependence tabular data for a given feature.
 * While a strict PD implementation would need the whole training set used to train the model, this implementation seeks
 * to reproduce an approximate version of the training data by means of data distribution information (min, max, mean,
 * stdDev).
 */
public class PartialDependenceProvider implements GlobalVizExplanationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final int featureIndex;

    public PartialDependenceProvider(int featureIndex) {
        this.featureIndex = featureIndex;
    }

    @Override
    public TabularData explain(ModelInfo modelInfo) {
        long start = System.currentTimeMillis();
        int size = 100;
        Model model = DummyModelRegistry.getModel(modelInfo.getId());
        ModelInfo.DataDistribution trainingDataDistributions = modelInfo.getTrainingDataDistribution();
        double[] featureXSvalues = DataGenerationUtils.generateData(trainingDataDistributions.getMin(featureIndex),
                                                                    trainingDataDistributions.getMax(featureIndex), size);

        int noOfFeatures = modelInfo.getInputShape().asDoubles().length;
        double[][] trainingData = new double[noOfFeatures][size];
        for (int i = 0; i < noOfFeatures; i++) {
            double[] featureData = DataGenerationUtils.generateData(trainingDataDistributions.getMean(i),
                                                                    trainingDataDistributions.getStdDeviation(i), size);
            trainingData[i] = featureData;
        }

        double[] marginalImpacts = new double[featureXSvalues.length];
        for (int i = 0; i < featureXSvalues.length; i++) {
            double xs = featureXSvalues[i];
            double[] inputs = new double[noOfFeatures];
            inputs[featureIndex] = xs;
            for (int j = 0; j < size; j++) {
                for (int f = 0; f < noOfFeatures; f++) {
                    if (f != featureIndex) {
                        inputs[f] = trainingData[f][j];
                    }
                }
                // TODO: solve the problem of multiple to one double output in a more elegant way here (sum() is likely to be wrong in many cases)
                marginalImpacts[i] += DoubleStream.of(model.predict(inputs)).sum() / (double) size;
            }
        }
        TabularData tabularData = new TabularData(featureXSvalues, marginalImpacts);
        long end = System.currentTimeMillis();
        logger.info("explanation time: {}ms", (end - start));
        return tabularData;
    }
}
