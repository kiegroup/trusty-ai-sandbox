package com.redhat.developer.xai.lime;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.redhat.developer.model.DataDistribution;
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
import com.redhat.developer.xai.ExplanationTestUtils;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        features.add(new Feature("text", Type.STRING, new Value<>(inputText)));
        PredictionInput input = new PredictionInput(features);
        PredictionOutput output = new PredictionOutput(List.of(new Output("lang", Type.STRING, new Value<>(bestLanguage.getLang()),
                                                                          bestLanguage.getConfidence())));
        Prediction prediction = new Prediction(input, output);

        LIMEishExplainer limEishExplainer = new LIMEishExplainer(100, 2);
        Model model = new Model() {
            @Override
            public List<PredictionOutput> predict(List<PredictionInput> inputs) {
                List<PredictionOutput> results = new LinkedList<>();
                for (PredictionInput predictionInput : inputs) {
                    StringBuilder builder = new StringBuilder();
                    for (Feature f : predictionInput.getFeatures()) {
                        if (Type.STRING.equals(f.getType())) {
                            if (builder.length() > 0) {
                                builder.append(' ');
                            }
                            builder.append(f.getValue().asString());
                        }
                    }
                    Language language = languageDetector.predictLanguage(builder.toString());
                    PredictionOutput predictionOutput = new PredictionOutput(List.of(new Output("lang", Type.STRING, new Value<>(language.getLang()), language.getConfidence())));
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
        Saliency saliency = limEishExplainer.explain(prediction, model);
        assertNotNull(saliency);
        List<String> strings = saliency.getPositiveFeatures(2).stream().map(f -> f.getFeature().getName()).collect(Collectors.toList());
        assertTrue(strings.contains("pizza (text)"));
    }
}