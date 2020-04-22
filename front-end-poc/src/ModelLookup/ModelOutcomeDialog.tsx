import React, { useState } from 'react';
import { Button, Divider, Modal, Stack, StackItem, Title } from "@patternfly/react-core";
import OutcomeList from "../Outcome/OutcomeList/OutcomeList";
import { IExecutionRouteParams } from "../Audit/types"

const ModelOutcomeDialog = (props: IExecutionRouteParams) => {
    const { id, executionType } = props;
    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleModalToggle = () => {
        setIsModalOpen(!isModalOpen);
    };
    let executionTitle = `Produced by execution `;

    return (
        <>
            <Button variant="secondary" onClick={handleModalToggle}>View {executionType} Outcome</Button>
            <Modal
                width={'50%'}
                title="Decision Outcome"
                isOpen={isModalOpen}
                onClose={handleModalToggle}
                actions={[
                    <Button key="cancel" variant="link" onClick={handleModalToggle}>
                        Close
                    </Button>
                ]}
                isFooterLeftAligned
            >
                <Stack gutter="md">
                    <StackItem>
                        <Title size="xl" headingLevel="h4"><strong>{executionType}</strong> {executionTitle} <strong>{id}</strong></Title>
                    </StackItem>
                    <StackItem isFilled>
                        <Divider component="div" />
                    </StackItem>
                    <StackItem>
                        <div style={{
                            paddingRight: 20
                        }}>
                            <OutcomeList allAttributes={true} />
                        </div>
                    </StackItem>
                </Stack>

            </Modal>
        </>
    );
};

export default ModelOutcomeDialog;