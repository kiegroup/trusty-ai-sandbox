import React from "react";
import { VictoryBar, VictoryChart, VictoryAxis } from "victory";
import { map, maxBy } from "lodash";

let chartData = [
  {
    x: 1,
    y: 20,
    desc: "10-30k",
  },
  {
    x: 2,
    y: 35,
    desc: "30-50k",
  },
  {
    x: 3,
    y: 80,
    desc: "50-70k",
  },
  {
    x: 4,
    y: 160,
    desc: "70-90k",
  },
  {
    x: 5,
    y: 60,
    desc: ">90k",
  },
];

const FeatureDistribution = () => {
  const ticks = map(chartData, "desc");
  const maxItem = maxBy(chartData, (item) => item.y);
  const max = maxItem && maxItem.y ? maxItem.y : 100;
  const colors = ["#B2B0EA", "#8481DD", "#5752D1", "#3C3D99", "#2A265F"];
  return (
    <div>
      <VictoryChart
        domainPadding={{ x: 28, y: 5 }}
        width={300}
        height={120}
        padding={{ top: 20, left: 40, right: 0, bottom: 30 }}
        domain={{ y: [0, max] }}>
        <VictoryAxis
          tickValues={ticks}
          style={{
            tickLabels: {
              fontSize: 15,
            },
            axis: {
              stroke: "#c6c6c6",
            },
          }}
        />
        <VictoryAxis
          dependentAxis
          tickValues={[max]}
          maxDomain={{ y: max }}
          style={{
            tickLabels: {
              fontSize: 15,
            },
            axis: {
              stroke: "#c6c6c6",
            },
          }}
        />
        <VictoryBar
          barRatio={1.2}
          style={{
            data: {
              fill: ({ datum }) => {
                return colors[datum.x - 1];
              },
            },
          }}
          data={chartData}
        />
      </VictoryChart>
    </div>
  );
};

export default FeatureDistribution;
