import React from "react";
import { Link } from "react-router-dom";
import { IRow, Table, TableBody, TableHeader } from "@patternfly/react-table";
import { Bullseye, EmptyState, EmptyStateBody, EmptyStateIcon, Title } from "@patternfly/react-core";
import { SearchIcon } from "@patternfly/react-icons";
import ExecutionStatus from "../ExecutionStatus/ExecutionStatus";
import FormattedDate from "../../Shared/components/FormattedDate/FormattedDate";
import skeletonRows from "../../Shared/skeletons/skeletonRows";
import { RemoteData } from "../../Shared/types";
import { IExecution } from "../types";

type ExecutionTableProps = {
  data: RemoteData<Error, IExecution[]>;
};

const prepareExecutionTableRows = (rowData: IExecution[]) => {
  let rows: IRow[] = [];

  rowData.forEach((item) => {
    let row: IRow = {};
    let cells = [];
    cells.push({
      title: (
        <Link to={`/audit/${item.executionType.toLocaleLowerCase()}/${item.executionId}`}>
          {"#" + item.executionId.toUpperCase()}
        </Link>
      ),
    });
    cells.push(item.executedModelName);
    cells.push(item.executorName);
    cells.push({
      title: <FormattedDate date={item.executionDate} />,
    });
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
                <Title headingLevel="h5" size="lg">
                  No executions found
                </Title>
                <EmptyStateBody>No results match the filter criteria. Try removing all filters.</EmptyStateBody>
              </EmptyState>
            </Bullseye>
          ),
        },
      ],
    },
  ];
};

const ExecutionTable = (props: ExecutionTableProps) => {
  const { data } = props;
  const columns = ["ID", "Description", "Executor", "Date", "Execution Status"];

  return (
    <>
      {(data.status === "LOADING" || data.status === "NOT_ASKED") && (
        <Table cells={columns} rows={skeletonRows(columns.length, 8, "decisionKey")} aria-label="Executions list">
          <TableHeader />
          <TableBody rowKey="decisionKey" />
        </Table>
      )}
      {data.status === "SUCCESS" && data.data.length > 0 && (
        <Table cells={columns} rows={prepareExecutionTableRows(data.data)} aria-label="Executions list">
          <TableHeader />
          <TableBody rowKey="decisionKey" />
        </Table>
      )}
      {data.status === "SUCCESS" && data.data.length === 0 && (
        <Table cells={columns} rows={noExecutions(columns.length)} aria-label="Executions list">
          <TableHeader />
          <TableBody rowKey="decisionKey" />
        </Table>
      )}
      {data.status === "FAILURE" && <span>error</span>}
    </>
  );
};

export default ExecutionTable;
