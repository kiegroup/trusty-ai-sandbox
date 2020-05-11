import React, { useCallback, useMemo } from "react";
import { IFeatureScores } from "../ExplanationView/ExplanationView";
import { Chart, ChartAxis, ChartBar, ChartLegend } from "@patternfly/react-charts";

type ownProps = {
  featuresScore: IFeatureScores[];
  large?: boolean;
};

const FeaturesScoreChart = (props: ownProps) => {
  const { featuresScore, large = false } = props;
  const width = large ? 1400 : 800;
  const height = large ? 50 * featuresScore.length : 500;

  const labels = useMemo(() => {
    console.log("compute labels");
    let labels: string[] = [];
    featuresScore.forEach((item) => {
      labels.push(`${item.featureName}\n${Math.floor(item.featureScore * 100) / 100}`);
    });
    return labels;
  }, [featuresScore]);

  const computeOpacity = useCallback((data) => {
    const computedOpacity = Math.floor(Math.abs(data.datum.featureScore) * 100) / 100;
    return computedOpacity < 0.25 ? 0.25 : computedOpacity;
  }, []);

  const computeColor = useCallback((data) => {
    return data.datum.featureScore >= 0 ? "var(--pf-global--info-color--100)" : "var(--pf-global--palette--orange-300)";
  }, []);

  return (
    <Chart
      ariaDesc="Importance of different features on the decision"
      ariaTitle="Features Scores Chart"
      width={width}
      height={height}
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
        labels={labels}
        alignment="middle"
        barWidth={25}
        style={{
          data: {
            fill: computeColor,
            opacity: computeOpacity,
          },
        }}
      />

      <ChartLegend
        data={[{ name: "Negative Impact" }, { name: "Positive Impact" }]}
        colorScale={["var(--pf-global--palette--orange-300)", "var(--pf-global--info-color--100)"]}
        x={width / 2 - 150}
        y={10}
      />
    </Chart>
  );
};

export default FeaturesScoreChart;
