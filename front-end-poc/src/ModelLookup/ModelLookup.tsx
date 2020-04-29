import { Divider, PageSection, PageSectionVariants, Spinner } from "@patternfly/react-core";
import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { IExecutionRouteParams } from "../Audit/types";
import { getModelDetail } from "../Shared/api/audit.api";
import ModelDiagram from "./ModelDiagram";
import "./ModelLookup.scss";


const ModelLookup = () => {
    const { executionId } = useParams<IExecutionRouteParams>();
    const [viewer, setViewer] = useState(<Spinner size="xl" />);

    useEffect(() => {
        let didMount = true;
        getModelDetail(executionId)
            .then(response => {
                if (didMount) {
                    const modelType: string = response.data[0].modelType;
                    const xml: string = response.data[0].xml;

                    setViewer(<ModelDiagram executionId={executionId} modelType={modelType} xml={xml} />);
                }
            })
            .catch(() => { });
        return () => {
            didMount = false;
        };
    }, [executionId]);

    return (
        <>
            <PageSection
                variant={PageSectionVariants.light}
                style={{ paddingTop: 0, paddingBottom: 0 }}>
                <Divider />
            </PageSection>
            <PageSection variant={"light"}>
                {viewer}
            </PageSection>
        </>
    );
};

export default ModelLookup;