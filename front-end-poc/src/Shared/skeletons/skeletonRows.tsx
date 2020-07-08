import React from "react";
import { IRow } from "@patternfly/react-table";
import SkeletonStripe from "./SkeletonStripe/SkeletonStripe";

/*
 * Based on a number of rows and columns, this function creates an array specifically intended
 * for use with the Patternfly table component. Feeding this array to the table rows prop,
 * it will produce animated stripes to be displayed while loading the real data
 * */

const skeletonRows = (colsNumber: number, rowsNumber: number, rowKey?: string) => {
  let skeletons = [];
  rowKey = rowKey || "key";
  for (let j = 0; j < rowsNumber; j++) {
    let cells = [];
    for (let i = 0; i < colsNumber; i++) {
      const size = (i + j) % 2 ? "lg" : "md";
      cells.push({
        title: <SkeletonStripe size={size} />,
      });
    }
    const skeletonRow: IRow = {
      cells,
    };
    skeletonRow[rowKey] = `skeleton-${j}`;
    skeletons.push(skeletonRow);
  }
  return skeletons;
};

export default skeletonRows;
