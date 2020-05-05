import React, { useEffect, useState } from 'react';
import {
    Divider,
    Grid,
    GridItem,
    PageSection,
    Stack,
    StackItem,
    Title, Tooltip
} from "@patternfly/react-core";
import { HelpIcon } from '@patternfly/react-icons'
import FeaturesTornadoChart from "../FeaturesTornadoChart";
import { useParams } from "react-router-dom";
import { IExecutionRouteParams } from "../../Audit/types";
import { getDecisionOutcome, getDecisionOutcomeDetail } from "../../Shared/api/audit.api";
import { IOutcome } from "../../Outcome/types";
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
            <PageSection variant="default" className="explanation-view__section">
                <div className="container">
                    <Title headingLevel="h2" size="3xl">
                        <span className="explanation-view__title">Decision Explanation: </span>
                        {!outcomeData && <SkeletonInlineStripe />}
                        {outcomeData && <span>{outcomeData[0].outcomeName}</span>}
                    </Title>
                </div>
            </PageSection>
            <section className="container">
                <Divider />
            </section>
            <PageSection variant="default" className="explanation-view__section">
                <div className="container">
                    <Stack gutter="md">
                        <StackItem>
                            <Title headingLevel="h3" size="2xl">Decision Outcome</Title>
                        </StackItem>
                        <StackItem>
                            {!outcomeData && <SkeletonGrid rowsNumber={6} colsNumber={2} gutterSize="md" />}
                            {outcomeData && <OutcomePreview outcomeData={outcomeData} compact={false}/>}
                        </StackItem>
                    </Stack>
                </div>
            </PageSection>
            <PageSection variant="light" className="explanation-view__section">
                <div className="container">
                    <Stack gutter="md">
                        <StackItem>
                            <Title headingLevel="h3" size="2xl">Explanation</Title>
                        </StackItem>
                        <StackItem>
                            <Title headingLevel="h4" size="xl">Features Score Chart</Title>
                            <Grid>
                                <GridItem span={8}>
                                    <FeaturesTornadoChart onlyTopFeatures={true} />
                                </GridItem>
                                <GridItem span={4}>

                                </GridItem>
                            </Grid>
                        </StackItem>
                    </Stack>
                </div>
            </PageSection>
            <PageSection className="explanation-view__section">
                <div className="container">
                    <Stack gutter="md">
                        <StackItem>
                            <Title headingLevel="h3" size="2xl">
                                <span>Decision Influencing Inputs</span>
                                <Tooltip position="auto" content={
                                    <div>
                                        This section displays all the input that contributed to this specific decision outcome.
                                        They can include model inputs (or a subset) or other sub-decisions
                                    </div>
                                }>
                                    <HelpIcon className="explanation-view__input-help" />
                                </Tooltip>
                            </Title>
                        </StackItem>
                        <StackItem>
                            {<InputDataBrowser inputData={outcomeDetail} />}
                        </StackItem>
                    </Stack>
                </div>
            </PageSection>
        </section>
    );
};

export default ExplanationView;