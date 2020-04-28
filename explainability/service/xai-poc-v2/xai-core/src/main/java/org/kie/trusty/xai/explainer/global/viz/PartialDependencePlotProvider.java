package org.kie.trusty.xai.explainer.global.viz;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import io.swagger.client.api.GlobalApi;
import org.kie.trusty.xai.explainer.utils.DataUtils;
import org.kie.trusty.xai.handler.ApiException;
import org.kie.trusty.xai.handler.Configuration;
import org.kie.trusty.xai.model.DataDistribution;
import org.kie.trusty.xai.model.Feature;
import org.kie.trusty.xai.model.ModelInfo;
import org.kie.trusty.xai.model.Output;
import org.kie.trusty.xai.model.PredictionInput;
import org.kie.trusty.xai.model.PredictionOutput;
import org.kie.trusty.xai.model.TabularData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates the partial dependence tabular data for a given feature.
 * While a strict PD implementation would need the whole training set used to train the model, this implementation seeks
 * to reproduce an approximate version of the training data by means of data distribution information (min, max, mean,
 * stdDev).
 */
public class PartialDependencePlotProvider implements GlobalVizExplanationProvider {

    private static final int TABLE_SIZE = 100;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final GlobalApi apiInstance;

    PartialDependencePlotProvider(GlobalApi apiInstance) {
        this.apiInstance = apiInstance;
    }

    public PartialDependencePlotProvider() {
        apiInstance = new GlobalApi(Configuration.getDefaultApiClient());
    }

    @Override
    public Collection<TabularData> explain(ModelInfo modelInfo) {
        long start = System.currentTimeMillis();

        apiInstance.getApiClient().setBasePath(modelInfo.getEndpoint());

        Collection<TabularData> pdps = new LinkedList<>();
        try {
            DataDistribution dataDistribution = apiInstance.dataDistribution();
            int noOfFeatures = modelInfo.getInputShape();

            for (int featureIndex = 0; featureIndex < noOfFeatures; featureIndex++) {
                for (int outputIndex = 0; outputIndex < modelInfo.getOutputShape(); outputIndex++) {
                    double[] featureXSvalues = DataUtils.generateSamples(dataDistribution.getFeatureDistributions().get(featureIndex).getMin().doubleValue(),
                                                                         dataDistribution.getFeatureDistributions().get(featureIndex).getMax().doubleValue(), TABLE_SIZE);

                    TabularData tabularData = new TabularData();
                    double[][] trainingData = new double[noOfFeatures][TABLE_SIZE];
                    for (int i = 0; i < noOfFeatures; i++) {
                        double[] featureData = DataUtils.generateData(dataDistribution.getFeatureDistributions().get(i).getMean().doubleValue(),
                                                                      dataDistribution.getFeatureDistributions().get(i).getStdDev().doubleValue(), TABLE_SIZE);
                        trainingData[i] = featureData;
                    }

                    double[] marginalImpacts = new double[featureXSvalues.length];
                    for (int i = 0; i < featureXSvalues.length; i++) {
                        List<PredictionInput> predictionInputs = new LinkedList<>();
                        double xs = featureXSvalues[i];
                        double[] inputs = new double[noOfFeatures];
                        inputs[featureIndex] = xs;
                        for (int j = 0; j < TABLE_SIZE; j++) {
                            PredictionInput input = new PredictionInput();
                            for (int f = 0; f < noOfFeatures; f++) {
                                if (f != featureIndex) {
                                    inputs[f] = trainingData[f][j];
                                }
                            }
                            input.setFeatures(DataUtils.doublesToFeatures(inputs));
                            predictionInputs.add(input);
                        }
                        // prediction requests are batched per value of feature 'Xs' under analysis
                        for (PredictionOutput predictionOutput : apiInstance.predict(predictionInputs)) {
                            Output output = predictionOutput.getOutputs().get(outputIndex);
                            marginalImpacts[i] += output.getScore().doubleValue() / (double) TABLE_SIZE;
                        }
                    }
                    tabularData.setXAxis(DataUtils.doublesToBigDecimals(featureXSvalues));
                    tabularData.setYAxis(DataUtils.doublesToBigDecimals(marginalImpacts));
                    Feature feature = new Feature();
                    feature.setName("f" + featureIndex + "_o" + outputIndex);
                    tabularData.setFeature(feature);
                    pdps.add(tabularData);
                }
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        logger.info("explanation time: {}ms", (end - start));
        return pdps;
    }
}
