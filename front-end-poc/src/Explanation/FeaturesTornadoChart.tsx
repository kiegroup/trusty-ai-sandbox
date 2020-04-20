import React from "react";
import {VictoryBar, VictoryChart, VictoryAxis} from "victory";
import {sortBy} from "lodash";

let chartData = [
    {feature: 1, weight: 0.21, desc: 'Annual Income', topFeature: true},
    {feature: 2, weight: 0.23, desc: 'Mortgage Amount', topFeature: true},
    {feature: 3, weight: -0.34, desc: 'Down Payment', topFeature: true},
    {feature: 4, weight: 0.11, desc: 'Years of amortization', topFeature: true},
    {feature: 5, weight: -0.13, desc: 'Age of Property (yrs)', topFeature: true},
    {feature: 6, weight: -0.33, desc: 'Sale Price', topFeature: true},
    {feature: 1, weight: 0.06, desc: 'Purchase Price', topFeature: false},
    {feature: 2, weight: 0.03, desc: 'Monthly tax payment', topFeature: false},
    {feature: 3, weight: 0.04, desc: 'Monthly insurance payment', topFeature: false},
    {feature: 4, weight: 0.03, desc: 'Property / City', topFeature: false},
    {feature: 5, weight: -0.03, desc: 'Property / State', topFeature: false},
    {feature: 6, weight: -0.02, desc: 'Liabilities / Asset #1', topFeature: false},
    {feature: 6, weight: -0.02, desc: 'Liabilities / Asset #2', topFeature: false},
    {feature: 6, weight: -0.01, desc: 'Lender Ratings / Customer Rating #1', topFeature: false},
    {feature: 6, weight: -0.01, desc: 'Lender Ratings / Customer Rating #2', topFeature: false}
];
chartData = sortBy(chartData, [(item) => Math.abs(item.weight)]);
chartData.map((item, i) => {
    item.feature = i;
    return item;
});
const colorFill = (value: number): string => {
    return (value >= 0) ? 'var(--pf-global--info-color--100)' : 'var(--pf-global--palette--orange-200)';
};
const FeaturesTornadoChart = (props:{onlyTopFeatures?:boolean}) => {
    const { onlyTopFeatures } = props;
    let labelSize = 6;
    let chartDomain = [...chartData];
    if (onlyTopFeatures === true) {
        chartDomain = chartDomain.filter(item => item.topFeature);
        labelSize = 10;
    }
    return (
        <>
            <VictoryChart
                domainPadding={{x: [20, 20], y: 50}}
                height={260}
                padding={{top: 10, right:50, bottom: 30, left: 50}}
            >
                <VictoryAxis tickFormat={() => ""} style={{axis: {stroke: "#c6c6c6"}}}/>
                <VictoryBar
                    data={chartDomain}
                    x="feature"
                    y="weight"
                    horizontal
                    labels={({ datum }) => `${datum.desc} \n ${datum.weight}`}
                    alignment="middle"
                    style={{
                        data: {
                            fill: ({ datum }) => colorFill(datum.weight)
                        },
                        labels: {
                            fontSize: labelSize,
                            fontFamily: "overpass"
                        }
                    }}
                />
            </VictoryChart>
        </>
    )
};

export default FeaturesTornadoChart;