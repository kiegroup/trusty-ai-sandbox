import { ExecutionType } from "../Shared/api/audit.api";

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
    executionId: string,
    executionType: ExecutionType
}
