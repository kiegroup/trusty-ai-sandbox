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
          data={[{ name: "Fraud" }, { name: "Denials" }]}
          colorScale={["var(--pf-global--info-color--100)", "var(--pf-global--palette--orange-300)"]}
          x={465}
          y={85}
          orientation="vertical"
        />
        <ChartGroup offset={11}>
          <ChartBar
            data={[
              { name: "Fraud", x: "w1", y: 10 },
              { name: "Fraud", x: "w2", y: 23 },
              { name: "Fraud", x: "w3", y: 52 },
              { name: "Fraud", x: "w4", y: 33 },
              { name: "Fraud", x: "w5", y: 12 },
            ]}
            style={{ data: { fill: "var(--pf-global--info-color--100)" } }}
          />
        </ChartGroup>
      </Chart>
    </div>
  );
};

export default FraudScoringDistribution;
