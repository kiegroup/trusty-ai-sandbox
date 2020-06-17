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
import { ExecutionType, getDecisionOutcome, getExecution } from "../../Shared/api/audit.api";
import { getModelDetail } from "../../Shared/api/audit.api";
import { IExecutionModelResponse } from "../types";
import { IExecution, IExecutionRouteParams } from "../types";
import { RemoteData } from "../../Shared/types";
import { IOutcome } from "../../Outcome/types";
//import DecisionDetail from "../../Execution/DecisionDetail";
import ExplanationView from "../../Explanation/ExplanationView/ExplanationView";
import InputDataView from "../../InputData/InputDataView/InputDataView";
import ModelLookup from "../../ModelLookup/ModelLookup";
import SkeletonInlineStripe from "../../Shared/skeletons/SkeletonInlineStripe";
import DecisionDetailAlt from "../../Execution/DecisionDetailAlt";
import ExecutionStatus from "../ExecutionStatus/ExecutionStatus";
import { UserIcon } from "@patternfly/react-icons";
import SkeletonStripes from "../../Shared/skeletons/SkeletonStripes";
import SkeletonCards from "../../Shared/skeletons/SkeletonCards/SkeletonCards";
import FormattedDate from "../../Shared/components/FormattedDate/FormattedDate";
import "./AuditDetail.scss";

const AuditDetail = () => {
  let { path, url } = useRouteMatch();
  let location = useLocation();
  const { executionId, executionType } = useParams<IExecutionRouteParams>();
  const [execution, setExecution] = useState<RemoteData<Error, IExecution>>({
    status: "NOT_ASKED",
  });
  const [outcome, setOutcome] = useState<RemoteData<Error, IOutcome[]>>({
    status: "NOT_ASKED",
  });
  const [thirdLevelNav, setThirdLevelNav] = useState<{ url: string; desc: string }[]>([]);
  const [model, setModel] = useState<IExecutionModelResponse>({
    name: "",
    namespace: "",
    type: "",
    model: "",
    serviceIdentifier: {},
  });

  useEffect(() => {
    let isMounted = true;
    setExecution({ status: "LOADING" });
    getExecution(executionType as ExecutionType, executionId)
      .then((response) => {
        if (isMounted) {
          setExecution({ status: "SUCCESS", data: response.data });
        }
      })
      .catch((error) => {
        setExecution({ status: "FAILURE", error: error });
      });
    return () => {
      isMounted = false;
    };
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
                {execution.status === "LOADING" && (
                  <SkeletonInlineStripe customStyle={{ height: "1.5em", verticalAlign: "baseline" }} />
                )}
                {execution.status === "SUCCESS" && (
                  <Title size="xl" headingLevel="h3">
                    <ExecutionStatus result={execution.data.executionSucceeded} />
                    <span> </span>
                    <FormattedDate date={execution.data.executionDate} preposition={true} />
                    {/*{formatDate(execution.data.executionDate)}*/}
                  </Title>
                )}
              </FlexItem>
              <FlexItem className="audit-detail__executor">
                {execution.status === "LOADING" && (
                  <SkeletonInlineStripe customStyle={{ height: "1.5em", verticalAlign: "baseline" }} />
                )}

                {execution.status === "SUCCESS" && (
                  <Title size="xl" headingLevel="h3">
                    <span>
                      <UserIcon className="audit-detail__executor__icon" />
                      Executed by {execution.data.executorName}
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
          <DecisionDetailAlt model={model} execution={execution} outcome={outcome} />
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
          )}
        </Route>
      </Switch>
    </>
  );
};

export default AuditDetail;
