import React from 'react';

const FormattedValue = (props: { value: any }) => {
    const { value } = props;
    let formattedValue;

    switch (typeof value) {
        case 'number':
        case 'bigint':
            formattedValue = value;
            break;
        case 'string':
            formattedValue = value;
            break;
        case 'boolean':
            formattedValue = value.toString()
            break;
        default:
            formattedValue =  '';
            break;
    }

    return (
        <span>{formattedValue}</span>
    );
};

export default FormattedValue;