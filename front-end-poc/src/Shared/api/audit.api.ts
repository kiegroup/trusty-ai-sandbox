import { callOnce, httpClient } from "./httpClient";
import { AxiosRequestConfig } from "axios";

const EXECUTIONS_PATH = "/executions";
const DECISIONS_PATH = EXECUTIONS_PATH + "/decisions";
const PROCESS_PATH = EXECUTIONS_PATH + "/processes";

export type ExecutionType = 'decision' | 'process';

const getExecPath = (executionType: ExecutionType) => {
    switch (executionType) {
        case 'decision': {
            return DECISIONS_PATH;
        }
        case 'process': {
            return PROCESS_PATH;
        }
        default: {
            return EXECUTIONS_PATH
        }
    }
};

const getExecutions = (searchString: string, from: string, to: string, limit: number, offset: number) => {
    const getExecutionsConfig: AxiosRequestConfig = {
        url: EXECUTIONS_PATH,
        method: 'get',
        params: {
            search: searchString,
            from,
            to,
            limit,
            offset
        }
    };
    return callOnce(getExecutionsConfig);
};

const getExecution = (executionType: ExecutionType, id: string) => {
    const getExecConfig: AxiosRequestConfig = {
        url: `${getExecPath(executionType)}/${id}`,
        method: 'get'
    };
    return httpClient(getExecConfig);
};

const getDecisionInput = (id: string) => {
    const getDecisionInputConfig: AxiosRequestConfig = {
        url: `${DECISIONS_PATH}/${id}/structuredInputs`,
        method: 'get'
    }
    return httpClient(getDecisionInputConfig);
};

const getDecisionOutcome = (id: string) => {
    const getDecisionOutcomeConfig: AxiosRequestConfig = {
        url: `${DECISIONS_PATH}/${id}/outcomes`,
        method: 'get'
    }
    return httpClient(getDecisionOutcomeConfig);
}

const getDecisionFeatureScores = (executionId: string) => {
    const requestCfg: AxiosRequestConfig = {
        url: `${DECISIONS_PATH}/${executionId}/featureImportance`,
        method: 'get'
    }
    return httpClient(requestCfg);
}

const getDecisionOutcomeDetail = (executionId: string, outcomeId: string) => {
    const getDecisionOutcomeDetailConfig: AxiosRequestConfig = {
        url: `${DECISIONS_PATH}/${executionId}/outcomes/${outcomeId}`,
        method: 'get'
    }
    return httpClient(getDecisionOutcomeDetailConfig);
}

const getModelDetail = (executionId: string) => {
    const getModelDetailConfig: AxiosRequestConfig = {
        url: `${DECISIONS_PATH}/${executionId}/model`,
        method: 'get'
    }
    return httpClient(getModelDetailConfig);
}

export {
    getExecutions,
    getExecution,
    getDecisionInput,
    getDecisionOutcome,
    getDecisionOutcomeDetail,
    getModelDetail,
    getDecisionFeatureScores
};