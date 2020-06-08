import React, { useState } from "react";
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
  InputGroup,
  Label,
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
  const [demo, setDemo] = useState(1);
  return (
    <section className="decision-detail-view">
      <PageSection variant="default" className="decision-detail-view__section">
        <div className="container">
          <Title headingLevel="h2" size="3xl">
            Overview
          </Title>
          <div className="demo-switch">
            <InputGroup>
              <Button variant="control" onClick={() => setDemo(1)} isActive={demo === 1}>
                Demo 1
              </Button>
              <Button variant="control" onClick={() => setDemo(2)} isActive={demo === 2}>
                Demo 2
              </Button>
              <Button variant="control" onClick={() => setDemo(3)} isActive={demo === 3}>
                Demo 3
              </Button>
              <Button variant="control" onClick={() => setDemo(4)} isActive={demo === 4}>
                Demo 4
              </Button>
            </InputGroup>
          </div>
        </div>
      </PageSection>
      <div className="container">
        <Divider />
      </div>
      {demo === 1 && (
        <>
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
                      {executionData === null && <SkeletonGrid rowsNumber={1} colsNumber={4} gutterSize="md" />}
                      {executionData !== null && (
                        <Grid gutter="md" className={"data"}>
                          <GridItem span={4}>
                            <label className={"data__label"}>Execution ID</label>
                            <span>{executionData.executionId}</span>
                          </GridItem>
                          <GridItem span={2}>
                            <label className={"data__label"}>Execution Status</label>
                            <ExecutionStatus result={executionData.executionSucceeded} />
                          </GridItem>
                          <GridItem span={2}>
                            <label className={"data__label"}>Executor Name</label>
                            <span>{executionData.executorName}</span>
                          </GridItem>
                          <GridItem span={2}>
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
                            Total Amount for Last 24h Transactions
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
                                illicicorp, slimshady, taintedthings, unscrupulous, unethicalbiz, wecorrupt,
                                wickedstuff, verybadthing
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
        </>
      )}
      {demo === 2 && (
        <>
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
                            <span>Mortgage approval</span>{" "}
                            {/*<Label isCompact style={{ verticalAlign: "text-bottom", marginLeft: 15 }}>*/}
                            {/*  TOP LEVEL*/}
                            {/*</Label>*/}
                          </Title>
                        </CardHeader>
                        <CardBody>
                          <div className="decision-detail-view__big-outcome">True</div>
                        </CardBody>
                        <CardFooter>
                          <Split>
                            <SplitItem isFilled>
                              <EvaluationStatus status={"SUCCEEDED"} />
                            </SplitItem>
                            <SplitItem>
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
                            Risk Score
                          </Title>
                        </CardHeader>
                        <CardBody>
                          <div className="decision-detail-view__big-outcome">21.7031851958099</div>
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
                  </Gallery>
                </StackItem>
              </Stack>
            </div>
          </PageSection>
        </>
      )}
      {demo === 3 && (
        <>
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
                            <span>Risk score</span>
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
                    <GalleryItem style={{ gridRow: "span 2" }}>
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
                              <FormattedValue value="Debit and some other words causing the property to spread to two rows" />
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
                                illicicorp, slimshady, taintedthings, unscrupulous, unethicalbiz, wecorrupt,
                                wickedstuff, verybadthing
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
        </>
      )}
      {demo === 4 && (
        <>
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
                            {/*<span>Product Recommendation</span>*/}
                            <Label>Recommended Loan Products</Label>
                          </Title>
                        </CardHeader>
                        <CardBody>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Product Name:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"Lender B - ARM5/1-Standard"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Note Amount:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$273,775.90"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Interest Rate:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"3.75"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Monthly Payment:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$1,267.90"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Cash to Close:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$75,475.52"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Required Credit Score:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"720"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Recommendation:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"Good"} />
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
                    <GalleryItem key={"d"}>
                      <Card className="decision-detail-view__outcome-card" isHoverable>
                        <CardHeader>
                          <Title headingLevel="h4" size="xl">
                            {/*<span>Product Recommendation</span>*/}
                            <Label>Recommended Loan Products</Label>
                          </Title>
                        </CardHeader>
                        <CardBody>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Product Name:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"Lender B - ARM5/1-Standard"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Note Amount:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$273,775.90"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Interest Rate:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"3.75"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Monthly Payment:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$1,267.90"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Cash to Close:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$75,475.52"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Required Credit Score:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"720"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Recommendation:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"Good"} />
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
                    <GalleryItem key={"d"}>
                      <Card className="decision-detail-view__outcome-card" isHoverable>
                        <CardHeader>
                          <Title headingLevel="h4" size="xl">
                            {/*<span>Product Recommendation</span>*/}
                            <Label>Recommended Loan Products</Label>
                          </Title>
                        </CardHeader>
                        <CardBody>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Product Name:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"Lender B - ARM5/1-Standard"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Note Amount:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$273,775.90"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Interest Rate:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"3.75"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Monthly Payment:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$1,267.90"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Cash to Close:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$75,475.52"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Required Credit Score:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"720"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Recommendation:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"Good"} />
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
                    <GalleryItem key={"d"}>
                      <Card className="decision-detail-view__outcome-card" isHoverable>
                        <CardHeader>
                          <Title headingLevel="h4" size="xl">
                            {/*<span>Product Recommendation</span>*/}
                            <Label>Recommended Loan Products</Label>
                          </Title>
                        </CardHeader>
                        <CardBody>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Product Name:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"Lender B - ARM5/1-Standard"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Note Amount:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$273,775.90"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Interest Rate:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"3.75"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Monthly Payment:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$1,267.90"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Cash to Close:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"$75,475.52"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Required Credit Score:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"720"} />
                            </SplitItem>
                          </Split>
                          <Split key={uuid()} className="outcome__property">
                            <SplitItem className="outcome__property__name" key="property-name">
                              Recommendation:
                            </SplitItem>
                            <SplitItem className="outcome__property__value" key="property-value">
                              <FormattedValue value={"Good"} />
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
                    <GalleryItem key={"d"}>
                      <Card className="decision-detail-view__outcome-card" isHoverable>
                        <CardHeader>
                          <Title headingLevel="h4" size="xl">
                            <span>Credit score</span>{" "}
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
                              <FormattedValue value={800} />
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
                  </Gallery>
                </StackItem>
              </Stack>
            </div>
          </PageSection>
        </>
      )}
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
                  <Card style={{ height: "100%" }}>
                    <CardBody>
                      <Grid gutter="md" className={"data"}>
                        <GridItem span={6}>
                          <label className={"data__label"}>Name</label>
                          <span>{model.name}</span>
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
                        {models.get(model.name)}
                      </GridItem>
                    </CardBody>
                  </Card>
                </GridItem>
              </Grid>
            </StackItem>
          </Stack>
        </div>
      </PageSection>
    </section>
  );
};

type DecisionDetailAltProps = {
  executionData: IExecution | null;
  model: IExecutionModelResponse;
};

export default DecisionDetailAlt;
