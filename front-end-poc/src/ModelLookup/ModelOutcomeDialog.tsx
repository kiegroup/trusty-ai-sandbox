import React, { useState } from 'react';
import { Button, Divider, Modal, Stack, StackItem, Title } from "@patternfly/react-core";
import OutcomeList from "../Outcome/OutcomeList/OutcomeList";
import { IModelVersion } from "./types"

type MODProps = {
    isOriginalVersion: boolean,
    selectedVersion: IModelVersion
}

const ModelOutcomeDialog = (props: MODProps) => {
    const { isOriginalVersion, selectedVersion } = props;
    const [isModalOpen, setIsModalOpen] = useState(false);
    const handleModalToggle = () => {
        setIsModalOpen(!isModalOpen);
    };
    let decisionTitle = `produced by model version `;
    let decisionType = (isOriginalVersion) ? "Original decision" : "Simulated decision";

    return (
        <>
            <Button variant="secondary" onClick={handleModalToggle}>{isOriginalVersion ? "View Decision Outcome" : "Simulate Decision"}</Button>
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
                        <Title size="xl" headingLevel="h4"><strong>{decisionType}</strong> {decisionTitle} <strong>{selectedVersion.version}</strong></Title>
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