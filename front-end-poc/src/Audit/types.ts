import { ExecutionType } from "../Shared/api/audit.api";

export interface IExecution {
  executionId: string;
  executionDate: string;
  executedModelName: string;
  executionType: string;
  executionSucceeded: boolean;
  executorName: string;
}

export interface IExecutionRouteParams {
  executionId: string;
  executionType: ExecutionType;
}

export interface IOutcomeRouteParams extends IExecutionRouteParams {
  outcomeId: string;
}

export interface IServiceIdentifier {
  groupId?: string;
  artifactId?: string;
  version?: string;
}

export interface IExecutionModelResponse {
  deploymentDate?: string;
  modelId?: string;
  name: string;
  namespace: string;
  type: string;
  serviceIdentifier: IServiceIdentifier;
  model: string;
}
