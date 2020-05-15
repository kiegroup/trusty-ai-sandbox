import React, { useCallback, useMemo } from "react";
import { IFeatureScores } from "../ExplanationView/ExplanationView";
import { Chart, ChartAxis, ChartBar, ChartLegend } from "@patternfly/react-charts";
import { maxBy } from "lodash";
import formattedScore from "../../Shared/components/FormattedScore/formattedScore";

type ownProps = {
  featuresScore: IFeatureScores[];
  large?: boolean;
};

const FeaturesScoreChart = (props: ownProps) => {
  const { featuresScore, large = false } = props;
  const width = large ? 1400 : 800;
  const height = large ? 50 * featuresScore.length : 500;

  const maxValue = useMemo(() => {
    const max = maxBy(featuresScore, function (item) {
      return Math.abs(item.featureScore);
    });
    return max ? max.featureScore : 1;
  }, [featuresScore]);

  const labels = useMemo(() => {
    let labels: string[] = [];
    featuresScore.forEach((item) => {
      labels.push(`${item.featureName}\n${formattedScore(item.featureScore)}`);
    });
    return labels;
  }, [featuresScore]);

  const computeOpacity = useCallback(
    (data) => {
      const computedOpacity = Math.abs(Math.floor((data.datum.featureScore / maxValue) * 100) / 100);
      return computedOpacity < 0.25 ? 0.25 : computedOpacity;
    },
    [maxValue]
  );

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
      domain={{ y: [-maxValue, maxValue] }}
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
