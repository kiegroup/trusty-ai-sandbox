import React, {useState} from 'react';
import {
    Button,
    Card,
    CardBody,
    CardHeader,
    Grid,
    GridItem, Modal,
    PageSection,
    Stack,
    StackItem,
    Title
} from "@patternfly/react-core";
import OutcomeList from "../Outcome/OutcomeList";
import FeaturesTornadoChart from "./FeaturesTornadoChart";
import NestedInputDataList from "../InputData/NestedInputDataList";

const Explanation = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleModalToggle = () => {
        setIsModalOpen(!isModalOpen);
    };
    return (
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
    );
};

export default Explanation;