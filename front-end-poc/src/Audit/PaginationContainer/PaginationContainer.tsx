import React from "react";
import { Pagination, PaginationVariant } from "@patternfly/react-core";

type PaginationContainerProps = {
  total: number;
  page: number;
  pageSize: number;
  paginationId: string;
  onSetPage: (page: number) => void;
  onSetPageSize: (size: number) => void;
};

const PaginationContainer = (props: PaginationContainerProps) => {
  const { total, page, pageSize, paginationId, onSetPage, onSetPageSize } = props;

  const updatePage = (event: any, page: number) => {
    onSetPage(page);
  };
  const updatePageSize = (event: any, page: number) => {
    onSetPageSize(page);
  };
  return (
    <Pagination
      itemCount={total}
      perPage={pageSize}
      page={page}
      onSetPage={updatePage}
      widgetId={paginationId}
      onPerPageSelect={updatePageSize}
      variant={PaginationVariant.top}
    />
  );
};

export default PaginationContainer;
