import React from "react";
import { IFeatureScores } from "../ExplanationView/ExplanationView";
import { Chart, ChartAxis, ChartBar, ChartLegend } from "@patternfly/react-charts";

const colorFill = (value: number): string => {
  return value >= 0 ? "var(--pf-global--info-color--100)" : "var(--pf-global--palette--orange-300)";
};

const colorOpacity = (value: number): number => {
  const computedOpacity = Math.floor(Math.abs(value) * 100) / 100;
  return computedOpacity < 0.25 ? 0.25 : computedOpacity;
};

type ownProps = {
  onlyTopFeatures?: boolean;
  featuresScore: IFeatureScores[];
};

const FeaturesScoreChart = (props: ownProps) => {
  const { featuresScore } = props;

  return (
    <Chart
      ariaDesc="Importance of different features on the decision"
      ariaTitle="Features Scores Chart"
      width={800}
      height={500}
      domainPadding={{ x: [20, 20], y: 80 }}
      domain={{ y: [-1, 1] }}
      horizontal
      padding={{ top: 60, right: 30, bottom: 30, left: 30 }}>
      <ChartAxis tickFormat={() => ""} />

      <ChartBar
        animate={{
          duration: 2000,
          onLoad: { duration: 1000 },
        }}
        data={featuresScore}
        x="featureName"
        y="featureScore"
        labels={({ datum }) => `${datum.featureName}\n${Math.floor(datum.featureScore * 100) / 100}`}
        alignment="middle"
        barWidth={25}
        style={{
          data: {
            fill: (data: any) => {
              return colorFill(data.datum.featureScore);
            },
            opacity: (data: any) => {
              return colorOpacity(data.datum.featureScore);
            },
          },
        }}
      />

      <ChartLegend
        data={[{ name: "Negative Impact" }, { name: "Positive Impact" }]}
        colorScale={["var(--pf-global--palette--orange-300)", "var(--pf-global--info-color--100)"]}
        x={250}
        y={10}
      />
    </Chart>
  );
};

export default FeaturesScoreChart;
