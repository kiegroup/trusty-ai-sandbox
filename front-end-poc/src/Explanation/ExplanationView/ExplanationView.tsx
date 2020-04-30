import React, { useEffect, useState } from 'react';
import {
    Divider,
    Grid,
    GridItem,
    PageSection,
    Stack,
    StackItem,
    Title
} from "@patternfly/react-core";
import { HelpIcon } from '@patternfly/react-icons'
import FeaturesTornadoChart from "../FeaturesTornadoChart";
import { useParams } from "react-router-dom";
import { IExecutionRouteParams } from "../../Audit/types";
import { getDecisionOutcome, getDecisionOutcomeDetail } from "../../Shared/api/audit.api";
import { IOutcome, IOutcomeDetail } from "../../Outcome/types";
import OutcomePreview from "../../Outcome/OutcomePreview/OutcomePreview";
import InputDataBrowser from "../../InputData/InputDataBrowser";
import SkeletonInlineStripe from "../../Shared/skeletons/SkeletonInlineStripe";
import SkeletonGrid from "../../Shared/skeletons/SkeletonGrid";
import './ExplanationView.scss';

const ExplanationView = () => {
    const { executionId } = useParams<IExecutionRouteParams>();
    const [outcomeData, setOutcomeData] = useState<IOutcome[] | null>(null);
    const [outcomeId, setOutcomeId] = useState<string | null>(null);
    const [outcomeDetail, setOutcomeDetail] = useState(null);

    useEffect(() => {
        let isMounted = true;
        getDecisionOutcome(executionId).then(response => {
            if (isMounted) {
                if (response.data && response.data.outcomes) {
                    setOutcomeData(response.data.outcomes.slice(0, 1));
                    let defaultOutcome = response.data.outcomes[0];
                    setOutcomeId(defaultOutcome.outcomeId);
                    console.log(defaultOutcome);
                }
            }
        });
        return () => {
            isMounted = false;
        }
    }, [executionId]);

    useEffect(() => {
        let isMounted = true;
        if (outcomeId !== null) {
            getDecisionOutcomeDetail(executionId, outcomeId).then(response => {
                if (isMounted) {
                    if (response.data && response.data && response.data.outcomeInputs) {
                        setOutcomeDetail(response.data.outcomeInputs);
                        console.log(response.data);
                    }
                }
            });
        }
        return () => {
            isMounted = false;
        }
    }, [executionId, outcomeId]);

    return (
        <section className="explanation-view">
            <PageSection variant="default">
                <Title headingLevel="h2" size="3xl">
                    <span className="explanation-view__title">Decision Explanation: </span>
                    {!outcomeData && <SkeletonInlineStripe />}
                    {outcomeData && <span>{outcomeData[0].outcomeName}</span>}
                </Title>
            </PageSection>
            <Divider style={{ paddingLeft: "1em", paddingRight: "1em" }} />
            <PageSection variant="default">
                <Stack gutter="md">
                    <StackItem>
                        <Title headingLevel="h3" size="2xl">Decision Outcome</Title>
                    </StackItem>
                    <StackItem>
                        {!outcomeData && <SkeletonGrid rowsNumber={6} colsNumber={2} gutterSize="md" />}
                        {outcomeData && <OutcomePreview outcomeData={outcomeData} />}
                    </StackItem>
                </Stack>
            </PageSection>
            <PageSection variant="light">
                <Stack gutter="md">
                    <StackItem>
                        <Title headingLevel="h3" size="2xl">Explanation</Title>
                    </StackItem>
                    <StackItem>
                        <Grid>
                            <GridItem span={9}>
                                <FeaturesTornadoChart onlyTopFeatures={false} />
                            </GridItem>
                            <GridItem span={3}>
                                Score table view
                            </GridItem>
                        </Grid>
                    </StackItem>
                </Stack>
            </PageSection>
            <PageSection>
                <Stack gutter="md">
                    <StackItem>
                        <Title headingLevel="h3" size="2xl"><span>Decision Influencing Inputs</span> <HelpIcon style={{ fill: "gray" }} /></Title>
                    </StackItem>
                    <StackItem>
                        {<InputDataBrowser inputData={outcomeDetail} />}
                    </StackItem>
                </Stack>
            </PageSection>
        </section>
    );
};

export default ExplanationView;