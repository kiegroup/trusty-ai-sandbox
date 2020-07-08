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
import { getExecutions } from "../../Shared/api/audit.api";
import { IExecution } from "../types";
import "./AuditOverview.scss";
import PaginationContainer from "../PaginationContainer/PaginationContainer";
import SkeletonStripe from "../../Shared/skeletons/SkeletonStripe/SkeletonStripe";
import AuditToolbar from "../AuditToolbar/AuditToolbar";
import ExecutionTable from "../ExecutionTable/ExecutionTable";
import { RemoteData } from "../../Shared/types";

const AuditOverview = () => {
  const oneMonthAgo = new Date();
  oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
  const [data, setData] = useState<RemoteData<Error, IExecution[]>>({
    status: "NOT_ASKED",
  });
  const [searchString, setSearchString] = useState("");
  const [latestSearches, setLatestSearches] = useState<string[] | null>(null);
  const [fromDate, setFromDate] = useState(oneMonthAgo.toISOString().substr(0, 10));
  const [toDate, setToDate] = useState(new Date().toISOString().substr(0, 10));
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    let didMount = true;
    setData({ status: "LOADING" });
    getExecutions(searchString, fromDate, toDate, pageSize, pageSize * (page - 1))
      .then((response) => {
        if (didMount) {
          setData({
            status: "SUCCESS",
            data: response.data.headers,
          });
          setTotal(response.data.total);
          // temporary solution: for demo purposes we display the first 3 executions here
          if (latestSearches === null) {
            let searches = [];
            let maxSearches = Math.min(3, response.data.total);
            for (let i = 0; i < maxSearches; i++) {
              searches.push(response.data.headers[i].executionId);
            }
            setLatestSearches(searches);
          }
        }
      })
      .catch((error) => {
        setData({ status: "FAILURE", error: error });
      });
    return () => {
      didMount = false;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchString, fromDate, toDate, page, pageSize]);

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
        />

        <ExecutionTable data={data} />

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
