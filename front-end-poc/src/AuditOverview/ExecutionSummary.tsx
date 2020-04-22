import React, { useEffect, useState } from 'react';
import { Card, CardBody, CardHeader, Grid, GridItem, Title } from "@patternfly/react-core";
import { useParams } from "react-router-dom";
import { ExecutionType, getExecution } from "../Shared/api/audit.api";
import { IExecution, IExecutionRouteParams } from "../Audit/types";
import SkeletonGrid from "../Shared/skeletons/SkeletonGrid";


const ExecutionSummary = () => {
    const { executionId, executionType } = useParams<IExecutionRouteParams>();
    const [executionData, setExecutionData] = useState<IExecution | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        getExecution(executionType as ExecutionType, executionId).then(response => {
            setIsLoading(false);
            let execution = {
                ...response.data,
                executionDate: new Date(response.data.executionDate).toLocaleString(),
                executionSucceeded: (response.data.executionSucceeded) ? "Completed" : "Failed"
            };
            setExecutionData(execution);
        });
    }, [executionType, executionId]);

    return (
        <Card>
            <CardHeader>
                <Title headingLevel="h3" size="2xl">
                    Execution Summary
                </Title>
            </CardHeader>
            <CardBody>
                {isLoading && <SkeletonGrid rowsNumber={2} colsNumber={2} gutterSize="md" />}
                {!isLoading && (
                    <Grid gutter="md" className={"data"}>
                        <GridItem span={6}>
                            <label className={"data__label"}>Execution ID</label>
                            <span>#{executionData && executionData.executionId}</span>
                        </GridItem>
                        <GridItem span={6}>
                            <label className={"data__label"}>Execution Status</label>
                            <span>{executionData && executionData.executionSucceeded}</span>
                        </GridItem>
                        <GridItem span={6}>
                            <label className={"data__label"}>Executor Name</label>
                            <span>{executionData && executionData.executorName}</span>
                        </GridItem>
                        <GridItem span={6}>
                            <label className={"data__label"}>Date</label>
                            <span>{executionData && executionData.executionDate}</span>
                        </GridItem>
                    </Grid>
                )}
            </CardBody>
        </Card>
    );
};

export default ExecutionSummary;