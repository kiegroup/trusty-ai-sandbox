import React, { useEffect, useMemo, useRef, useState } from "react";
import {
  Bullseye,
  Button,
  ButtonVariant,
  EmptyState,
  EmptyStateBody,
  EmptyStateIcon,
  InputGroup,
  List,
  ListItem,
  ListVariant,
  PageSection,
  PageSectionVariants,
  Text,
  TextContent,
  TextInput,
  Title,
} from "@patternfly/react-core";
import { Link } from "react-router-dom";
import { IRow, Table, TableBody, TableHeader } from "@patternfly/react-table";
import SkeletonRows from "../../Shared/skeletons/SkeletonRows";
import { getExecutions } from "../../Shared/api/audit.api";
import { IExecution } from "../types";
import "./AuditOverview.scss";
import {
  DataToolbar,
  DataToolbarContent,
  DataToolbarItem,
  DataToolbarItemVariant,
} from "@patternfly/react-core/dist/js/experimental";
import { SearchIcon } from "@patternfly/react-icons";
import FromFilter from "../FromFilter/FromFilter";
import ToFilter from "../ToFilter/ToFilter";
import PaginationContainer from "../PaginationContainer/PaginationContainer";
import SkeletonInlineStripe from "../../Shared/skeletons/SkeletonInlineStripe";
import ExecutionStatus from "../ExecutionStatus/ExecutionStatus";

const prepareExecutionTableRows = (rowData: IExecution[]) => {
  let rows: IRow[] = [];

  rowData.forEach((item) => {
    let row: IRow = {};
    let cells = [];
    cells.push({
      title: (
        <Link to={`/audit/${item.executionType.toLocaleLowerCase()}/${item.executionId}`}>
          {"#" + item.executionId}
        </Link>
      ),
    });
    cells.push(item.executedModelName);
    cells.push(item.executorName);
    cells.push(new Date(item.executionDate).toLocaleString());
    cells.push({
      title: <ExecutionStatus result={item.executionSucceeded} />,
    });
    row.cells = cells;
    row.decisionKey = "key-" + item.executionId;
    rows.push(row);
  });
  return rows;
};

const noExecutions = (colSpan: number) => {
  return [
    {
      heightAuto: true,
      decisionKey: "no-results",
      cells: [
        {
          props: { colSpan },
          title: (
            <Bullseye>
              <EmptyState>
                <EmptyStateIcon icon={SearchIcon} />
                <Title size="lg">No executions found</Title>
                <EmptyStateBody>No results match the filter criteria. Try removing all filters.</EmptyStateBody>
              </EmptyState>
            </Bullseye>
          ),
        },
      ],
    },
  ];
};

const AuditOverview = () => {
  const columns = ["ID", "Description", "Executor", "Date", "Execution Status"];
  const oneMonthAgo = new Date();
  oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
  const [rows, setRows] = useState<IRow[]>([]);
  const [searchString, setSearchString] = useState("");
  const [latestSearches, setLatestSearches] = useState<string[] | null>(null);
  const [fromDate, setFromDate] = useState(oneMonthAgo.toISOString().substr(0, 10));
  const [toDate, setToDate] = useState(new Date().toISOString().substr(0, 10));
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [total, setTotal] = useState(0);
  const searchField = useRef<HTMLInputElement>(null);

  const skeletons = useMemo(() => {
    return SkeletonRows(5, 8, "decisionKey");
  }, []);

  const noResults = useMemo(() => {
    return noExecutions(5);
  }, []);

  const onSearchSubmit = (): void => {
    if (searchField && searchField.current) setSearchString(searchField.current.value);
  };
  const onSearchEnter = (event: React.KeyboardEvent): void => {
    if (searchField && searchField.current && event.key === "Enter") {
      setSearchString(searchField.current.value);
    }
  };
  useEffect(() => {
    let didMount = true;
    setRows(skeletons);
    getExecutions(searchString, fromDate, toDate, pageSize, pageSize * (page - 1))
      .then((response) => {
        if (didMount) {
          let tableRows = response.data.headers.length ? prepareExecutionTableRows(response.data.headers) : noResults;
          setRows(tableRows);
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
      .catch(() => {});
    return () => {
      didMount = false;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [searchString, fromDate, toDate, page, pageSize, skeletons, noResults]);

  return (
    <>
      <PageSection variant={PageSectionVariants.light}>
        <TextContent>
          <Title size="4xl" headingLevel="h1">
            Audit Investigation
          </Title>
          <Text component="p">Here you can retrieve all the available information about past cases</Text>
        </TextContent>
      </PageSection>
      <PageSection style={{ minHeight: "50em" }} isFilled={true}>
        <div style={{ marginBottom: "var(--pf-global--spacer--lg)" }}>
          <List variant={ListVariant.inline}>
            <ListItem>Last Opened:</ListItem>
            {latestSearches === null && <SkeletonInlineStripe customStyle={{ height: "inherit" }} />}
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
        <DataToolbar id="audit-list-top-toolbar" style={{ marginBottom: "var(--pf-global--spacer--lg)" }}>
          <DataToolbarContent>
            <DataToolbarItem variant="label">Search</DataToolbarItem>
            <DataToolbarItem>
              <InputGroup>
                <TextInput
                  name="search"
                  ref={searchField}
                  id="search"
                  type="search"
                  aria-label="search executions"
                  onKeyDown={onSearchEnter}
                />
                <Button
                  variant={ButtonVariant.control}
                  aria-label="search button for search input"
                  onClick={onSearchSubmit}>
                  <SearchIcon />
                </Button>
              </InputGroup>
            </DataToolbarItem>
            <DataToolbarItem variant="label">From</DataToolbarItem>
            <DataToolbarItem>
              <FromFilter fromDate={fromDate} onFromDateUpdate={setFromDate} maxDate={toDate} />
            </DataToolbarItem>
            <DataToolbarItem variant="label">To</DataToolbarItem>
            <DataToolbarItem>
              <ToFilter toDate={toDate} onToDateUpdate={setToDate} minDate={fromDate} />
            </DataToolbarItem>
            <DataToolbarItem variant={DataToolbarItemVariant.pagination}>
              <PaginationContainer
                total={total}
                page={page}
                pageSize={pageSize}
                onSetPage={setPage}
                onSetPageSize={setPageSize}
                paginationId="audit-list-top-pagination"
              />
            </DataToolbarItem>
          </DataToolbarContent>
        </DataToolbar>

        <Table cells={columns} rows={rows} aria-label="Executions list">
          <TableHeader />
          <TableBody rowKey="decisionKey" />
        </Table>

        <DataToolbar id="audit-list-bottom-toolbar" style={{ marginTop: "var(--pf-global--spacer--lg)" }}>
          <DataToolbarContent>
            <DataToolbarItem variant={DataToolbarItemVariant.pagination}>
              <PaginationContainer
                total={total}
                page={page}
                pageSize={pageSize}
                onSetPage={setPage}
                onSetPageSize={setPageSize}
                paginationId="audit-list-bottom-pagination"
              />
            </DataToolbarItem>
          </DataToolbarContent>
        </DataToolbar>
      </PageSection>
    </>
  );
};
export default AuditOverview;
