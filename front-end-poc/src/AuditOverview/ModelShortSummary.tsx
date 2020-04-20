import React from "react";
import {Card, CardBody, CardHeader, Title} from "@patternfly/react-core";

const ModelShortSummary = () => {
    return (
        <Card style={{height: "100%"}}>
            <CardHeader>
                <Title headingLevel="h3" size="2xl">
                    Model Lookup
                </Title>
            </CardHeader>
            <CardBody className="decision">
                <span>Mortgage Model v5.0</span>
            </CardBody>
        </Card>
    )
};

export default ModelShortSummary;