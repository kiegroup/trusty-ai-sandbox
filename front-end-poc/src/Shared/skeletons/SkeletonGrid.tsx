import React from "react";
import { Grid, GridItem, gridSpans } from "@patternfly/react-core";

type ownProps = {
  rowsNumber: number;
  colsNumber: number | number[];
  gutterSize: "sm" | "md" | "lg";
};

/*
 * SkeletonGrid can be called passing as colsNumber a number of columns or an explicit list of columns sizes that
 * will be fed to the css grid component. This is intended for cases when an uneven number of columns
 * is needed or when specific columns size are wanted instead of columns with equally divided size
 * */
const SkeletonGrid = (props: ownProps) => {
  const { rowsNumber, colsNumber, gutterSize } = props;
  let colsCount = 0;
  let colList: number[] = [];

  if (typeof colsNumber === "number") {
    colsCount = colsNumber;
    colList.length = colsNumber;
    colList.fill(Math.floor(12 / colsNumber));
  }
  if (Array.isArray(colsNumber)) {
    colsCount = colsNumber.length;
    colList = colsNumber;
  }
  const gridRows = [];
  for (let i = 0; i < rowsNumber; i++) {
    for (let j = 0; j < colsCount; j++) {
      let className = "skeleton__stripe";
      className += (i + j) % 2 ? " skeleton__stripe--lg" : " skeleton__stripe--md";
      gridRows.push(
        <GridItem span={colList[j] as gridSpans} key={`skeleton-grid-${j}-${i}`}>
          <span className={className} />
        </GridItem>
      );
    }
  }
  return <Grid gutter={gutterSize}>{gridRows.map((item) => item)}</Grid>;
};

export default SkeletonGrid;
