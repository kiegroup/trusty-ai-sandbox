package com.redhat.developer.xai.lime;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.redhat.developer.model.DataDistribution;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.Model;
import com.redhat.developer.model.Output;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.PredictionInput;
import com.redhat.developer.model.PredictionOutput;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.Type;
import com.redhat.developer.model.Value;
import com.redhat.developer.utils.DataUtils;
import com.redhat.developer.xai.lime.pmml.LogisticRegressionIrisDataExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.evaluator.api.executor.PMMLRuntime;

import static com.redhat.developer.xai.lime.pmml.AbstractPMMLTest.getPMMLRuntime;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PmmlLIMEishExplainerTest {

    private static PMMLRuntime localPMMLRuntime;

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
        localPMMLRuntime = getPMMLRuntime("LogisticRegressionIrisData");
    }

    @Disabled
    public void testPMMLRegression() throws IOException {
        LogisticRegressionIrisDataExecutor pmmlModel = new LogisticRegressionIrisDataExecutor(6.9, 3.1, 5.1, 2.3, "virginica");
        PMML4Result result = pmmlModel.execute(localPMMLRuntime);
        String species = result.getResultVariables().get("Species").toString();
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("sepalLength", Type.NUMBER, new Value<>(6.9)));
        features.add(new Feature("sepalWidth", Type.NUMBER, new Value<>(3.1)));
        features.add(new Feature("petalLength", Type.NUMBER, new Value<>(5.1)));
        features.add(new Feature("petalWidth", Type.NUMBER, new Value<>(2.3)));
        PredictionInput input = new PredictionInput(features);
        PredictionOutput output = new PredictionOutput(List.of(new Output("result", Type.STRING, new Value<>("virginica"), 1d)));
        Prediction prediction = new Prediction(input, output);

        LIMEishExplainer limEishExplainer = new LIMEishExplainer(100, 2);
        Model model = new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    List<Feature> features = input.getFeatures();
                    LogisticRegressionIrisDataExecutor pmmlModel = new LogisticRegressionIrisDataExecutor(
                            features.get(0).getValue().asNumber(), features.get(1).getValue().asNumber(),
                            features.get(2).getValue().asNumber(), features.get(3).getValue().asNumber(), "");
                    PMML4Result result = pmmlModel.execute(localPMMLRuntime);
                    String species = result.getResultVariables().get("Species").toString();
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(new Output("species", Type.STRING, new Value<>(species), 1d)));
                    outputs.add(predictionOutput);
                }
                return outputs;
            }

            @Override
            public DataDistribution getDataDistribution() {
                return null;
            }

            @Override
            public PredictionInput getInputShape() {
                return null;
            }

            @Override
            public PredictionOutput getOutputShape() {
                return null;
            }
        };
        Saliency saliency = limEishExplainer.explain(prediction, model);
        assertNotNull(saliency);
        List<String> strings = saliency.getPositiveFeatures(2).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("pizza (text)"));
    }
}