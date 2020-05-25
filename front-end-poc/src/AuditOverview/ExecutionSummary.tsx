import React from "react";
import { Card, CardBody, CardHeader, Grid, GridItem, Title } from "@patternfly/react-core";
import { IExecution } from "../Audit/types";
import SkeletonGrid from "../Shared/skeletons/SkeletonGrid";

type ExecutionSummaryProps = {
  executionData: IExecution | null;
};

const ExecutionSummary = (props: ExecutionSummaryProps) => {
  const { executionData } = props;

  return (
    <Card>
      <CardHeader>
        <Title headingLevel="h3" size="2xl">
          Execution Summary
        </Title>
      </CardHeader>
      <CardBody>
        {executionData === null && <SkeletonGrid rowsNumber={2} colsNumber={2} gutterSize="md" />}
        {executionData !== null && (
          <Grid gutter="md" className={"data"}>
            <GridItem span={6}>
              <label className={"data__label"}>Execution ID</label>
              <span>#{executionData.executionId}</span>
            </GridItem>
            <GridItem span={6}>
              <label className={"data__label"}>Execution Status</label>
              <span>{executionData.executionSucceeded ? "Completed" : "Failed"}</span>
            </GridItem>
            <GridItem span={6}>
              <label className={"data__label"}>Executor Name</label>
              <span>{executionData.executorName}</span>
            </GridItem>
            <GridItem span={6}>
              <label className={"data__label"}>Date</label>
              <span>{new Date(executionData.executionDate).toLocaleString()}</span>
            </GridItem>
          </Grid>
        )}
      </CardBody>
    </Card>
  );
};

export default ExecutionSummary;
