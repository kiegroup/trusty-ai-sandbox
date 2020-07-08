import React, { useEffect, useState } from "react";
import {
  List,
  ListItem,
  ListVariant,
  PageSection,
  PageSectionVariants,
  Text,
  TextContent,
  Title,
  Toolbar,
  ToolbarContent,
  ToolbarItem,
} from "@patternfly/react-core";
import { Link } from "react-router-dom";
import "./AuditOverview.scss";
import PaginationContainer from "../PaginationContainer/PaginationContainer";
import SkeletonStripe from "../../Shared/skeletons/SkeletonStripe/SkeletonStripe";
import AuditToolbar from "../AuditToolbar/AuditToolbar";
import ExecutionTable from "../ExecutionTable/ExecutionTable";
import useExecutions from "./useExecutions";

const AuditOverview = () => {
  const oneMonthAgo = new Date();
  oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
  const [searchString, setSearchString] = useState("");
  const [latestSearches, setLatestSearches] = useState<string[] | null>(null);
  const [fromDate, setFromDate] = useState(oneMonthAgo.toISOString().substr(0, 10));
  const [toDate, setToDate] = useState(new Date().toISOString().substr(0, 10));
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [total, setTotal] = useState(0);
  const [loadExecutions, executions] = useExecutions(searchString, fromDate, toDate, pageSize, pageSize * (page - 1));

  useEffect(() => {
    if (executions.status === "SUCCESS") {
      setTotal(executions.data.total);
      // temporary solution: for demo purposes we display the first 3 executions here
      if (latestSearches === null) {
        let searches = [];
        let maxSearches = Math.min(3, executions.data.total);
        for (let i = 0; i < maxSearches; i++) {
          searches.push(executions.data.headers[i].executionId);
        }
        setLatestSearches(searches);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [executions]);

  return (
    <>
      <PageSection variant={PageSectionVariants.light}>
        <TextContent>
          <Title size="3xl" headingLevel="h2">
            Audit Investigation
          </Title>
          <Text component="p">Here you can retrieve all the available information about past cases</Text>
        </TextContent>
      </PageSection>
      <PageSection style={{ minHeight: "50em" }} isFilled={true}>
        <div style={{ marginBottom: "var(--pf-global--spacer--lg)" }}>
          <List variant={ListVariant.inline}>
            <ListItem>Last Opened:</ListItem>
            {latestSearches === null && <SkeletonStripe isInline={true} customStyle={{ height: "inherit" }} />}
            {latestSearches && latestSearches.length === 0 && (
              <span>
                <em>None</em>
              </span>
            )}
            {latestSearches &&
              latestSearches.length > 0 &&
              latestSearches.map((item, index) => {
                let latestSearchId;
                if (item.toString().indexOf("-") > -1) {
                  let splitted = item.split("-");
                  latestSearchId = splitted[splitted.length - 1];
                } else latestSearchId = item;
                return (
                  <ListItem key={`row-${index}`}>
                    <Link to={`/audit/decision/${item}`}>#{latestSearchId}</Link>
                  </ListItem>
                );
              })}
          </List>
        </div>
        <AuditToolbar
          setSearchString={setSearchString}
          fromDate={fromDate}
          setFromDate={setFromDate}
          toDate={toDate}
          setToDate={setToDate}
          total={total}
          pageSize={pageSize}
          page={page}
          setPage={setPage}
          setPageSize={setPageSize}
          onRefresh={loadExecutions}
        />

        <ExecutionTable data={executions} />

        <Toolbar id="audit-list-bottom-toolbar">
          <ToolbarContent>
            <ToolbarItem variant="pagination">
              <PaginationContainer
                total={total}
                page={page}
                pageSize={pageSize}
                onSetPage={setPage}
                onSetPageSize={setPageSize}
                paginationId="audit-list-bottom-pagination"
              />
            </ToolbarItem>
          </ToolbarContent>
        </Toolbar>
      </PageSection>
    </>
  );
};
export default AuditOverview;
