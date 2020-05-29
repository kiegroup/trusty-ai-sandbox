import { Card, CardBody, CardHeader, Grid, GridItem, Title } from "@patternfly/react-core";
import React from "react";
import { IExecutionModelResponse } from "../Audit/types";
import FraudScoringDistribution from "../ModelLookup/FraudScoringDistribution";
import MortgageDistribution from "../ModelLookup/MortgageDistribution";

const models: Map<string, JSX.Element> = new Map([
  ["myMortgage", <MortgageDistribution />],
  ["fraud-scoring", <FraudScoringDistribution />],
]);

type ModelOverviewProps = {
  model: IExecutionModelResponse;
};

function ModelOverview(props: ModelOverviewProps) {
  return (
    <Card>
      <CardHeader>
        <Title headingLevel="h3" size="2xl">
          Model Lookup
        </Title>
      </CardHeader>
      <CardBody>
        <Grid gutter="md" className={"data"}>
          <GridItem span={6}>
            <label className={"data__label"}>Name</label>
            <span>{props.model.name}</span>
          </GridItem>
          <GridItem span={6}>
            <label className={"data__label"}>Version</label>
            <span>v5.0</span>
          </GridItem>
          <GridItem span={6}>
            <label className={"data__label"}>Authored on</label>
            <span>20/01/2020</span>
          </GridItem>
          <GridItem span={6}>
            <label className={"data__label"}>Released By</label>
            <span>Anouk Aimée</span>
          </GridItem>
          <GridItem span={6}>
            <label className={"data__label"}># of Decisions Produced</label>
            <span>1,523</span>
          </GridItem>
          <GridItem span={12}>
            <Title headingLevel="h5" size="lg">
              Decision Distribution (last 60 days)
            </Title>
            {models.get(props.model.name)}
          </GridItem>
        </Grid>
      </CardBody>
    </Card>
  );
}

export default ModelOverview;
