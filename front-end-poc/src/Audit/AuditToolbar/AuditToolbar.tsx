import React, { useRef } from "react";
import {
  DataToolbar,
  DataToolbarContent,
  DataToolbarItem,
  DataToolbarItemVariant,
} from "@patternfly/react-core/dist/js/experimental";
import { Button, ButtonVariant, InputGroup, TextInput } from "@patternfly/react-core";
import { SearchIcon } from "@patternfly/react-icons";
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
};

const AuditToolbar = (props: AuditToolbarProps) => {
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
    <DataToolbar id="audit-list-top-toolbar" style={{ marginBottom: "var(--pf-global--spacer--lg)" }}>
      <DataToolbarContent>
        <DataToolbarItem variant="label">Search</DataToolbarItem>
        <DataToolbarItem>
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
  );
};

export default AuditToolbar;
