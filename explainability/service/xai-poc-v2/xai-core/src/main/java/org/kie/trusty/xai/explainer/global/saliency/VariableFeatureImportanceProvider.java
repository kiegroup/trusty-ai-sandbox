package org.kie.trusty.xai.explainer.global.saliency;

import org.kie.trusty.xai.model.ModelInfo;
import org.kie.trusty.xai.model.Saliency;

public class VariableFeatureImportanceProvider implements SaliencyGlobalExplanationProvider {

    @Override
    public Saliency explain(ModelInfo modelInfo) {
        throw new RuntimeException("not yet implemented");
    }
}
