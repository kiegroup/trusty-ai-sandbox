import {ExecutionType} from "../Shared/api/audit.api";

export interface IExecution {
    executionId: string,
    executionDate: Date,
    executionType: string,
    executionSucceeded: boolean,
    executorName: string
}

export interface IExecutionList {
    total: number,
    limit: number,
    offset: number,
    headers: IExecution[]
}

export interface IExecutionRouteParams {
    executionType: ExecutionType,
    id: string
}
