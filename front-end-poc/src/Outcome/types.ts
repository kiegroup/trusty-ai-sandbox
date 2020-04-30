import { IItemObject } from "../InputData/types";

export interface IOutcome {
    outcomeId: string,
    outcomeName: string,
    evaluationStatus: string,
    hasErrors: boolean,
    messages: string[],
    outcomeResult: IItemObject
}

export interface IOutcomeDetail extends IOutcome {
    outcomeInputs: IItemObject[]
}
