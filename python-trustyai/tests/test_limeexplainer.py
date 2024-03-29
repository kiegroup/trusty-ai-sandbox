# pylint: disable=import-error, wrong-import-position, wrong-import-order, unused-import
"""LIME explainer test suite"""
import sys
import os
import pytest

myPath = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, myPath + "/../")

import common
import trustyai

DEFAULT_NO_OF_PERTURBATIONS = 1

from common import mock_feature
from trustyai.explainers import LimeConfig, LimeExplainer
from trustyai.utils import TestUtils
from trustyai.model import (
    PerturbationContext,
    PredictionInput,
    FeatureFactory,
    SimplePrediction,
)
from java.util import Random
from org.kie.kogito.explainability.local import (
    LocalExplanationException,
)

jrandom = Random()
jrandom.setSeed(0)


def test_empty_prediction():
    """Check if the explanation returned is not null"""
    config = (
        LimeConfig()
        .withPerturbationContext(
            PerturbationContext(jrandom, DEFAULT_NO_OF_PERTURBATIONS)
        )
        .withSamples(10)
    )
    lime_explainer = LimeExplainer(config)
    input_ = PredictionInput([])
    model = TestUtils.getSumSkipModel(0)
    output = model.predictAsync([input_]).get().get(0)
    prediction = SimplePrediction(input_, output)
    with pytest.raises(LocalExplanationException):
        lime_explainer.explain(prediction, model)


def test_non_empty_input():
    """Test for non-empty input"""
    config = (
        LimeConfig()
        .withPerturbationContext(
            PerturbationContext(jrandom, DEFAULT_NO_OF_PERTURBATIONS)
        )
        .withSamples(10)
    )
    lime_explainer = LimeExplainer(config)
    features = [FeatureFactory.newNumericalFeature(f"f-num{i}", i) for i in range(4)]

    _input = PredictionInput(features)

    model = TestUtils.getSumSkipModel(0)
    output = model.predictAsync([_input]).get().get(0)
    prediction = SimplePrediction(_input, output)
    saliency_map = lime_explainer.explain(prediction, model)
    assert saliency_map is not None


def test_sparse_balance():  # pylint: disable=too-many-locals
    """Test sparse balance"""
    for n_features in range(1, 4):
        no_of_samples = 100
        config_no_penalty = (
            LimeConfig()
            .withPerturbationContext(
                PerturbationContext(jrandom, DEFAULT_NO_OF_PERTURBATIONS)
            )
            .withSamples(no_of_samples)
            .withPenalizeBalanceSparse(False)
        )
        lime_explainer_no_penalty = LimeExplainer(config_no_penalty)

        features = [mock_feature(i) for i in range(n_features)]

        input_ = PredictionInput(features)
        model = TestUtils.getSumSkipModel(0)
        output = model.predictAsync([input_]).get().get(0)
        prediction = SimplePrediction(input_, output)

        saliency_map_no_penalty = lime_explainer_no_penalty.explain(prediction, model)

        assert saliency_map_no_penalty is not None

        decision_name = "sum-but0"
        saliency_no_penalty = saliency_map_no_penalty.get(decision_name)

        config = LimeConfig().withSamples(no_of_samples).withPenalizeBalanceSparse(True)
        lime_explainer = LimeExplainer(config)

        saliency_map = lime_explainer.explain(prediction, model)
        assert saliency_map is not None

        saliency = saliency_map.get(decision_name)

        for i in range(len(features)):
            score = saliency.getPerFeatureImportance().get(i).getScore()
            score_no_penalty = (
                saliency_no_penalty.getPerFeatureImportance().get(i).getScore()
            )
            assert abs(score) <= abs(score_no_penalty)


def test_normalized_weights():
    """Test normalized weights"""
    config = (
        LimeConfig()
        .withNormalizeWeights(True)
        .withPerturbationContext(PerturbationContext(jrandom, 2))
        .withSamples(10)
    )
    lime_explainer = LimeExplainer(config)
    n_features = 4
    features = [mock_feature(i) for i in range(n_features)]
    input_ = PredictionInput(features)
    model = TestUtils.getSumSkipModel(0)
    output = model.predictAsync([input_]).get().get(0)
    prediction = SimplePrediction(input_, output)

    saliency_map = lime_explainer.explain(prediction, model)
    assert saliency_map is not None

    decision_name = "sum-but0"
    saliency = saliency_map.get(decision_name)
    per_feature_importance = saliency.getPerFeatureImportance()
    for feature_importance in per_feature_importance:
        assert -1.0 < feature_importance.getScore() < 1.0
