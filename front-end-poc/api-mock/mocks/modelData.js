// See http://dmg.org/pmml/v4-4/Regression.html#xsdElement_RegressionModel
const example1 = "<PMML xmlns='http://www.dmg.org/PMML-4_4' version='4.4'> " +
    "<Header copyright='DMG.org'/>" +
    "<DataDictionary numberOfFields='4'>" +
    "  <DataField name='age' optype='continuous' dataType='double'/>" +
    "  <DataField name='salary' optype='continuous' dataType='double'/>" +
    "  <DataField name='car_location' optype='categorical' dataType='string'>" +
    "    <Value value='carpark'/>" +
    "    <Value value='street'/>" +
    "    <Value value='garage'/>" +
    "  </DataField>" +
    "  <DataField name='number_of_claims' optype='continuous' dataType='integer'/>" +
    "</DataDictionary>" +
    "<RegressionModel modelName='Sample for linear regression' functionName='regression' algorithmName='linearRegression' targetFieldName='number_of_claims'>" +
    "  <MiningSchema>" +
    "    <MiningField name='age'/>" +
    "    <MiningField name='salary'/>" +
    "    <MiningField name='car_location'/>" +
    "    <MiningField name='number_of_claims' usageType='target'/>" +
    "  </MiningSchema>" +
    "  <RegressionTable intercept='132.37'>" +
    "    <NumericPredictor name='age' exponent='1' coefficient='7.1'/>" +
    //  "    <NumericPredictor name='salary' exponent='1' coefficient='0.01'/>" +
    "    <CategoricalPredictor name='car_location' value='carpark' coefficient='41.1'/>" +
    "    <CategoricalPredictor name='car_location' value='street' coefficient='325.03'/>" +
    "    <CategoricalPredictor name='car_location' value='garage' coefficient='-500.0'/>" +
    "  </RegressionTable>" +
    "</RegressionModel>" +
    "</PMML>";

const example2 = "<PMML xmlns='http://www.dmg.org/PMML-4_4' version='4.4'> " +
    "<Header copyright='DMG.org'/>" +
    "<DataDictionary numberOfFields='2'>" +
    "  <DataField name='water_temperature' optype='continuous' dataType='double'/>" +
    "  <DataField name='hemisphere' optype='categorical' dataType='string'>" +
    "    <Value value='northern'/>" +
    "    <Value value='southern'/>" +
    "  </DataField>" +
    "  <DataField name='height_of_tide' optype='continuous' dataType='double'/>" +
    "</DataDictionary>" +
    "<RegressionModel modelName='Tide height' functionName='regression' algorithmName='linearRegression' targetFieldName='height_of_tide'>" +
    "  <MiningSchema>" +
    "    <MiningField name='water_temperature'/>" +
    "    <MiningField name='height_of_tide' usageType='target'/>" +
    "  </MiningSchema>" +
    "  <RegressionTable intercept='1.2'>" +
    "    <NumericPredictor name='water_temperature' exponent='1' coefficient='6'/>" +
    "    <CategoricalPredictor name='hemisphere' value='northern' coefficient='-2.0'/>" +
    "  </RegressionTable>" +
    "</RegressionModel>" +
    "</PMML>";

const example3 = "<PMML xmlns='http://www.dmg.org/PMML-4_4' version='4.4'> " +
    "<Header copyright='DMG.org'/>" +
    "<DataDictionary numberOfFields='2'>" +
    "  <DataField name='age' optype='continuous' dataType='double'>" +
    "    <Interval closure='closedClosed' leftMargin='0' rightMargin='100'/>" +
    "  </DataField>" +
    "  <DataField name='weight' optype='continuous' dataType='double'>" +
    "    <Interval closure='closedClosed' leftMargin='0' rightMargin='200'/>" +
    "  </DataField>" +
    "</DataDictionary>" +
    "<RegressionModel modelName='You get fatter as you get older' functionName='regression' algorithmName='linearRegression' targetFieldName='height_of_tide'>" +
    "  <MiningSchema>" +
    "    <MiningField name='age'/>" +
    "    <MiningField name='weight' usageType='target'/>" +
    "  </MiningSchema>" +
    "  <RegressionTable intercept='2'>" +
    "    <NumericPredictor name='age' exponent='1' coefficient='1.6'/>" +
    "  </RegressionTable>" +
    "</RegressionModel>" +
    "</PMML>";

const modelData = [
    {
        executionId: 1000,
        modelType: "DMN",
        xml: ""
    },
    {
        executionId: 1001,
        modelType: "DMN",
        xml: ""
    },
    {
        executionId: 1002,
        modelType: "DMN",
        xml: ""
    },
    {
        executionId: 1003,
        modelType: "DMN",
        xml: ""
    },
    {
        executionId: 1004,
        modelType: "DMN",
        xml: ""
    },
    {
        executionId: 1005,
        modelType: "PMML",
        xml: example1
    },
    {
        executionId: 1006,
        modelType: "PMML",
        xml: example2
    },
    {
        executionId: 1007,
        modelType: "PMML",
        xml: example3
    },
    {
        executionId: 1008,
        modelType: "PMML",
        xml: example1
    },
    {
        executionId: 1009,
        modelType: "PMML",
        xml: example2
    }
];

module.exports = modelData;