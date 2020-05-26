const faker = require("faker");
//const inputDataWithScores = require('./mocks/inputDataWithScores');
const inputData = require("./mocks/inputData");
const outcomeData = require("./mocks/outcomes");
const outcomeDetailData = require("./mocks/outcomeDetail");
const modelData = require("./mocks/modelData");
const featureImportance = require("./mocks/featureImportance");

let generateFakeAPIs = () => {
  let decisionsList = [];

  for (let id = 1000; id < 1010; id++) {
    let executionDate = faker.date.past();

    decisionsList.push({
      executionId: id,
      executionDate: executionDate,
      executionType: "DECISION",
      executionSucceeded: true,
      executorName: "Technical User",
    });
  }

  let executionsList = {
    total: 65,
    limit: 10,
    offset: 0,
    headers: decisionsList,
  };

  return {
    executions: executionsList,
    decisions: decisionsList,
    inputs: inputData,
    outcomes: outcomeData,
    outcomeDetail: outcomeDetailData,
    models: modelData,
    featureImportance: featureImportance,
  };
};

module.exports = generateFakeAPIs;
