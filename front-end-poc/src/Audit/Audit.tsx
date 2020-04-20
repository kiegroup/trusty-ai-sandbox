import React, {useEffect, useRef, useState} from "react";
import {
    Button,
    ButtonVariant,
    InputGroup,
    List,
    ListItem,
    ListVariant,
    PageSection,
    PageSectionVariants,
    Text,
    TextContent,
    TextInput,
    Title
} from "@patternfly/react-core";
import {Link} from "react-router-dom";
import {IRow, Table, TableBody, TableHeader} from "@patternfly/react-table";
import SkeletonRows from "../Shared/skeletons/SkeletonRows";
import {getExecutions} from "../Shared/api/audit.api";
import {IExecution} from "./types";
import './Audit.scss';
import {
    DataToolbar,
    DataToolbarContent,
    DataToolbarItem,
    DataToolbarItemVariant
} from "@patternfly/react-core/dist/js/experimental";
import {SearchIcon} from "@patternfly/react-icons";
import FromFilter from "./FromFilter/FromFilter";
import ToFilter from "./ToFilter/ToFilter";
import PaginationContainer from "./PaginationContainer/PaginationContainer";
import NoExecutions from "./NoExecutions/NoExecutions";

const ExecutionStatus = (props:{result:boolean}) => {
    let className = "execution-status-badge execution-status-badge--";
    let status;
    if (props.result) {
        className += 'success';
        status = "Completed";
    } else {
        className += 'failure';
        status = "Failed";
    }
    return (
        <span className={className}>{status}</span>
    );
};

const prepareExecutionTableRows = (rowData: IExecution[]) => {
    let rows: IRow[] = [];

    rowData.forEach((item) => {
        let row: IRow = {};
        let cells = [];
        cells.push('#' + item.executionId);
        cells.push(item.executorName);
        cells.push(new Date(item.executionDate).toLocaleString());
        cells.push({
            title: <ExecutionStatus result={item.executionSucceeded}/>
        });
        cells.push({ title: <Link to={`/audit/${item.executionType.toLocaleLowerCase()}/${item.executionId}`}>View Detail</Link> });
        row.cells = cells;
        row.decisionKey = 'key-' + item.executionId;
        rows.push(row);
    });
    return rows;
};

const skeletonRows = SkeletonRows(5, 8, "decisionKey");
const noResults = NoExecutions(5);

const Audit = () => {
    const columns = ['ID', 'Executor', 'Date', 'Execution Status', ''];
    const [rows, setRows] = useState<IRow[]>(skeletonRows);
    const [searchString, setSearchString] = useState('');
    const [latestSearches] = useState(["1001", "1007", "1032"]);
    const [fromDate, setFromDate] =  useState("");
    const [toDate, setToDate] = useState("");
    const [page, setPage] = useState(1);
    const [pageSize, setPageSize] = useState(10);
    const [total, setTotal] = useState(0);
    const searchField = useRef<HTMLInputElement>(null);
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
        setRows(skeletonRows);
        getExecutions(searchString, fromDate, toDate, pageSize, pageSize * (page - 1))
            .then(response => {
                if (didMount) {
                    let tableRows = (response.data.headers.length)
                        ? prepareExecutionTableRows(response.data.headers)
                        : noResults;
                    setRows(tableRows);
                    setTotal(response.data.total);
                }
            })
            .catch(() => {});
        return () => {
            didMount = false;
        };
    }, [searchString, fromDate, toDate, page, pageSize]);

    return (
        <>
            <PageSection variant={PageSectionVariants.light}>
                <TextContent>
                    <Title size="4xl" headingLevel="h1">Audit Investigation</Title>
                    <Text component="p">
                        Here you can retrieve all the available information about past cases
                    </Text>
                </TextContent>
            </PageSection>
            <PageSection style={{minHeight: "50em"}} isFilled={true}>
                <div style={{marginBottom: 'var(--pf-global--spacer--lg)'}}>
                    <List variant={ListVariant.inline}>
                        <ListItem>Last Opened Decisions:</ListItem>
                        {latestSearches.map((item, index) => {
                            return <ListItem key={`row-${index}`}><Link to={`/audit/${item}`}>#{item}</Link></ListItem>
                        })}
                    </List>
                </div>
                <DataToolbar id="audit-list-top-toolbar" style={{marginBottom: 'var(--pf-global--spacer--lg)'}}>
                    <DataToolbarContent>
                        <DataToolbarItem variant="label">Search</DataToolbarItem>
                        <DataToolbarItem>
                            <InputGroup>
                                <TextInput name="search" ref={searchField} id="search" type="search" aria-label="search executions" onKeyDown={onSearchEnter}/>
                                <Button variant={ButtonVariant.control} aria-label="search button for search input" onClick={onSearchSubmit}>
                                    <SearchIcon />
                                </Button>
                            </InputGroup>
                        </DataToolbarItem>
                        <DataToolbarItem variant="label">From</DataToolbarItem>
                        <DataToolbarItem>
                            <FromFilter fromDate={fromDate} onFromDateUpdate={setFromDate} maxDate={toDate}/>
                        </DataToolbarItem>
                        <DataToolbarItem variant="label">To</DataToolbarItem>
                        <DataToolbarItem>
                            <ToFilter toDate={toDate} onToDateUpdate={setToDate} minDate={fromDate}/>
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

                <DataToolbar id="audit-list-bottom-toolbar" style={{marginTop: 'var(--pf-global--spacer--lg)'}}>
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
    )
};
export default Audit;