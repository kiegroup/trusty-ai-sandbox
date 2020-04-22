import { DataToolbar, DataToolbarContent, DataToolbarItem } from "@patternfly/react-core/dist/js/experimental";
import React from 'react';
import { IExecutionRouteParams } from "../Audit/types";
import ModelOutcomeDialog from "./ModelOutcomeDialog";

const ModelLookupToolbar = (props: IExecutionRouteParams) => {
    const { id, executionType } = props;

    return (
        <DataToolbar id="model-version-browser">
            <DataToolbarContent>
                <DataToolbarItem>
                    <ModelOutcomeDialog id={id} executionType={executionType} />
                </DataToolbarItem>
            </DataToolbarContent>
        </DataToolbar>
    );
};

export default ModelLookupToolbar;