export interface IItemObject {
    name: string,
    typeRef: string,
    value: string | number | null,
    components: (IItemObject | IItemObject[])[],
    impact?: boolean | number,
    score?: number
}

export interface IInputRow {
    inputLabel: string,
    inputValue?: string | number | boolean | object | null,
    hasEffect?: boolean | number,
    score?: number,
    key: string,
    category: string
}

export function isIItemObjectArray(object: any): object is IItemObject[] {
    return typeof object[0].name == 'string';
}

export function isIItemObjectMultiArray(object: any): object is IItemObject[][] {
    return Array.isArray(object[0]);
}