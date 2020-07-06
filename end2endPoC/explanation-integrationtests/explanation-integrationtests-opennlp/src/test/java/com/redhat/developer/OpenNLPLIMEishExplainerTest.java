package com.redhat.developer;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import com.redhat.developer.model.DataDistribution;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureFactory;
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
import com.redhat.developer.xai.lime.LimeExplainer;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenNLPLIMEishExplainerTest {

    @BeforeAll
    public static void setUpBefore() {
        DataUtils.seed(4);
    }

    @RepeatedTest(10)
    public void testOpenNLPLangDetect() throws IOException {
        InputStream is = getClass().getResourceAsStream("/opennlp/langdetect-183.bin");
        LanguageDetectorModel languageDetectorModel = new LanguageDetectorModel(is);
        String inputText = "italiani, spaghetti pizza mandolino";
        LanguageDetector languageDetector = new LanguageDetectorME(languageDetectorModel);
        Language bestLanguage = languageDetector.predictLanguage(inputText);

        List<Feature> features = new LinkedList<>();
        features.add(FeatureFactory.newTextFeature("text", inputText));
        PredictionInput input = new PredictionInput(features);
        PredictionOutput output = new PredictionOutput(List.of(new Output("lang", Type.TEXT, new Value<>(bestLanguage.getLang()),
                                                                          bestLanguage.getConfidence())));
        Prediction prediction = new Prediction(input, output);

        LimeExplainer limeExplainer = new LimeExplainer(100, 2);
        Model model = new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> results = new LinkedList<>();
                for (PredictionInput predictionInput : inputs) {
                    StringBuilder builder = new StringBuilder();
                    for (Feature f : predictionInput.getFeatures()) {
                        if (Type.TEXT.equals(f.getType())) {
                            if (builder.length() > 0) {
                                builder.append(' ');
                            }
                            builder.append(f.getValue().asString());
                        }
                    }
                    Language language = languageDetector.predictLanguage(builder.toString());
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(new Output("lang", Type.TEXT, new Value<>(language.getLang()), language.getConfidence())));
                    results.add(predictionOutput);
                }
                return results;
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
        Saliency saliency = limeExplainer.explain(prediction, model);
        assertNotNull(saliency);
        double i1 = ExplainabilityUtils.saliencyImpact(model, prediction, saliency, 1);
        assertTrue(i1 > 0);
        double i2 = ExplainabilityUtils.saliencyImpact(model, prediction, saliency, 2);
        assertTrue(i2 > 0);
        assertTrue(i2 <= i1 * 1.5);
        double i3 = ExplainabilityUtils.saliencyImpact(model, prediction, saliency, 3);
        assertTrue(i3 > 0);
        assertTrue(i3 <= i2 * 1.5);
    }
}