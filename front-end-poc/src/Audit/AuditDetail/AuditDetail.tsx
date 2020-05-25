import {
  Nav,
  NavItem,
  NavList,
  NavVariants,
  PageSection,
  PageSectionVariants,
  TextContent,
  Title,
} from "@patternfly/react-core";
import React, { useEffect, useState } from "react";
import { Link, Redirect, Route, Switch, useLocation, useParams, useRouteMatch } from "react-router-dom";
import { IExecutionModelResponse } from "../types";
import DecisionDetail from "../../Execution/DecisionDetail";
import ExplanationView from "../../Explanation/ExplanationView/ExplanationView";
import InputData from "../../InputData/InputData";
import ModelLookup from "../../ModelLookup/ModelLookup";
import { ExecutionType, getExecution } from "../../Shared/api/audit.api";
import SkeletonInlineStripe from "../../Shared/skeletons/SkeletonInlineStripe";
import { IExecution, IExecutionRouteParams } from "../types";
import { getModelDetail } from "../../Shared/api/audit.api";

const AuditDetail = () => {
  let { path, url } = useRouteMatch();
  let location = useLocation();
  let thirdLevelNav = [
    { url: "/overview", desc: "Overview" },
    { url: "/explanation", desc: "Explanation" },
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
          <Title size="4xl" headingLevel="h1" style={{ display: "flex", alignItems: "center" }}>
            {<span>Decision Detail —&nbsp;</span>}
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
        <Nav>
          <NavList variant={NavVariants.tertiary}>
            {thirdLevelNav.map((item, index) => (
              <NavItem key={`sub-nav-${index}`} isActive={location.pathname === url + item.url}>
                <Link to={url + item.url}>{item.desc}</Link>
              </NavItem>
            ))}
          </NavList>
        </Nav>
      </PageSection>
      <Switch>
        <Route path={`${path}/overview`}>
          <DecisionDetail model={model} executionData={executionData} />
        </Route>
        <Route path={`${path}/input-data`}>
          <InputData />
        </Route>
        <Route path={`${path}/explanation`}>
          <ExplanationView />
        </Route>
        <Route path={`${path}/model-lookup`}>
          <ModelLookup model={model} />
        </Route>
        <Redirect exact from={path} to={`${location.pathname}/overview`} />} />
      </Switch>
    </>
  );
};

export default AuditDetail;
