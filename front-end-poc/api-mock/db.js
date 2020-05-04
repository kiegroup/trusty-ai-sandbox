const faker = require('faker');
//const inputDataWithScores = require('./mocks/inputDataWithScores');
const inputData = require('./mocks/inputData');
const outcomeData = require('./mocks/outcomes');
const outcomeDetailData = require('./mocks/outcomeDetail');
const modelData = require('./mocks/modelData')

let generateFakeAPIs = () => {

    let decisionsArray = [];

    for (let id = 1000; id < 1010; id++) {
        let executionDate = faker.date.past();

        decisionsArray.push({
            "executionId": id,
            "executionDate": executionDate,
            "executionType": "DECISION",
            "executionSucceeded": true,
            "executorName": "Technical User"
        });
    }

    let decisionsList = {
        "total": 65,
        "limit": 10,
        "offset": 0,
        "headers": decisionsArray
    };

    let decisionDetail = {
        "executionId": faker.random.number(),
        "executionDate": faker.date.past(),
        "executionType": "DECISION",
        "executionSucceeded": true,
        "executorName": faker.name.findName()
    }

    return {
        "executions": decisionsList,
        "decisions": decisionDetail,
        "inputs": inputData,
        "outcomes": outcomeData,
        "outcomeDetail": outcomeDetailData,
        "models": modelData
    }
};

module.exports = generateFakeAPIs;
