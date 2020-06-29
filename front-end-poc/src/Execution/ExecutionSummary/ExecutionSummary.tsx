import React from "react";
import "./ExecutionSummary.scss";
import { PageSection, Stack, StackItem } from "@patternfly/react-core";
import DataWithLabel from "../../Shared/components/DataWithLabel/DataWithLabel";
import { IExecution } from "../../Audit/types";
import { RemoteData } from "../../Shared/types";
import ExecutionStatus from "../../Audit/ExecutionStatus/ExecutionStatus";
import FormattedDate from "../../Shared/components/FormattedDate/FormattedDate";

type ExecutionSummaryProps = {
  execution: RemoteData<Error, IExecution>;
};

const ExecutionSummary = (props: ExecutionSummaryProps) => {
  const { execution } = props;
  return (
    <div className="execution-summary">
      {execution.status === "SUCCESS" && (
        <PageSection variant="light">
          <Stack hasGutter>
            <StackItem>
              <DataWithLabel label="Execution ID">{execution.data.executionId.toUpperCase()}</DataWithLabel>
            </StackItem>
            <StackItem>
              <DataWithLabel label="Model">{execution.data.executedModelName}</DataWithLabel>
            </StackItem>
            <StackItem>
              <DataWithLabel label="Execution Status">
                <ExecutionStatus result={execution.data.executionSucceeded} />
              </DataWithLabel>
            </StackItem>
            <StackItem>
              <DataWithLabel label="Requested by">{execution.data.executorName}</DataWithLabel>
            </StackItem>
            <StackItem>
              <DataWithLabel label="Completed on">
                <FormattedDate date={execution.data.executionDate} preposition={false} />
              </DataWithLabel>
            </StackItem>
          </Stack>
        </PageSection>
      )}
    </div>
  );
};

export default ExecutionSummary;
