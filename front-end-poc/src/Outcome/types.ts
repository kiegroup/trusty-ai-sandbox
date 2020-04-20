export interface IOutcome {
    outcomeId: string,
    outcomeName: string,
    evaluationStatus: string,
    hasErrors: boolean,
    messages: string[],
    outcomeResults: IOutcomeResult
}

export interface IOutcomeResult {
    name: string,
    typeRef: string,
    value: string | number | null,
    components: (IOutcomeResult | IOutcomeResult[])[],
}

export function isIOutcomeResultArray(object: any): object is IOutcomeResult[] {
    return typeof object[0].name == 'string';
}
export function isOutcomeResultMultiArray(object: any): object is IOutcomeResult[][] {
    return Array.isArray(object[0]);
}