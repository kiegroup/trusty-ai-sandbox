package com.redhat.developer.xai.lime;

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
import com.redhat.developer.xai.lime.pmml.CategoricalVariablesRegressionExecutor;
import com.redhat.developer.xai.lime.pmml.CompoundNestedPredicateScorecardExecutor;
import com.redhat.developer.xai.lime.pmml.LogisticRegressionIrisDataExecutor;
import com.redhat.developer.xai.lime.pmml.SimpleScorecardCategoricalExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.kie.api.pmml.PMML4Result;
import org.kie.pmml.evaluator.api.executor.PMMLRuntime;

import static com.redhat.developer.xai.lime.pmml.AbstractPMMLTest.getPMMLRuntime;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PmmlLIMEishExplainerTest {

    private static PMMLRuntime logisticRegressionIris;
    private static PMMLRuntime categoricalVariableRegression;
    private static PMMLRuntime scorecardCategorical;
    private static PMMLRuntime compoundScoreCard;

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
        logisticRegressionIris = getPMMLRuntime("LogisticRegressionIrisData");
        categoricalVariableRegression = getPMMLRuntime("categoricalVariables_Model");
        scorecardCategorical = getPMMLRuntime("SimpleScorecardCategorical");
        compoundScoreCard = getPMMLRuntime("CompoundNestedPredicateScorecard");
    }

    @RepeatedTest(10)
    public void testPMMLRegression() {
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("sepalLength", Type.NUMBER, new Value<>(6.9)));
        features.add(new Feature("sepalWidth", Type.NUMBER, new Value<>(3.1)));
        features.add(new Feature("petalLength", Type.NUMBER, new Value<>(5.1)));
        features.add(new Feature("petalWidth", Type.NUMBER, new Value<>(2.3)));
        PredictionInput input = new PredictionInput(features);
        PredictionOutput output = new PredictionOutput(List.of(new Output("result", Type.TEXT, new Value<>("virginica"), 1d)));
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
                            features.get(2).getValue().asNumber(), features.get(3).getValue().asNumber());
                    PMML4Result result = pmmlModel.execute(logisticRegressionIris);
                    String species = result.getResultVariables().get("Species").toString();
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(new Output("species", Type.TEXT, new Value<>(species), 1d)));
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
        assertTrue(strings.contains("petalWidth"));
    }

    @RepeatedTest(10)
    public void testPMMLRegressionCategorical() {
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("mapX", Type.TEXT, new Value<>("red")));
        features.add(new Feature("mapY", Type.TEXT, new Value<>("classB")));
        PredictionInput input = new PredictionInput(features);
        PredictionOutput output = new PredictionOutput(List.of(new Output("result", Type.NUMBER, new Value<>(3.4d), 1d)));
        Prediction prediction = new Prediction(input, output);

        LIMEishExplainer limEishExplainer = new LIMEishExplainer(100, 2);
        Model model = new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    List<Feature> features = input.getFeatures();
                    CategoricalVariablesRegressionExecutor pmmlModel = new CategoricalVariablesRegressionExecutor(
                            features.get(0).getValue().asString(), features.get(1).getValue().asString());
                    PMML4Result result = pmmlModel.execute(categoricalVariableRegression);
                    String score = result.getResultVariables().get("result").toString();
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(new Output("result", Type.NUMBER, new Value<>(score), 1d)));
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
        List<String> strings = saliency.getPositiveFeatures(1).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("red (mapX)"));
    }

    @RepeatedTest(10)
    public void testPMMLScorecardCategorical() {
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("input1", Type.TEXT, new Value<>("classA")));
        features.add(new Feature("input2", Type.TEXT, new Value<>("classB")));
        PredictionInput input = new PredictionInput(features);
        List<Output> outputs = List.of(new Output("score", Type.TEXT, new Value<>(25), 1d),
                                       new Output("reason1", Type.TEXT, new Value<>("Input1ReasonCode"),1d),
                                       new Output("reason2", Type.TEXT, new Value<>("null"),1d));
        PredictionOutput output = new PredictionOutput(outputs);
        Prediction prediction = new Prediction(input, output);

        LIMEishExplainer limEishExplainer = new LIMEishExplainer(100, 2);
        Model model = new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    List<Feature> features = input.getFeatures();
                    SimpleScorecardCategoricalExecutor pmmlModel = new SimpleScorecardCategoricalExecutor(
                            features.get(0).getValue().asString(), features.get(1).getValue().asString());
                    PMML4Result result = pmmlModel.execute(scorecardCategorical);
                    String score = ""+result.getResultVariables().get(SimpleScorecardCategoricalExecutor.TARGET_FIELD);
                    String reason1 = ""+result.getResultVariables().get(SimpleScorecardCategoricalExecutor.REASON_CODE1_FIELD);
                    String reason2 = ""+result.getResultVariables().get(SimpleScorecardCategoricalExecutor.REASON_CODE2_FIELD);
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(
                            new Output("score", Type.TEXT, new Value<>(score), 1d),
                            new Output("reason1", Type.TEXT, new Value<>(reason1), 1d),
                            new Output("reason2", Type.TEXT, new Value<>(reason2), 1d)
                    ));
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
        List<String> strings = saliency.getPositiveFeatures(1).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("classA (input1)"));
    }

    @RepeatedTest(10)
    public void testPMMLCompoundScorecard() {
        List<Feature> features = new LinkedList<>();
        features.add(new Feature("input1", Type.NUMBER, new Value<>(-50)));
        features.add(new Feature("input2", Type.TEXT, new Value<>("classB")));
        PredictionInput input = new PredictionInput(features);
        List<Output> outputs = List.of(new Output("score", Type.TEXT, new Value<>(-8), 1d),
                                       new Output("reason1", Type.TEXT, new Value<>("characteristic2ReasonCode"),1d),
                                       new Output("reason2", Type.TEXT, new Value<>("null"),1d));
        PredictionOutput output = new PredictionOutput(outputs);
        Prediction prediction = new Prediction(input, output);

        LIMEishExplainer limEishExplainer = new LIMEishExplainer(100, 2);
        Model model = new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> outputs = new LinkedList<>();
                for (PredictionInput input : inputs) {
                    List<Feature> features = input.getFeatures();
                    CompoundNestedPredicateScorecardExecutor pmmlModel = new CompoundNestedPredicateScorecardExecutor(
                            features.get(0).getValue().asNumber(), features.get(1).getValue().asString());
                    PMML4Result result = pmmlModel.execute(compoundScoreCard);
                    String score = ""+result.getResultVariables().get(CompoundNestedPredicateScorecardExecutor.TARGET_FIELD);
                    String reason1 = ""+result.getResultVariables().get(CompoundNestedPredicateScorecardExecutor.REASON_CODE1_FIELD);
                    String reason2 = ""+result.getResultVariables().get(CompoundNestedPredicateScorecardExecutor.REASON_CODE2_FIELD);
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(
                            new Output("score", Type.TEXT, new Value<>(score), 1d),
                            new Output("reason1", Type.TEXT, new Value<>(reason1), 1d),
                            new Output("reason2", Type.TEXT, new Value<>(reason2), 1d)
                    ));
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
        List<String> strings = saliency.getPositiveFeatures(1).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("input1"));
    }
}