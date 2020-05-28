import { IItemObject } from "../InputData/types";

export enum evaluationStatus {
  EVALUATING = "Evaluating",
  FAILED = "Failed",
  NOT_EVALUATED = "Not evaluated",
  SKIPPED = "Skipped",
  SUCCEEDED = "Succeeded",
}

export type evaluationStatusStrings = keyof typeof evaluationStatus;

export interface IOutcome {
  outcomeId: string;
  outcomeName: string;
  evaluationStatus: evaluationStatusStrings;
  hasErrors: boolean;
  messages: string[];
  outcomeResult: IItemObject;
}
