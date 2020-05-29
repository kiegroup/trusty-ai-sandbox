import React from "react";
import { VictoryChart, VictoryLine, VictoryTheme, VictoryAxis } from "victory";

let chartData = [
  { x: 0, y: 0 },
  { x: 0.1, y: 10 },
  { x: 0.2, y: 18 },
  { x: 0.3, y: 48 },
  { x: 0.4, y: 19 },
  { x: 0.45, y: 20 },
  { x: 0.5, y: 33 },
  { x: 0.6, y: 49 },
  { x: 0.7, y: 77 },
  { x: 0.8, y: 65 },
  { x: 0.9, y: 31 },
  { x: 0.1, y: 7 },
];

const DecisionDistributionChart = () => {
  return (
    <VictoryChart theme={VictoryTheme.material} height={130} padding={{ top: 10, right: 30, bottom: 30, left: 40 }}>
      <VictoryLine
        style={{
          data: { stroke: "var(--pf-global--info-color--200)" },
          parent: { border: "1px solid #ccc" },
        }}
        data={chartData}
        interpolation="basis"
      />
      <VictoryAxis
        dependentAxis
        axisValue={0.46}
        style={{
          axis: {
            stroke: "var(--pf-global--palette--orange-200)",
          },
          ticks: {
            stroke: "transparent",
          },
          tickLabels: { fill: "none" },
          grid: {
            stroke: "transparent",
          },
        }}
      />
      <VictoryAxis
        label="Scores"
        style={{
          tickLabels: {
            fontSize: 8,
            fontFamily: "overpass",
            padding: 4,
          },
          grid: {
            stroke: "transparent",
          },
          axisLabel: {
            fontFamily: "overpass",
            fontSize: 8,
            padding: 20,
          },
        }}
      />
      <VictoryAxis
        dependentAxis
        label="Decisions"
        style={{
          tickLabels: {
            fontSize: 8,
            fontFamily: "overpass",
            padding: 4,
          },
          grid: {
            stroke: "transparent",
          },
          axisLabel: {
            fontFamily: "overpass",
            fontSize: 8,
            padding: 25,
          },
        }}
      />
    </VictoryChart>
  );
};

export default DecisionDistributionChart;
