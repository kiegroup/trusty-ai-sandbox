import React from 'react';
import {
    Card, CardBody, CardHeader, Flex, FlexItem, FlexModifiers,
    Grid,
    GridItem,
    PageSection,
    Stack,
    StackItem, Title
} from "@patternfly/react-core";
import NestedInputDataList from "../InputData/NestedInputDataList";
import SingleDecisionOutput from "./SingleDecisionOutput";
import DecisionOutputData from "../Mocks/decision-outcome-mock";
import ModelShortSummary from "./ModelShortSummary";
import FeaturesTornadoChart from "../Explanation/FeaturesTornadoChart";
import DecisionDistributionChart from "../ModelLookup/DecisionDistributionChart";
import LoanInputDetail from "../Mocks/loan-input-detail-mock";
import InputDataList from "../InputData/InputDataList";

/*
    This visualization is meant for simple models, where
    input data and outcome are composed by low amount of data with
    low data type complexity
*/
const AuditCompactView = () => {
    return (
        <PageSection isFilled={true}>
            <Grid gutter="md">
                <GridItem span={6}>
                    <Stack gutter={"md"}>
                        <StackItem>
                            <NestedInputDataList />
                        </StackItem>
                        <StackItem>
                                <InputDataList inputData={LoanInputDetail}/>
                        </StackItem>
                    </Stack>
                </GridItem>
                <GridItem span={6}>
                    <Stack gutter="md">
                        <StackItem>
                            <Flex breakpointMods={[{modifier: FlexModifiers.grow}]}>
                                <FlexItem style={{flex: "1", alignSelf: "stretch"}}>
                                    <SingleDecisionOutput decision={DecisionOutputData}/>
                                </FlexItem>
                                <FlexItem style={{flex: "1", alignSelf: "stretch"}}>
                                    <ModelShortSummary />
                                </FlexItem>
                            </Flex>
                        </StackItem>
                        <StackItem>
                            <Card>
                                <CardHeader>
                                    <Title headingLevel="h3" size="2xl">
                                        Feature Explanation
                                    </Title>
                                </CardHeader>
                                <CardBody>
                                    <FeaturesTornadoChart onlyTopFeatures={true} />
                                </CardBody>
                            </Card>
                        </StackItem>
                        <StackItem>
                            <Card>
                                <CardHeader>
                                    <Title headingLevel="h3" size="2xl">
                                        Decision distribution (last 90 days)
                                    </Title>
                                </CardHeader>
                                <CardBody>
                                    <DecisionDistributionChart />
                                </CardBody>
                            </Card>
                        </StackItem>
                    </Stack>
                </GridItem>
            </Grid>
        </PageSection>
    );
};

export default AuditCompactView;