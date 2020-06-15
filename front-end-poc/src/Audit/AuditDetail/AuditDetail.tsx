import { Nav, NavItem, NavList, PageSection, PageSectionVariants, TextContent, Title } from "@patternfly/react-core";
import React, { useEffect, useState } from "react";
import { Link, Redirect, Route, Switch, useLocation, useParams, useRouteMatch } from "react-router-dom";
import { IExecutionModelResponse } from "../types";
//import DecisionDetail from "../../Execution/DecisionDetail";
import ExplanationView from "../../Explanation/ExplanationView/ExplanationView";
import InputDataView from "../../InputData/InputDataView/InputDataView";
import ModelLookup from "../../ModelLookup/ModelLookup";
import { ExecutionType, getExecution } from "../../Shared/api/audit.api";
import SkeletonInlineStripe from "../../Shared/skeletons/SkeletonInlineStripe";
import { IExecution, IExecutionRouteParams } from "../types";
import { getModelDetail } from "../../Shared/api/audit.api";
import "./AuditDetail.scss";
import DecisionDetailAlt from "../../Execution/DecisionDetailAlt";

const AuditDetail = () => {
  let { path, url } = useRouteMatch();
  let location = useLocation();
  let thirdLevelNav = [
    { url: "/outcomes", desc: "Outcomes" },
    { url: "/input-data", desc: "Input Data" },
    { url: "/model-lookup", desc: "Model Lookup" },
  ];
  const { executionId, executionType } = useParams<IExecutionRouteParams>();
  const [executionData, setExecutionData] = useState<IExecution | null>(null);

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

  return (
    <>
      <PageSection variant={PageSectionVariants.light}>
        <TextContent>
          <Title size="3xl" headingLevel="h2">
            {<span>Execution Detail —&nbsp;</span>}
            {executionData === null && (
              <>
                <SkeletonInlineStripe customStyle={{ height: "0.9em", verticalAlign: "baseline" }} />
                <SkeletonInlineStripe customStyle={{ height: "0.9em", verticalAlign: "baseline" }} />
              </>
            )}
            {executionData !== null && (
              <span>
                {formatDate(executionData.executionDate)} by {executionData.executorName}
              </span>
            )}
          </Title>
        </TextContent>
        <Nav className="audit-detail__nav" variant="tertiary">
          <NavList>
            {thirdLevelNav.map((item, index) => (
              <NavItem key={`sub-nav-${index}`} isActive={location.pathname === url + item.url}>
                <Link to={url + item.url}>{item.desc}</Link>
              </NavItem>
            ))}
          </NavList>
        </Nav>
      </PageSection>
      <Switch>
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
        <Redirect exact from={path} to={`${location.pathname}/outcomes`} />} />
      </Switch>
    </>
  );
};

export default AuditDetail;
