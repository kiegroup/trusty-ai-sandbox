package com.redhat.developer;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.redhat.developer.model.Model;
import com.redhat.developer.model.Prediction;
import com.redhat.developer.model.Saliency;
import com.redhat.developer.model.dmn.DMNUtils;
import com.redhat.developer.model.dmn.RemoteDMNModel;
import com.redhat.developer.model.dmn.TypedData;
import com.redhat.developer.utils.HttpHelper;
import com.redhat.developer.xai.lime.LimeExplainer;

@ApplicationScoped
public class LocalSaliencyExplanationProvider {

    private static final HttpHelper httpHelper = new HttpHelper("http://producer:1337/");

    /**
     * no. of samples to be generated for the local linear model training
     */
    private final int noOfSamples;

    /**
     * no. of perturbations to perform on a prediction
     */
    private final int noOfPerturbations;

    public LocalSaliencyExplanationProvider() {
        this(100, 1);
    }

    public LocalSaliencyExplanationProvider(int noOfSamples, int noOfPerturbations) {
        this.noOfSamples = noOfSamples;
        this.noOfPerturbations = noOfPerturbations;
    }

    public Saliency explain(List<TypedData> dmnInputs, List<TypedData> dmnOutputs, String modelName) {
        Prediction prediction = DMNUtils.convert(dmnInputs, dmnOutputs);
        Model model = getModel(dmnInputs, dmnOutputs, modelName);
        LimeExplainer limeExplainer = new LimeExplainer(noOfSamples, noOfPerturbations);
        return limeExplainer.explain(prediction, model);
    }

    protected Model getModel(List<TypedData> dmnInputs, List<TypedData> dmnOutputs, String modelName) {
        return new RemoteDMNModel(httpHelper, dmnInputs, dmnOutputs, modelName);
    }
}
