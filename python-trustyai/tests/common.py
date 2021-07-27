# pylint: disable=wrong-import-position
"""Common methods and models for tests"""
import trustyai

trustyai.init(
    path=[
        "./dep/org/kie/kogito/explainability-core/1.8.0.Final/*",
        "./dep/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar",
        "./dep/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar",
        "./dep/org/optaplanner/optaplanner-core/8.8.0.Final/optaplanner-core-8.8.0.Final.jar",
        "./dep/org/apache/commons/commons-math3/3.6.1/commons-math3-3.6.1.jar",
        "./dep/org/kie/kie-api/7.55.0.Final/kie-api-7.55.0.Final.jar",
        "./dep/io/micrometer/micrometer-core/1.6.6/micrometer-core-1.6.6.jar",
    ]
)


from trustyai.model import (
    FeatureFactory,
    Output,
    PredictionOutput,
    Type,
    Value,
)


def mock_feature(value):
    """Create a mock numerical feature"""
    return FeatureFactory.newNumericalFeature("f-num", value)


def sum_skip_model(inputs):
    """SumSkip test model"""
    prediction_outputs = []
    for prediction_input in inputs:
        features = prediction_input.getFeatures()
        result = 0.0
        for i in range(features.size()):
            if i != 0:
                result += features.get(i).getValue().asNumber()
        output = [Output("sum-but0", Type.NUMBER, Value(result), 1.0)]
        prediction_output = PredictionOutput(output)
        prediction_outputs.append(prediction_output)
    return prediction_outputs
