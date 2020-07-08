import React from "react";
import withBreadcrumbs, { BreadcrumbsProps } from "react-router-breadcrumbs-hoc";
import { Breadcrumb, BreadcrumbItem } from "@patternfly/react-core";

type TAuditParams = { id: string };

const AuditDetailBreadcrumb = (breadcrumb: BreadcrumbsProps<TAuditParams>) => {
  const { match } = breadcrumb;
  return <span style={{ textTransform: "uppercase" }}>ID #{match.params.id}</span>;
};

const routes = [
  { path: "/audit", breadcrumb: "Audit Investigation" },
  { path: "/audit/:executionType/:id/outcomes-details", breadcrumb: "Outcomes Details" },
  // the following route is needed to display a dedicated breadcrumb path for executions with only 1 outcome
  { path: "/audit/:executionType/:id/single-outcome", breadcrumb: "Outcome" },
  { path: "/audit/:executionType/:id/model-lookup", breadcrumb: "Model Lookup" },
  { path: "/audit/:executionType/:id/input-data", breadcrumb: "Input Data" },
  { path: "/audit/:executionType/:id", breadcrumb: AuditDetailBreadcrumb },
];
const excludePaths = ["/", "/audit/:executionType"];

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
