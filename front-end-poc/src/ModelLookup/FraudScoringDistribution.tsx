import { Chart, ChartAxis, ChartBar, ChartGroup, ChartLegend, ChartVoronoiContainer } from "@patternfly/react-charts";
import React from "react";

const FraudScoringDistribution = () => {
  return (
    <div style={{ height: "auto", width: "100%" }}>
      <Chart
        ariaDesc="Model distribution over 60 days"
        ariaTitle="Model distribution"
        containerComponent={
          <ChartVoronoiContainer labels={({ datum }) => `${datum.name}: ${datum.y}`} constrainToVisibleArea />
        }
        domain={{ y: [0, 50] }}
        domainPadding={{ x: [30, 25] }}
        height={250}
        padding={{
          bottom: 50,
          left: 50,
          right: 100, // Adjusted to accommodate legend
          top: 50,
        }}
        width={600}>
        <ChartAxis />
        <ChartAxis dependentAxis showGrid />
        <ChartLegend
          data={[{ name: "0" }, { name: "1" }, { name: "2" }, { name: "3" }]}
          colorScale={[
            "var(--pf-global--palette--orange-100)",
            "var(--pf-global--palette--orange-200)",
            "var(--pf-global--palette--orange-300)",
            "var(--pf-global--palette--orange-400)",
          ]}
          x={515}
          y={65}
          orientation="vertical"
        />
        <ChartGroup offset={11}>
          <ChartBar
            data={[
              { name: "0", x: "w1", y: 10 },
              { name: "0", x: "w2", y: 25 },
              { name: "0", x: "w3", y: 7 },
              { name: "0", x: "w4", y: 19 },
              { name: "0", x: "w5", y: 12 },
            ]}
            style={{ data: { fill: "var(--pf-global--palette--orange-100)" } }}
          />
          <ChartBar
            data={[
              { name: "1", x: "w1", y: 35 },
              { name: "1", x: "w2", y: 32 },
              { name: "1", x: "w3", y: 29 },
              { name: "1", x: "w4", y: 7 },
              { name: "1", x: "w5", y: 19 },
            ]}
            style={{ data: { fill: "var(--pf-global--palette--orange-200)" } }}
          />
          <ChartBar
            data={[
              { name: "2", x: "w1", y: 7 },
              { name: "2", x: "w2", y: 9 },
              { name: "2", x: "w3", y: 40 },
              { name: "2", x: "w4", y: 32 },
              { name: "2", x: "w5", y: 30 },
            ]}
            style={{ data: { fill: "var(--pf-global--palette--orange-300)" } }}
          />
          <ChartBar
            data={[
              { name: "3", x: "w1", y: 3 },
              { name: "3", x: "w2", y: 9 },
              { name: "3", x: "w3", y: 45 },
              { name: "3", x: "w4", y: 12 },
              { name: "3", x: "w5", y: 15 },
            ]}
            style={{ data: { fill: "var(--pf-global--palette--orange-400)" } }}
          />
        </ChartGroup>
      </Chart>
    </div>
  );
};

export default FraudScoringDistribution;
