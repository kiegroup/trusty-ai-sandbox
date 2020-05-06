import React from "react";
import { IFeatureScores } from "../ExplanationView/ExplanationView";
import {
    Chart,
    ChartAxis,
    ChartBar
} from "@patternfly/react-charts";

const colorFill = (value: number): string => {
    return (value >= 0) ? 'var(--pf-global--info-color--100)' : 'var(--pf-global--palette--orange-300)';
};

type ownProps = {
    onlyTopFeatures?: boolean,
    featuresScore: IFeatureScores[]
}

const FeaturesScoreChart = (props: ownProps) => {
    const { featuresScore } = props;

    return (
        <Chart
            ariaDesc="Importance of different features on the decision"
            ariaTitle="Features Scores Chart"
            domainPadding={{ x: [20, 20], y: 80 }}
            width={800}
            height={500}
            horizontal
            padding={{ top: 20, right: 50, bottom: 30, left: 50 }}
        >
            <ChartAxis tickFormat={() => ""}/>
            <ChartBar
                animate={{
                    duration: 2000,
                    onLoad: { duration: 1000 }
                }}
                data={featuresScore}
                x="featureName"
                y="featureScore"
                labels={({ datum }) => `${datum.featureName} \n ${(Math.floor(datum.featureScore * 100) / 100)}`}
                alignment="middle"
                barWidth={20}
                style={{
                    data: {
                        fill: (data: any) => {
                            return colorFill(data.datum.featureScore);
                        }
                    }
                }}
            />
        </Chart>
    )
};

export default FeaturesScoreChart;