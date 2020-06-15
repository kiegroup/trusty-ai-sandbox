import React, { useState } from "react";
import { Button, Modal, Label, StackItem, Stack } from "@patternfly/react-core";
import { IOutcome } from "../../Outcome/types";
import "./ExplanationSelector.scss";
import EvaluationStatus from "../EvaluationStatus/EvaluationStatus";
import { Table, TableBody, TableHeader } from "@patternfly/react-table";

type ExplanationSelectorProps = {
  currentExplanationId: string;
  onDecisionSelection: (outcomeId: string) => void;
  outcomesList: IOutcome[];
};

const ExplanationSelector = (props: ExplanationSelectorProps) => {
  const { outcomesList, onDecisionSelection, currentExplanationId } = props;
  const [isModalOpen, setIsModalOpen] = useState(false);

  const columns = ["Decision Name", "Evaluation Status"];
  const rows = outcomesList.map((item) => {
    return {
      cells: [
        {
          title:
            item.outcomeId === currentExplanationId ? (
              <span>
                <span id={item.outcomeName}>{item.outcomeName}</span>{" "}
                <Label className="explanation-currently-viewed">Currently viewing</Label>
              </span>
            ) : (
              <Button variant="link" isInline onClick={() => decisionSelection(item.outcomeId)}>
                <span id={item.outcomeName}>{item.outcomeName}</span>
              </Button>
            ),
          colSpan: 3,
        },
        { title: <EvaluationStatus status={item.evaluationStatus} /> },
      ],
    };
  });

  const decisionSelection = (outcomeId: string) => {
    onDecisionSelection(outcomeId);
    toggleModalOpening();
  };

  const toggleModalOpening = () => {
    setIsModalOpen(!isModalOpen);
  };

  return (
    <>
      <Button
        variant="secondary"
        onClick={toggleModalOpening}
        className="explanation-selector"
        title="Select another decision">
        Switch Outcome
      </Button>
      <Modal
        width={"50%"}
        title="Decisions List"
        isOpen={isModalOpen}
        onClose={toggleModalOpening}
        actions={[
          <Button key="close" variant="tertiary" onClick={toggleModalOpening}>
            Close
          </Button>,
        ]}>
        <Stack hasGutter>
          <StackItem>
            <p>Choose which decision of the model you want explained from the list below.</p>
          </StackItem>
          <StackItem>
            <Table aria-label="Decisions List" cells={columns} rows={rows}>
              <TableHeader />
              <TableBody />
            </Table>
          </StackItem>
        </Stack>
      </Modal>
    </>
  );
};

export default ExplanationSelector;
