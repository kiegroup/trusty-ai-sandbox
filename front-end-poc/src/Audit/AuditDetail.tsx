import React, { useEffect, useState } from "react";
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
import { Switch, Route, Link, Redirect, useLocation, useParams, useRouteMatch } from "react-router-dom";
import AuditDetailOverview from "../AuditOverview/AuditDetailOverview";
import InputData from "../InputData/InputData";
import ExplanationView from "../Explanation/ExplanationView/ExplanationView";
import ModelLookup from "../ModelLookup/ModelLookup";
import { ExecutionType, getExecution } from "../Shared/api/audit.api";
import { IExecution, IExecutionRouteParams } from "./types";
import SkeletonInlineStripe from "../Shared/skeletons/SkeletonInlineStripe";

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

  useEffect(() => {
    getExecution(executionType as ExecutionType, executionId)
      .then((response) => {
        setExecutionData(response.data);
      })
      .catch(() => {});
  }, [executionType, executionId]);

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
          <AuditDetailOverview />
        </Route>
        <Route path={`${path}/input-data`}>
          <InputData />
        </Route>
        <Route path={`${path}/explanation`}>
          <ExplanationView />
        </Route>
        <Route path={`${path}/model-lookup`}>
          <ModelLookup />
        </Route>
        <Route render={() => <Redirect to={`${location.pathname}/overview`} />} />
      </Switch>
    </>
  );
};

export default AuditDetail;
