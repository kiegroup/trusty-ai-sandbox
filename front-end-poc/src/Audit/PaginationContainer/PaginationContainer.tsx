import React from 'react';
import {Pagination, PaginationVariant} from "@patternfly/react-core";

type ownProps = {
    total: number,
    page: number,
    pageSize: number,
    paginationId: string,
    onSetPage: (page: number) => void,
    onSetPageSize: (size: number) => void
}

const PaginationContainer = (props: ownProps) => {
    const { total, page, pageSize, paginationId } = props;
    const updatePage = (event: any, page: number) => {
        props.onSetPage(page);
    };
    const updatePageSize = (event: any, page: number) => {
        props.onSetPageSize(page);
    };
    return (
        <Pagination
            itemCount={total}
            perPage={pageSize}
            page={page}
            onSetPage={updatePage}
            widgetId={paginationId}
            onPerPageSelect={updatePageSize}
            variant={PaginationVariant.right}
        />
    );
};

export default PaginationContainer;