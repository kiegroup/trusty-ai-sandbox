import {
  Flex,
  FlexItem,
  Nav,
  NavItem,
  NavList,
  PageSection,
  PageSectionVariants,
  Stack,
  StackItem,
  TextContent,
  Title,
} from "@patternfly/react-core";
import React, { useEffect, useState } from "react";
import { Link, Redirect, Route, Switch, useLocation, useParams, useRouteMatch } from "react-router-dom";
import { IExecutionModelResponse } from "../types";
//import DecisionDetail from "../../Execution/DecisionDetail";
import ExplanationView from "../../Explanation/ExplanationView/ExplanationView";
import InputDataView from "../../InputData/InputDataView/InputDataView";
import ModelLookup from "../../ModelLookup/ModelLookup";
import { ExecutionType, getDecisionOutcome, getExecution } from "../../Shared/api/audit.api";
import SkeletonInlineStripe from "../../Shared/skeletons/SkeletonInlineStripe";
import { IExecution, IExecutionRouteParams } from "../types";
import { getModelDetail } from "../../Shared/api/audit.api";
import DecisionDetailAlt from "../../Execution/DecisionDetailAlt";
import ExecutionStatus from "../ExecutionStatus/ExecutionStatus";
import { UserIcon } from "@patternfly/react-icons";
import "./AuditDetail.scss";
import { RemoteData } from "../../Shared/types";
import { IOutcome } from "../../Outcome/types";
import SkeletonStripes from "../../Shared/skeletons/SkeletonStripes";
import SkeletonCards from "../../Shared/skeletons/SkeletonCards/SkeletonCards";

const AuditDetail = () => {
  let { path, url } = useRouteMatch();
  let location = useLocation();
  const { executionId, executionType } = useParams<IExecutionRouteParams>();
  const [executionData, setExecutionData] = useState<IExecution | null>(null);
  const [outcome, setOutcome] = useState<RemoteData<Error, IOutcome[]>>({
    status: "NOT_ASKED",
  });
  const [thirdLevelNav, setThirdLevelNav] = useState<{ url: string; desc: string }[]>([]);

  const formatDate = (date: string) => {
    const d = new Date(date);
    const y = d.getFullYear();
    const mm = d.getMonth() + 1;
    const dd = d.getDate();
    const h = d.getHours();
    const m = (d.getMinutes() < 10 ? "0" : "") + d.getMinutes();
    return `${mm}/${dd}/${y}, ${h}:${m}`;
  };

  const [model, setModel] = useState<IExecutionModelResponse>({
    name: "",
    namespace: "",
    type: "",
    model: "",
    serviceIdentifier: {},
  });

  useEffect(() => {
    getExecution(executionType as ExecutionType, executionId)
      .then((response) => {
        setExecutionData(response.data);
      })
      .catch(() => {});
  }, [executionType, executionId]);

  useEffect(() => {
    let didMount = true;
    getModelDetail(executionId)
      .then((response) => {
        if (didMount) {
          const model: IExecutionModelResponse = response.data as IExecutionModelResponse;
          setModel(model);
        }
      })
      .catch(() => {});
    return () => {
      didMount = false;
    };
  }, [executionId]);

  useEffect(() => {
    let isMounted = true;
    setOutcome({ status: "LOADING" });
    if (executionId) {
      getDecisionOutcome(executionId)
        .then((response) => {
          if (isMounted) {
            setOutcome({
              status: "SUCCESS",
              data: response.data.outcomes,
            });
          }
        })
        .catch((error) => {
          setOutcome({ status: "FAILURE", error: error });
        });
    }
    return () => {
      isMounted = false;
    };
  }, [executionId]);

  useEffect(() => {
    if (outcome.status === "SUCCESS") {
      const newNav = [];
      if (outcome.data.length === 1) {
        newNav.push({
          url: `/outcome-details/${outcome.data[0].outcomeId}`,
          desc: "Outcome Details",
        });
      } else {
        newNav.push({ url: "/outcomes", desc: "Outcomes" });
      }
      newNav.push({ url: "/input-data", desc: "Input Data" });
      newNav.push({ url: "/model-lookup", desc: "Model Lookup" });
      setThirdLevelNav(newNav);
    }
  }, [outcome]);

  return (
    <>
      <PageSection variant={PageSectionVariants.light}>
        <TextContent>
          <Flex>
            <Flex grow={{ default: "grow" }}>
              <FlexItem>
                <Title size="3xl" headingLevel="h2">
                  <span>Execution Detail</span>
                </Title>
              </FlexItem>
            </Flex>
            <Flex>
              <FlexItem className="audit-detail__execution-time">
                {executionData === null && (
                  <SkeletonInlineStripe customStyle={{ height: "1.5em", verticalAlign: "baseline" }} />
                )}
                {executionData !== null && (
                  <Title size="xl" headingLevel="h3">
                    <ExecutionStatus result={executionData.executionSucceeded} /> on{" "}
                    {formatDate(executionData.executionDate)}
                  </Title>
                )}
              </FlexItem>
              <FlexItem className="audit-detail__executor">
                {executionData === null && (
                  <SkeletonInlineStripe customStyle={{ height: "1.5em", verticalAlign: "baseline" }} />
                )}

                {executionData !== null && (
                  <Title size="xl" headingLevel="h3">
                    <span>
                      <UserIcon className="audit-detail__executor__icon" />
                      Executed by {executionData.executorName}
                    </span>
                  </Title>
                )}
              </FlexItem>
            </Flex>
          </Flex>
        </TextContent>

        {thirdLevelNav.length === 0 && (
          <div className="audit-detail__nav">
            <SkeletonStripes stripesNumber={3} stripesHeight={1.5} stripesWidth={120} isPadded={false} />
          </div>
        )}
        {thirdLevelNav.length > 0 && (
          <Nav className="audit-detail__nav" variant="tertiary">
            <NavList>
              {thirdLevelNav.map((item, index) => (
                <NavItem key={`sub-nav-${index}`} isActive={location.pathname === url + item.url}>
                  <Link to={url + item.url}>{item.desc}</Link>
                </NavItem>
              ))}
            </NavList>
          </Nav>
        )}
      </PageSection>

      <Switch>
        <Route path={`${path}/outcome-details/:outcomeId`}>
          <ExplanationView />
        </Route>

        <Route path={`${path}/outcomes/:outcomeId`}>
          <ExplanationView />
        </Route>
        <Route path={`${path}/outcomes`}>
          <DecisionDetailAlt model={model} executionData={executionData} />
        </Route>

        <Route path={`${path}/input-data`}>
          <InputDataView />
        </Route>
        <Route path={`${path}/model-lookup`}>
          <ModelLookup model={model} />
        </Route>
        <Route exact path={`${path}/`}>
          {outcome.status === "SUCCESS" && outcome.data.length === 1 && (
            <Redirect exact from={path} to={`${location.pathname}/outcome-details/${outcome.data[0].outcomeId}`} />
          )}
          {outcome.status === "SUCCESS" && outcome.data.length > 1 && (
            <Redirect exact from={path} to={`${location.pathname}/outcomes`} />
          )}
          {outcome.status === "LOADING" && (
            <>
              <PageSection>
                <Stack hasGutter>
                  <StackItem>
                    <SkeletonInlineStripe customStyle={{ height: "1.5em" }} />
                  </StackItem>
                  <StackItem>
                    <SkeletonCards quantity={2} />
                  </StackItem>
                </Stack>
              </PageSection>
            </>
          )}
        </Route>
      </Switch>
    </>
  );
};

export default AuditDetail;
