import React, {useEffect, useState} from 'react';
import {IOutcome, IOutcomeResult} from "../types";
import OutcomeRecommendations from "../OutcomeRecommendations/OutcomeRecommendations";

const OutcomePreview = (props: {outcomeData: IOutcome[] | null}) => {
    const {outcomeData} = props;
    const [topDecision, setTopDecision] = useState<IOutcomeResult | null>(null);

    useEffect(() => {
        if (outcomeData) {
            setTopDecision(outcomeData[0].outcomeResults);
        }
    }, [outcomeData]);

    return (
        <>
            {topDecision && <OutcomeRecommendations topDecision={topDecision} />}
        </>
    );
};

export default OutcomePreview;