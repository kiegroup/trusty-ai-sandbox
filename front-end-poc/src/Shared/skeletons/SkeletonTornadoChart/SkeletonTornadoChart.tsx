import React from "react";
import "./SkeletonTornadoChart.scss";
import { v4 as uuid } from "uuid";
import SkeletonStripe from "../SkeletonStripe/SkeletonStripe";

type SkeletonTornadoChartProps = {
  valuesCount: number;
  width?: number;
  height: number;
};

const SkeletonTornadoChart = (props: SkeletonTornadoChartProps) => {
  const { valuesCount = 10, width = "100%", height = 500 } = props;
  let stripes = [];

  for (let i = 0; i < valuesCount; i++) {
    let width = 45 - (40 / valuesCount) * i;
    let left = i % 2 ? 50 : 50 - width;
    let top = i * 10 + 1;
    stripes.push(
      <SkeletonStripe
        isInline={true}
        key={uuid()}
        customStyle={{ width: width + "%", left: left + "%", top: top + "%" }}
      />
    );
  }

  return (
    <div className="skeleton-tornado" style={{ width, height }}>
      <div className="skeleton-tornado__legend">
        <SkeletonStripe isInline={true} />
        <SkeletonStripe isInline={true} />
      </div>
      <div className="skeleton-tornado__chart">{stripes.map((item) => item)}</div>
    </div>
  );
};

export default SkeletonTornadoChart;
