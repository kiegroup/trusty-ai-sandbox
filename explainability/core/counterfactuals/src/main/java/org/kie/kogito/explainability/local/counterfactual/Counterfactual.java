package org.kie.kogito.explainability.local.counterfactual;

import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.model.PredictionOutput;

import java.util.List;

public class Counterfactual {

    public List<CounterfactualEntity> getEntities() {
        return entities;
    }

    private List<CounterfactualEntity> entities;

    public List<PredictionOutput> getOutput() {
        return output;
    }

    private List<PredictionOutput> output;

    public Counterfactual(List<CounterfactualEntity> entities, List<PredictionOutput> output) {
        this.entities = entities;
        this.output = output;
    }

}
