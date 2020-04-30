import React, { useEffect, useState } from 'react';
import {
    Button,
    Card,
    CardBody,
    CardHeader, Divider,
    Grid,
    GridItem, Modal,
    PageSection, PageSectionVariants,
    Stack,
    StackItem,
    Title
} from "@patternfly/react-core";
import { HelpIcon } from '@patternfly/react-icons'
import OutcomeList from "../Outcome/OutcomeList/OutcomeList";
import FeaturesTornadoChart from "./FeaturesTornadoChart";
import NestedInputDataList from "../InputData/NestedInputDataList";
import { useParams } from "react-router-dom";
import { IExecutionRouteParams } from "../Audit/types";
import { getDecisionOutcome, getDecisionOutcomeDetail } from "../Shared/api/audit.api";
import { IOutcome, IOutcomeDetail } from "../Outcome/types";
import OutcomePreview from "../Outcome/OutcomePreview/OutcomePreview";
import InputDataBrowser from "../InputData/InputDataBrowser";
import SkeletonInlineStripe from "../Shared/skeletons/SkeletonInlineStripe";
import SkeletonGrid from "../Shared/skeletons/SkeletonGrid";

const Explanation = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleModalToggle = () => {
        setIsModalOpen(!isModalOpen);
    };

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
        <>
            <PageSection
                variant={PageSectionVariants.light}
                style={{ paddingTop: 0, paddingBottom: 0 }}>
                <Divider />
            </PageSection>
            <PageSection variant="light">
                <Stack gutter="md">
                    <StackItem>
                        <Title headingLevel="h2" size="3xl">
                            <strong>Decision Explanation: </strong>
                            {!outcomeData && <SkeletonInlineStripe />}
                            {outcomeData && <span>{outcomeData[0].outcomeName}</span>}
                        </Title>
                    </StackItem>
                    <StackItem>
                        <Title headingLevel="h3" size="2xl">Decision Outcome</Title>
                    </StackItem>
                    <StackItem>
                        {!outcomeData && <SkeletonGrid rowsNumber={6} colsNumber={2} gutterSize="md" />}
                        {outcomeData && <OutcomePreview outcomeData={outcomeData} />}
                    </StackItem>
                    <StackItem>
                        <Title headingLevel="h3" size="2xl">Explanation</Title>
                    </StackItem>
                    <StackItem>
                        Chart placeholder
                    </StackItem>
                    <StackItem>
                        <Title headingLevel="h3" size="2xl">Decision Influencing Inputs <HelpIcon/></Title>
                    </StackItem>
                    <StackItem>
                        {<InputDataBrowser inputData={outcomeDetail} />}
                    </StackItem>
                </Stack>
            </PageSection>
            <PageSection isFilled={true}>
                <Grid gutter="md">
                    <GridItem span={6}>
                        <Stack gutter={"md"}>
                            <StackItem>
                                <Card>
                                    <CardHeader>
                                        <Title headingLevel="h3" size="2xl">
                                            DECISION OUTCOME
                                        </Title>
                                    </CardHeader>
                                    <CardBody>
                                        <OutcomeList allAttributes={true}/>
                                    </CardBody>
                                </Card>
                            </StackItem>
                        </Stack>
                    </GridItem>
                    <GridItem span={6}>
                        <Stack gutter="md">
                            <StackItem>
                                <Card>
                                    <CardHeader>
                                        <Title headingLevel="h3" size="2xl">
                                            Top Features Explanation
                                        </Title>
                                    </CardHeader>
                                    <CardBody>
                                        <FeaturesTornadoChart onlyTopFeatures={true} />
                                        <div style={{textAlign: "right"}}>
                                            <Button variant="link" onClick={handleModalToggle}>
                                                View Complete Chart
                                            </Button>
                                        </div>
                                        <Modal
                                            width={'80%'}
                                            title="Features Explanation"
                                            isOpen={isModalOpen}
                                            onClose={handleModalToggle}
                                            actions={[
                                                <Button key="close" onClick={handleModalToggle}>
                                                    Close
                                                </Button>
                                            ]}
                                            isFooterLeftAligned
                                        >
                                            <FeaturesTornadoChart />
                                        </Modal>
                                    </CardBody>
                                </Card>
                            </StackItem>
                            <StackItem>
                                <NestedInputDataList showOnlyAffecting={true}/>
                            </StackItem>
                        </Stack>
                    </GridItem>
                </Grid>
            </PageSection>
        </>
    );
};

export default Explanation;