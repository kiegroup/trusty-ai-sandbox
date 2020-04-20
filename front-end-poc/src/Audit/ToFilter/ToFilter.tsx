import React from 'react';
import {InputGroup, TextInput} from "@patternfly/react-core";

const ToFilter = (props: {toDate: string, minDate: string, onToDateUpdate: (date: string) => void }) => {
    const { toDate, minDate, onToDateUpdate } = props;

    return (
        <InputGroup>
            <TextInput
                name="date-to"
                id="date-to"
                type="date"
                min={minDate}
                aria-label="set starting date"
                value={toDate}
                onChange={onToDateUpdate} />
        </InputGroup>
    );
};

export default ToFilter;