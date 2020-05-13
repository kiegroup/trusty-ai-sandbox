import { Divider, PageSection, PageSectionVariants, Spinner } from "@patternfly/react-core";
import React, { useEffect, useState } from 'react';
import { useParams } from "react-router-dom";
import { IExecutionRouteParams, IFullModelResponse } from "../Audit/types";
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
                    const model: IFullModelResponse = response.data[0] as IFullModelResponse;
                    setViewer(<ModelDiagram model={model} />);
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