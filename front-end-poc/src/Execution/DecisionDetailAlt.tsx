import React from "react";
import {
  Button,
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Divider,
  Gallery,
  GalleryItem,
  Grid,
  GridItem,
  PageSection,
  Split,
  SplitItem,
  Stack,
  StackItem,
  Title,
} from "@patternfly/react-core";
import { IExecution, IExecutionModelResponse } from "../Audit/types";
import SkeletonGrid from "../Shared/skeletons/SkeletonGrid";
import { v4 as uuid } from "uuid";
import FormattedValue from "../Shared/components/FormattedValue/FormattedValue";
import "./DecisionDetailAlt.scss";
import EvaluationStatus from "../Explanation/EvaluationStatus/EvaluationStatus";
import MortgageDistribution from "../ModelLookup/MortgageDistribution";
import FraudScoringDistribution from "../ModelLookup/FraudScoringDistribution";
import ExecutionStatus from "../Audit/ExecutionStatus/ExecutionStatus";
import { LongArrowAltRightIcon } from "@patternfly/react-icons";
import { Link } from "react-router-dom";

const models: Map<string, JSX.Element> = new Map([
  ["myMortgage", <MortgageDistribution />],
  ["fraud-scoring", <FraudScoringDistribution />],
]);

const DecisionDetailAlt = (props: DecisionDetailAltProps) => {
  const { executionData, model } = props;

  return (
    <section className="decision-detail-view">
      <PageSection variant="default" className="decision-detail-view__section">
        <div className="container">
          <Title headingLevel="h2" size="3xl">
            Overview
          </Title>
        </div>
      </PageSection>
      <div className="container">
        <Divider />
      </div>
      <PageSection variant="default" className="decision-detail-view__section">
        <div className="container">
          <Stack gutter="md">
            <StackItem>
              <Title headingLevel="h3" size="2xl">
                Execution Info
              </Title>
            </StackItem>
            <StackItem>
              <Card>
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
                        <ExecutionStatus result={executionData.executionSucceeded} />
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
            </StackItem>
          </Stack>
        </div>
      </PageSection>
      <PageSection variant="default" className="decision-detail-view__section">
        <div className="container">
          <Stack gutter="md">
            <StackItem>
              <Title headingLevel="h3" size="2xl">
                Outcomes
              </Title>
            </StackItem>
            <StackItem>
              <Gallery gutter="md" className="outcome-cards" style={{ gridAutoRows: "1fr" }}>
                <GalleryItem key={"d"}>
                  <Card className="decision-detail-view__outcome-card" isHoverable>
                    <CardHeader>
                      <Title headingLevel="h4" size="xl">
                        <span>Risk score</span>{" "}
                        {/*<Label isCompact style={{ verticalAlign: "text-bottom", marginLeft: 15 }}>*/}
                        {/*  TOP LEVEL*/}
                        {/*</Label>*/}
                      </Title>
                    </CardHeader>
                    <CardBody>
                      <Split key={uuid()} className="outcome__property">
                        <SplitItem className="outcome__property__name" key="property-name">
                          Result:
                        </SplitItem>
                        <SplitItem className="outcome__property__value" key="property-value">
                          <FormattedValue value={0.333} />
                        </SplitItem>
                      </Split>
                    </CardBody>
                    <CardFooter>
                      <Split>
                        <SplitItem isFilled>
                          <EvaluationStatus status={"SUCCEEDED"} />
                        </SplitItem>
                        <SplitItem>
                          {/*<Button variant="link" isInline >*/}
                          {/*  View Explanation*/}
                          {/*</Button>*/}
                          <Link to={`explanation`} className="decision-detail-view__explanation-link">
                            View Explanation <LongArrowAltRightIcon />
                          </Link>
                        </SplitItem>
                      </Split>
                    </CardFooter>
                  </Card>
                </GalleryItem>
                <GalleryItem>
                  <Card className="decision-detail-view__outcome-card" isHoverable>
                    <CardHeader>
                      <Title headingLevel="h4" size="xl">
                        Total Amount from Last 24 hours Transactions
                      </Title>
                    </CardHeader>
                    <CardBody>
                      <Split key={uuid()} className="outcome__property">
                        <SplitItem className="outcome__property__name" key="property-name">
                          Result:
                        </SplitItem>
                        <SplitItem className="outcome__property__value" key="property-value">
                          <FormattedValue value={0} />
                        </SplitItem>
                      </Split>
                    </CardBody>
                    <CardFooter>
                      <Split>
                        <SplitItem isFilled>
                          <EvaluationStatus status={"SUCCEEDED"} />
                        </SplitItem>
                        <SplitItem>
                          <Button variant="link" isInline className="decision-detail-view__explanation-link">
                            View Explanation <LongArrowAltRightIcon />
                          </Button>
                        </SplitItem>
                      </Split>
                    </CardFooter>
                  </Card>
                </GalleryItem>
                <GalleryItem>
                  <Card className="decision-detail-view__outcome-card" isHoverable>
                    <CardHeader>
                      <Title headingLevel="h4" size="xl">
                        Last Transaction
                      </Title>
                    </CardHeader>
                    <CardBody>
                      <Split key={uuid()} className="outcome__property">
                        <SplitItem className="outcome__property__name" key="property-name">
                          Auth Code:
                        </SplitItem>
                        <SplitItem className="outcome__property__value" key="property-value">
                          <FormattedValue value="Authorized" />
                        </SplitItem>
                      </Split>
                      <Split key={uuid()} className="outcome__property">
                        <SplitItem className="outcome__property__name" key="property-name">
                          Amount:
                        </SplitItem>
                        <SplitItem className="outcome__property__value" key="property-value">
                          <FormattedValue value={1000} />
                        </SplitItem>
                      </Split>
                      <Split key={uuid()} className="outcome__property">
                        <SplitItem className="outcome__property__name" key="property-name">
                          Card type:
                        </SplitItem>
                        <SplitItem className="outcome__property__value" key="property-value">
                          <FormattedValue value="Debit" />
                        </SplitItem>
                      </Split>
                      <Split key={uuid()} className="outcome__property">
                        <SplitItem className="outcome__property__name" key="property-name">
                          Location:
                        </SplitItem>
                        <SplitItem className="outcome__property__value" key="property-value">
                          <FormattedValue value="Local" />
                        </SplitItem>
                      </Split>
                    </CardBody>
                    <CardFooter>
                      <Split>
                        <SplitItem isFilled>
                          <EvaluationStatus status={"SUCCEEDED"} />
                        </SplitItem>
                        <SplitItem>
                          <Button variant="link" isInline className="decision-detail-view__explanation-link">
                            View Explanation <LongArrowAltRightIcon />
                          </Button>
                        </SplitItem>
                      </Split>
                    </CardFooter>
                  </Card>
                </GalleryItem>
                <GalleryItem>
                  <Card className="decision-detail-view__outcome-card" isHoverable>
                    <CardHeader>
                      <Title headingLevel="h4" size="xl">
                        Merchant Deny List
                      </Title>
                    </CardHeader>
                    <CardBody>
                      <Split key={uuid()} className="outcome__property">
                        <SplitItem className="outcome__property__name" key="property-name">
                          Result:
                        </SplitItem>
                        <SplitItem className="outcome__property__value" key="property-value">
                          <span style={{ textTransform: "capitalize" }}>
                            illicicorp, slimshady, taintedthings, unscrupulous, unethicalbiz, wecorrupt, wickedstuff,
                            verybadthing
                          </span>
                        </SplitItem>
                      </Split>
                    </CardBody>
                    <CardFooter>
                      <Split>
                        <SplitItem isFilled>
                          <EvaluationStatus status={"SUCCEEDED"} />
                        </SplitItem>
                        <SplitItem>
                          <Button variant="link" isInline className="decision-detail-view__explanation-link">
                            View Explanation <LongArrowAltRightIcon />
                          </Button>
                        </SplitItem>
                      </Split>
                    </CardFooter>
                  </Card>
                </GalleryItem>
                <GalleryItem>
                  <Card className="decision-detail-view__outcome-card--skipped">
                    <CardHeader>
                      <Title headingLevel="h4" size="xl">
                        Bank Score
                      </Title>
                    </CardHeader>
                    <CardBody>
                      <em>No Result</em>
                    </CardBody>
                    <CardFooter>
                      <EvaluationStatus status={"SKIPPED"} />
                    </CardFooter>
                  </Card>
                </GalleryItem>
                <GalleryItem>
                  <Card className="decision-detail-view__outcome-card--skipped">
                    <CardHeader>
                      <Title headingLevel="h4" size="xl">
                        Credit history score
                      </Title>
                    </CardHeader>
                    <CardBody>
                      <Split key={uuid()} className="outcome__property">
                        <SplitItem className="outcome__property__name" key="property-name">
                          Error:
                        </SplitItem>
                        <SplitItem className="outcome__property__value" key="property-value">
                          <span style={{ textTransform: "capitalize" }}>Wrong values for credit balance</span>
                        </SplitItem>
                      </Split>
                    </CardBody>
                    <CardFooter>
                      <EvaluationStatus status={"FAILED"} />
                    </CardFooter>
                  </Card>
                </GalleryItem>
              </Gallery>
            </StackItem>
          </Stack>
        </div>
      </PageSection>
      <PageSection variant="default" className="decision-detail-view__section">
        <div className="container">
          <Stack gutter="md">
            <StackItem>
              <Title headingLevel="h3" size="2xl">
                Model Info
              </Title>
            </StackItem>
            <StackItem>
              <Grid gutter="md">
                <GridItem span={4}>
                  <Card>
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
                          <label className={"data__label"}># of Executions</label>
                          <span>1,523</span>
                        </GridItem>
                      </Grid>
                    </CardBody>
                  </Card>
                </GridItem>
                <GridItem span={8}>
                  <Card>
                    <CardBody>
                      <GridItem span={12}>
                        <Title headingLevel="h5" size="lg">
                          Decision Distribution (last 60 days)
                        </Title>
                        {models.get(props.model.name)}
                      </GridItem>
                    </CardBody>
                  </Card>
                </GridItem>
              </Grid>
            </StackItem>
          </Stack>
        </div>
      </PageSection>
      {/*
      <PageSection isFilled={true}>
        <Grid gutter="md">
          <GridItem span={6}>
            <Stack gutter={"md"}>
              <StackItem>
                <ExecutionSummary executionData={executionData} />
              </StackItem>
              <StackItem>
                <ModelOverview model={model} />
              </StackItem>
            </Stack>
          </GridItem>
          <GridItem span={6}>
            <Stack gutter="md">
              <StackItem>
                <Card>
                  <CardHeader>
                    <Title headingLevel="h3" size="2xl">
                      Decision Outcome
                    </Title>
                  </CardHeader>
                  <CardBody>
                    <OutcomeOverview />
                  </CardBody>
                </Card>
              </StackItem>
            </Stack>
          </GridItem>
        </Grid>
      </PageSection>
      */}
      )
    </section>
  );
};

type DecisionDetailAltProps = {
  executionData: IExecution | null;
  model: IExecutionModelResponse;
};

export default DecisionDetailAlt;
