import React from 'react';
import {DataList, DataListCell, DataListItem, DataListItemCells, DataListItemRow} from "@patternfly/react-core";

type ownProps = {
    rowsNumber: number,
    colsNumber: number,
    hasHeader?: boolean
}

const SkeletonDataList = (props: ownProps) => {
    const { rowsNumber, colsNumber, hasHeader } = props;

    let rows = [];
    for (let i = 0; i < rowsNumber; i++) {
        let row = [];
        for (let j = 0; j < colsNumber; j++) {
            let className = "skeleton__stripe";
            className += ((i + j) % 2 ) ? " skeleton__stripe--lg" : " skeleton__stripe--md";
            row.push(
                <DataListCell key={`content-${j}`}>
                    <span className={className} />
                </DataListCell>
            )
        }
        let skeletonRow = {
            cells: row,
            key: 'skeleton-row-' + i
        };
        rows.push(skeletonRow);
    }

    return (
        <DataList aria-label="Loading content">
            <DataListItem aria-labelledby="Loading content">
                {rows.map((item, index) => {
                    let headerClass;
                    if (hasHeader && index === 0) {
                        headerClass = 'skeleton__header';
                    }
                    return (
                        <DataListItemRow className={headerClass} key={item.key}>
                            <DataListItemCells
                                dataListCells={item.cells}
                            />
                        </DataListItemRow>
                    )
                })}
            </DataListItem>
        </DataList>
    );
};

export default SkeletonDataList;