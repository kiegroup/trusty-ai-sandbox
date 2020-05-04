import React from 'react';
import './FormattedValue.scss';

const FormattedList = (props: { valueList: any[] }) => {
    const { valueList } = props;

    if (valueList.length === 0) {
        return <span className="formatted-list formatted-list--no-entries">No entries</span>
    }
    return (
        <span className="formatted-list">
            {valueList.map((item, index) => (
                <React.Fragment key={Math.floor(Math.random() * 10000)}>
                    <FormattedValue value={item} key={Math.floor(Math.random() * 10000)} />
                    {(index < valueList.length - 1) && <span key={Math.floor(Math.random() * 10000)}>, </span>}
                </React.Fragment>
            ))}
        </span>
    );
};

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
        case 'object':
            if (Array.isArray(value)) {
                formattedValue = <FormattedList valueList={value} />
            }
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