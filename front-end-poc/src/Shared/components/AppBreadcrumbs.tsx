import React from "react";
import withBreadcrumbs from "react-router-breadcrumbs-hoc";
import { Breadcrumb, BreadcrumbItem } from "@patternfly/react-core";

const routes = [
  { path: "/audit", breadcrumb: "Audit Investigation" },
  { path: "/audit/:executionType/:id/outcome-details", breadcrumb: "Outcome Details" },
  // the following route is needed to display a dedicated breadcrumb path for executions with only 1 outcome
  { path: "/audit/:executionType/:id/single-outcome", breadcrumb: "Outcome" },
  { path: "/audit/:executionType/:id/model-lookup", breadcrumb: "Model Lookup" },
  { path: "/audit/:executionType/:id/input-data", breadcrumb: "Input Data" },
  { path: "/audit/:executionType/:id", breadcrumb: "Execution" },
];
const excludePaths = ["/", "/audit/:executionType", "/audit/:executionType/:id/outcome-details/"];

const BreadcrumbList = withBreadcrumbs(routes, { excludePaths })(({ breadcrumbs }) => {
  // hide breadcrumbs if there is 1 or 0 items to display
  // because there's no navigation tree
  if (breadcrumbs.length < 2) {
    return <></>;
  }
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
