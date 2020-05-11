import React from "react";
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

const AuditDetail = () => {
  let { path, url } = useRouteMatch();
  let location = useLocation();
  let thirdLevelNav = [
    { url: "/overview", desc: "Overview" },
    { url: "/explanation", desc: "Explanation" },
    { url: "/input-data", desc: "Input Data" },
    { url: "/model-lookup", desc: "Model Lookup" },
  ];
  const { executionId } = useParams();
  return (
    <>
      <PageSection variant={PageSectionVariants.light}>
        <TextContent>
          <Title size="4xl" headingLevel="h1">
            ID #{executionId} - Decision Detail
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
