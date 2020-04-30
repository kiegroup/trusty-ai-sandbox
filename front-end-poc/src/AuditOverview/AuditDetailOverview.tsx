import React from 'react';
import {
    Card, CardBody, CardHeader,
    Grid,
    GridItem,
    PageSection,
    Stack,
    StackItem, Title
} from "@patternfly/react-core";
import ExecutionSummary from "./ExecutionSummary";
import ModelOverview from "./ModelOverview";
import OutcomeOverview from "./OutcomeOverview";

const AuditDetailOverview = () => {
    return (
        <PageSection isFilled={true}>
            <Grid gutter="md">
                <GridItem span={6}>
                    <Stack gutter={"md"}>
                        <StackItem>
                            <ExecutionSummary />
                        </StackItem>
                        <StackItem>
                            <ModelOverview />
                        </StackItem>
                    </Stack>
                </GridItem>
                <GridItem span={6}>
                    <Stack gutter="md">
                        <StackItem>
                            <Card>
                                <CardHeader>
                                    <Title headingLevel="h3" size="2xl">
                                        Decision Outcome
                                    </Title>
                                </CardHeader>
                                <CardBody>
                                    <OutcomeOverview />
                                </CardBody>
                            </Card>
                        </StackItem>
                    </Stack>
                </GridItem>
            </Grid>
        </PageSection>
    );
};

export default AuditDetailOverview;