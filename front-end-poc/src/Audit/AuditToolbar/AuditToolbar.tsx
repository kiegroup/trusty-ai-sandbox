import React, { useRef } from "react";
import {
  Button,
  ButtonVariant,
  InputGroup,
  TextInput,
  Toolbar,
  ToolbarContent,
  ToolbarItem,
  ToolbarItemVariant,
} from "@patternfly/react-core";
import { SearchIcon, SyncIcon } from "@patternfly/react-icons";
import FromFilter from "../FromFilter/FromFilter";
import ToFilter from "../ToFilter/ToFilter";
import PaginationContainer from "../PaginationContainer/PaginationContainer";

type AuditToolbarProps = {
  setSearchString: (searchString: string) => void;
  fromDate: string;
  setFromDate: (fromDate: string) => void;
  toDate: string;
  setToDate: (toDate: string) => void;
  total: number;
  pageSize: number;
  page: number;
  setPage: (page: number) => void;
  setPageSize: (pageSize: number) => void;
  onRefresh: () => void;
};

export const AuditToolbarTop = (props: AuditToolbarProps) => {
  const {
    setSearchString,
    fromDate,
    setFromDate,
    toDate,
    setToDate,
    total,
    pageSize,
    page,
    setPage,
    setPageSize,
    onRefresh,
  } = props;

  const searchField = useRef<HTMLInputElement>(null);
  const onSearchSubmit = (): void => {
    if (searchField && searchField.current) setSearchString(searchField.current.value);
  };
  const onSearchEnter = (event: React.KeyboardEvent): void => {
    if (searchField && searchField.current && event.key === "Enter") {
      setSearchString(searchField.current.value);
    }
  };
  return (
    <Toolbar id="audit-overview-top-toolbar">
      <ToolbarContent>
        <ToolbarItem variant="label">Search</ToolbarItem>
        <ToolbarItem>
          <InputGroup>
            <TextInput
              name="audit-search-input"
              ref={searchField}
              id="audit-search-input"
              type="search"
              aria-label="search executions"
              onKeyDown={onSearchEnter}
            />
            <Button
              id="audit-search"
              variant={ButtonVariant.control}
              aria-label="search button for search input"
              onClick={onSearchSubmit}>
              <SearchIcon />
            </Button>
          </InputGroup>
        </ToolbarItem>
        <ToolbarItem variant="label">From</ToolbarItem>
        <ToolbarItem>
          <FromFilter fromDate={fromDate} onFromDateUpdate={setFromDate} maxDate={toDate} />
        </ToolbarItem>
        <ToolbarItem variant="label">To</ToolbarItem>
        <ToolbarItem>
          <ToFilter toDate={toDate} onToDateUpdate={setToDate} minDate={fromDate} />
        </ToolbarItem>
        <ToolbarItem>
          <ToolbarItem>
            <Button variant="plain" title="Refresh" aria-label="Refresh" onClick={() => onRefresh()}>
              <SyncIcon />
            </Button>
          </ToolbarItem>
        </ToolbarItem>
        <ToolbarItem variant={ToolbarItemVariant.pagination}>
          <PaginationContainer
            total={total}
            page={page}
            pageSize={pageSize}
            onSetPage={setPage}
            onSetPageSize={setPageSize}
            paginationId="audit-overview-top-pagination"
          />
        </ToolbarItem>
      </ToolbarContent>
    </Toolbar>
  );
};

type AuditToolbarBottomProps = {
  total: number;
  pageSize: number;
  page: number;
  setPage: (page: number) => void;
  setPageSize: (pageSize: number) => void;
};

export const AuditToolbarBottom = (props: AuditToolbarBottomProps) => {
  const { total, pageSize, page, setPage, setPageSize } = props;
  return (
    <Toolbar id="audit-overview-bottom-toolbar">
      <ToolbarContent>
        <ToolbarItem variant="pagination">
          <PaginationContainer
            total={total}
            page={page}
            pageSize={pageSize}
            onSetPage={setPage}
            onSetPageSize={setPageSize}
            paginationId="audit-overview-bottom-pagination"
          />
        </ToolbarItem>
      </ToolbarContent>
    </Toolbar>
  );
};
