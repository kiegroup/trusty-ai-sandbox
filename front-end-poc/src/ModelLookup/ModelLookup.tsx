import { Divider, PageSection, PageSectionVariants } from "@patternfly/react-core";
import React from 'react';
import { useParams } from "react-router-dom";
import { IExecutionRouteParams } from "../Audit/types";
import ModelDiagram from "./ModelDiagram";
import "./ModelLookup.scss";

const ModelLookup = () => {
    const { executionId } = useParams<IExecutionRouteParams>();

    return (
        <>
            <PageSection
                variant={PageSectionVariants.light}
                style={{ paddingTop: 0, paddingBottom: 0 }}>
                <Divider />
            </PageSection>
            <PageSection variant={"light"}>
                <ModelDiagram executionId={executionId} />
            </PageSection>
        </>
    );
};

export default ModelLookup;