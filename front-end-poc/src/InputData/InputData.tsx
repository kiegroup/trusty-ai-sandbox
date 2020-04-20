import React, {useEffect, useState} from 'react';
import InputDataBrowser from "./InputDataBrowser";
import {Divider, PageSection, PageSectionVariants} from "@patternfly/react-core";
import {getDecisionInput} from "../Shared/api/audit.api";
import {useParams} from "react-router-dom";
import {IExecutionRouteParams} from "../Audit/types";

const InputData = () => {
    const { id } = useParams<IExecutionRouteParams>();
    const [inputData, setInputData] = useState(null);

    useEffect(() => {
        let isMounted = true;
        getDecisionInput(id).then(response => {
            if (isMounted) {
                setInputData(response.data.inputs);
            }
        });
        return () => {
            isMounted = false;
        }
    }, [id]);

    return (
        <>
            <PageSection
                variant={PageSectionVariants.light}
                style={{paddingTop: 0, paddingBottom: 0}}>
                <Divider />
            </PageSection>
            <PageSection variant={PageSectionVariants.light}>
                <InputDataBrowser inputData={inputData} />
            </PageSection>
        </>
    );
};

export default InputData;