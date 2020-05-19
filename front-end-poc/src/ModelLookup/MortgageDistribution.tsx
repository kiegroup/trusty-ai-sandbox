import React from "react";
import { Chart, ChartAxis, ChartBar, ChartGroup, ChartLegend, ChartVoronoiContainer } from "@patternfly/react-charts";

const MortgageDistribution = () => {
  return (
    <div style={{ height: "auto", width: "100%" }}>
      <Chart
        ariaDesc="Model distribution over 60 days"
        ariaTitle="Model distribution"
        containerComponent={
          <ChartVoronoiContainer labels={({ datum }) => `${datum.name}: ${datum.y}`} constrainToVisibleArea />
        }
        domain={{ y: [0, 100] }}
        domainPadding={{ x: [30, 25] }}
        height={250}
        padding={{
          bottom: 50,
          left: 50,
          right: 150, // Adjusted to accommodate legend
          top: 50,
        }}
        width={600}>
        <ChartAxis />
        <ChartAxis dependentAxis showGrid />
        <ChartLegend
          data={[{ name: "Approvals" }, { name: "Denials" }]}
          colorScale={["var(--pf-global--info-color--100)", "var(--pf-global--palette--orange-300)"]}
          x={465}
          y={85}
          orientation="vertical"
        />
        <ChartGroup offset={11}>
          <ChartBar
            data={[
              { name: "Approvals", x: "w1", y: 10 },
              { name: "Approvals", x: "w2", y: 23 },
              { name: "Approvals", x: "w3", y: 52 },
              { name: "Approvals", x: "w4", y: 33 },
              { name: "Approvals", x: "w5", y: 12 },
              { name: "Approvals", x: "w6", y: 21 },
              { name: "Approvals", x: "w7", y: 52 },
              { name: "Approvals", x: "w8", y: 30 },
            ]}
            style={{ data: { fill: "var(--pf-global--info-color--100)" } }}
          />
          <ChartBar
            data={[
              { name: "Denials", x: "w1", y: 32 },
              { name: "Denials", x: "w2", y: 12 },
              { name: "Denials", x: "w3", y: 79 },
              { name: "Denials", x: "w4", y: 68 },
              { name: "Denials", x: "w5", y: 47 },
              { name: "Denials", x: "w6", y: 32 },
              { name: "Denials", x: "w7", y: 40 },
              { name: "Denials", x: "w8", y: 78 },
            ]}
            style={{ data: { fill: "var(--pf-global--palette--orange-300)" } }}
          />
        </ChartGroup>
      </Chart>
    </div>
  );
};

export default MortgageDistribution;
