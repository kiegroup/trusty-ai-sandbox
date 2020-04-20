import React, {useEffect, useState} from 'react';
import {getDecisionOutcome} from "../Shared/api/audit.api";
import {useParams} from "react-router-dom";
import {IExecutionRouteParams} from "../Audit/types";
import OutcomePreview from "../Outcome/OutcomePreview/OutcomePreview";
import SkeletonGrid from "../Shared/skeletons/SkeletonGrid";

const OutcomeOverview = () => {
    const { id } = useParams<IExecutionRouteParams>();
    const [outcomeData, setOutcomeData] = useState(null);

    useEffect(() => {
        let isMounted = true;
        getDecisionOutcome(id).then(response => {
            if (isMounted) {
                setOutcomeData(response.data.outcomes);
            }
        });
        return () => {
            isMounted = false;
        }
    }, [id]);

    return (
        <>
            {!outcomeData && <SkeletonGrid rowsNumber={8} colsNumber={2} gutterSize="md"/>}
            {outcomeData && <OutcomePreview outcomeData={outcomeData} />}
        </>
    );
};

export default OutcomeOverview;