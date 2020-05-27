import React, { useState } from "react";
import {
  Button,
  DataList,
  DataListCell,
  DataListItem,
  DataListItemRow,
  DataListItemCells,
  Modal,
  Label,
  StackItem,
  Stack,
} from "@patternfly/react-core";
import { evaluationStatus, IOutcome } from "../../Outcome/types";
import "./ExplanationSelector.scss";

type ExplanationSelectorProps = {
  currentExplanation: string;
  onDecisionSelection: (outcomeId: string) => void;
  outcomesList: IOutcome[];
};

const ExplanationSelector = (props: ExplanationSelectorProps) => {
  const { outcomesList, onDecisionSelection, currentExplanation } = props;
  const [isModalOpen, setIsModalOpen] = useState(false);

  const decisionSelection = (outcomeId: string) => {
    onDecisionSelection(outcomeId);
    toggleModalOpening();
  };

  const toggleModalOpening = () => {
    setIsModalOpen(!isModalOpen);
  };

  return (
    <>
      <Button variant="secondary" onClick={toggleModalOpening} className={"explanation-selector"}>
        View Another Decision
      </Button>
      <Modal
        width={"50%"}
        title="Choose decision explanation"
        isOpen={isModalOpen}
        onClose={toggleModalOpening}
        actions={[
          <Button key="close" variant="tertiary" onClick={toggleModalOpening}>
            Close
          </Button>,
        ]}
        isFooterLeftAligned>
        <Stack gutter="md">
          <StackItem>
            <p>Choose which decision of the model you want explained from the list below.</p>
          </StackItem>
          <StackItem>
            <DataList aria-label="selectable data list example" isCompact>
              <DataListItem aria-labelledby="decision-name" key="list header">
                <DataListItemRow>
                  <DataListItemCells
                    dataListCells={[
                      <DataListCell key="decision name" width={3}>
                        <span id="decision-name">Decision Name</span>
                      </DataListCell>,
                      <DataListCell key="decision evaluation status">Evaluation Status</DataListCell>,
                    ]}
                  />
                </DataListItemRow>
              </DataListItem>
              {outcomesList.map((item) => (
                <DataListItem aria-labelledby={item.outcomeName} id={item.outcomeId} key={item.outcomeId}>
                  <DataListItemRow>
                    <DataListItemCells
                      dataListCells={[
                        <DataListCell key="decision name" width={3}>
                          {item.outcomeId === currentExplanation ? (
                            <span>
                              <span id={item.outcomeName}>{item.outcomeName}</span>{" "}
                              <Label isCompact>Currently viewing</Label>
                            </span>
                          ) : (
                            <Button variant="link" isInline onClick={() => decisionSelection(item.outcomeId)}>
                              <span id={item.outcomeName}>{item.outcomeName}</span>
                            </Button>
                          )}
                        </DataListCell>,
                        <DataListCell key="decision evaluation status">
                          {evaluationStatus[item.evaluationStatus]}
                        </DataListCell>,
                      ]}
                    />
                  </DataListItemRow>
                </DataListItem>
              ))}
            </DataList>
          </StackItem>
        </Stack>
      </Modal>
    </>
  );
};

export default ExplanationSelector;
