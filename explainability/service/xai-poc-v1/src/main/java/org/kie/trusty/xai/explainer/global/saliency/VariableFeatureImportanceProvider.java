package org.kie.trusty.xai.explainer.global.saliency;

import org.kie.trusty.v1.ModelInfo;
import org.kie.trusty.xai.explainer.Saliency;

public class VariableFeatureImportanceProvider implements SaliencyGlobalExplanationProvider {

    @Override
    public Saliency explain(ModelInfo modelInfo) {
        throw new RuntimeException("not yet implemented");
    }
}
