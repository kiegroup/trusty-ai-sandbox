import { Divider, PageSection, PageSectionVariants } from "@patternfly/react-core";
import React from 'react';
import { useParams } from "react-router-dom";
import { IExecutionRouteParams } from "../Audit/types";
import { ExecutionType } from "../Shared/api/audit.api";
import ModelDiagram from "./ModelDiagram";
import "./ModelLookup.scss";
import ModelLookupToolbar from "./ModelLookupToolbar";

const ModelLookup = () => {
    const { id, executionType } = useParams<IExecutionRouteParams>();

    return (
        <>
            <PageSection
                variant={PageSectionVariants.light}
                style={{ paddingTop: 0, paddingBottom: 0 }}>
                <Divider />
            </PageSection>
            <PageSection variant={"light"}>
                <ModelLookupToolbar id={id} executionType={executionType as ExecutionType} />
                <ModelDiagram id={id} />
            </PageSection>
        </>
    );
};

export default ModelLookup;