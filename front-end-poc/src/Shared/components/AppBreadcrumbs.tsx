import React from "react";
import withBreadcrumbs from "react-router-breadcrumbs-hoc";
import { Breadcrumb, BreadcrumbItem } from "@patternfly/react-core";

// todo: move routes config somewhere and consolidate with routes declaration
const routes = [
  { path: "/", breadcrumb: "Monitoring" },
  { path: "/dashboard", breadcrumb: "Domain Monitoring Dashboard" },
  { path: "/op-dashboard", breadcrumb: "Operational Monitoring Dashboard" },
  { path: "/audit", breadcrumb: "Audit Investigation" },
  { path: "/audit/:executionType/:id/outcomes", breadcrumb: "Outcome" },
  { path: "/audit/:executionType/:id/outcomes/:outcomeId", breadcrumb: "Outcome Details" },
  // the following route is needed to display a dedicated breadcrumb path for executions with only 1 outcome
  { path: "/audit/:executionType/:id/outcome-details/:outcomeId", breadcrumb: "Outcome Details" },
  { path: "/audit/:executionType/:id/model-lookup", breadcrumb: "Model Lookup" },
  { path: "/audit/:executionType/:id/input-data", breadcrumb: "Input Data" },
  { path: "/audit/:executionType/:id", breadcrumb: "Execution Detail" },
];
const excludePaths = ["/audit/:executionType", "/audit/:executionType/:id/outcome-details/"];

const BreadcrumbList = withBreadcrumbs(routes, { excludePaths })(({ breadcrumbs }) => {
  return (
    <Breadcrumb>
      {breadcrumbs.map(({ match, location, breadcrumb }) => {
        return (
          <BreadcrumbItem to={`#${match.url}`} key={match.url} isActive={location.pathname === match.url}>
            {breadcrumb}
          </BreadcrumbItem>
        );
      })}
    </Breadcrumb>
  );
});

const AppBreadcrumbs = <BreadcrumbList />;

export default AppBreadcrumbs;
