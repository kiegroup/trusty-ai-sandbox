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
        domain={{ y: [0, 3] }}
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
          data={[
            { name: "Merchant" },
            { name: "Transactions" },
            { name: "Denied" },
            { name: "Amount" },
            { name: "24 hrs" },
          ]}
          colorScale={["var(--pf-global--info-color--100)", "var(--pf-global--palette--orange-300)"]}
          x={465}
          y={85}
          orientation="vertical"
        />
        <ChartGroup offset={11}>
          <ChartBar
            data={[
              { name: "Merchant", x: "w1", y: 1 },
              { name: "Merchant", x: "w2", y: 2 },
              { name: "Merchant", x: "w3", y: 3 },
              { name: "Merchant", x: "w4", y: 2 },
              { name: "Merchant", x: "w5", y: 2 },
            ]}
            style={{ data: { fill: "var(--pf-global--info-color--100)" } }}
          />
          <ChartBar
            data={[
              { name: "Transactions", x: "w1", y: 0 },
              { name: "Transactions", x: "w2", y: 0 },
              { name: "Transactions", x: "w3", y: 3 },
              { name: "Transactions", x: "w4", y: 3 },
              { name: "Transactions", x: "w5", y: 1 },
            ]}
            style={{ data: { fill: "var(--pf-global--palette--orange-300)" } }}
          />
          <ChartBar
            data={[
              { name: "Denied", x: "w1", y: 3 },
              { name: "Denied", x: "w2", y: 0 },
              { name: "Denied", x: "w3", y: 1 },
              { name: "Denied", x: "w4", y: 0 },
              { name: "Denied", x: "w5", y: 0 },
            ]}
            style={{ data: { fill: "var(--pf-global--info-color--100)" } }}
          />
          <ChartBar
            data={[
              { name: "Amount", x: "w1", y: 1 },
              { name: "Amount", x: "w2", y: 1 },
              { name: "Amount", x: "w3", y: 0 },
              { name: "Amount", x: "w4", y: 2 },
              { name: "Amount", x: "w5", y: 2 },
            ]}
            style={{ data: { fill: "var(--pf-global--palette--orange-300)" } }}
          />
          <ChartBar
            data={[
              { name: "24 hrs", x: "w1", y: 0 },
              { name: "24 hrs", x: "w2", y: 1 },
              { name: "24 hrs", x: "w3", y: 0 },
              { name: "24 hrs", x: "w4", y: 2 },
              { name: "24 hrs", x: "w5", y: 0 },
            ]}
            style={{ data: { fill: "var(--pf-global--info-color--100)" } }}
          />
        </ChartGroup>
      </Chart>
    </div>
  );
};

export default FraudScoringDistribution;
