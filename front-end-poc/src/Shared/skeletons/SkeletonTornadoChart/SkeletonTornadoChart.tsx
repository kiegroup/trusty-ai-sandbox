import React from "react";
import SkeletonInlineStripe from "../SkeletonInlineStripe";
import "./SkeletonTornadoChart.scss";
import { v4 as uuid } from "uuid";

type ownProps = {
  valuesCount: number;
  width?: number;
  height: number;
};

const SkeletonTornadoChart = (props: ownProps) => {
  const { valuesCount = 10, width = "100%", height = 500 } = props;
  let stripes = [];

  for (let i = 0; i < valuesCount; i++) {
    let width = 45 - (40 / valuesCount) * i;
    let left = i % 2 ? 50 : 50 - width;
    let top = i * 10 + 1;
    stripes.push(
      <SkeletonInlineStripe key={uuid()} customStyle={{ width: width + "%", left: left + "%", top: top + "%" }} />
    );
  }

  return (
    <div className="skeleton-tornado" style={{ width, height }}>
      <div className="skeleton-tornado__legend">
        <SkeletonInlineStripe />
        <SkeletonInlineStripe />
      </div>
      <div className="skeleton-tornado__chart">{stripes.map((item) => item)}</div>
    </div>
  );
};

export default SkeletonTornadoChart;
