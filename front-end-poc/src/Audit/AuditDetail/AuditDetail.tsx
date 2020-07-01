import React, { useEffect, useState } from "react";
import { Nav, NavItem, NavList, PageSection, PageSectionVariants, Stack, StackItem } from "@patternfly/react-core";
import { Link, Redirect, Route, Switch, useLocation, useParams, useRouteMatch } from "react-router-dom";
import { ExecutionType, getDecisionOutcome, getModelDetail } from "../../Shared/api/audit.api";
import { IExecutionModelResponse, IExecutionRouteParams } from "../types";
import { RemoteData } from "../../Shared/types";
import { IOutcome } from "../../Outcome/types";
import ExplanationView from "../../Explanation/ExplanationView/ExplanationView";
import InputDataView from "../../InputData/InputDataView/InputDataView";
import ModelLookup from "../../ModelLookup/ModelLookup";
import SkeletonInlineStripe from "../../Shared/skeletons/SkeletonInlineStripe";
import SkeletonStripes from "../../Shared/skeletons/SkeletonStripes";
import SkeletonCards from "../../Shared/skeletons/SkeletonCards/SkeletonCards";
import useExecutionInfo from "../../Shared/hooks/useExecutionInfo";
import ExecutionHeader from "../ExecutionHeader/ExecutionHeader";
import ExecutionDetail from "../../Execution/ExecutionDetail/ExecutionDetail";
import "./AuditDetail.scss";

const AuditDetail = () => {
  const { path, url } = useRouteMatch();
  const location = useLocation();
  const { executionId, executionType } = useParams<IExecutionRouteParams>();
  const execution = useExecutionInfo(executionType as ExecutionType, executionId);
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
          url: `/single-outcome`,
          desc: "Outcome",
        });
      } else {
        newNav.push({ url: "/overview", desc: "Overview" });
        newNav.push({ url: "/outcomes-details", desc: "Outcomes Details" });
      }
      newNav.push({ url: "/input-data", desc: "Input Data" });
      newNav.push({ url: "/model-lookup", desc: "Model Lookup" });
      setThirdLevelNav(newNav);
    }
  }, [outcome]);

  return (
    <>
      <PageSection variant={PageSectionVariants.light}>
        <ExecutionHeader execution={execution} />

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
        <Route path={`${path}/single-outcome`}>
          <ExplanationView />
        </Route>
        <Route path={`${path}/overview`}>
          <ExecutionDetail model={model} execution={execution} outcome={outcome} />
        </Route>
        <Route path={`${path}/outcomes-details`}>
          <ExplanationView />
        </Route>
        <Route path={`${path}/input-data`}>
          <InputDataView />
        </Route>
        <Route path={`${path}/model-lookup`}>
          <ModelLookup model={model} />
        </Route>
        <Route exact path={`${path}/`}>
          {outcome.status === "SUCCESS" && outcome.data.length === 1 && (
            <Redirect
              exact
              from={path}
              to={`${location.pathname}/single-outcome?outcomeId=${outcome.data[0].outcomeId}`}
            />
          )}
          {outcome.status === "SUCCESS" && outcome.data.length > 1 && (
            <Redirect exact from={path} to={`${location.pathname}/overview`} />
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
