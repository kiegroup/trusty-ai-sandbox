import React from 'react';
import {VictoryChart, VictoryAxis, VictoryBar, VictoryTooltip} from "victory";

const sampleData = [
    { x: 1, y: 10, label: "Product A"},
    { x: 2, y: 18, label: "Product B"},
    { x: 3, y: 48, label: "Product C"},
    { x: 4, y: 19, label: "Product D"},
    { x: 5, y: 33, label: "Product E"},
    { x: 6, y: 49, label: "Product F"},
    { x: 7, y: 77, label: "Product G"},
    { x: 8, y: 65, label: "Product H"},
    { x: 9, y: 31, label: "Product I"},
    { x: 10, y: 7, label: "Product L" }
];

const RecommendationDistributionChart = () => {
    return (
        <div>
            <VictoryChart
                domainPadding={{x: [20, 20]}}
                height={240}
                padding={{left: 50, right: 50, top: 30, bottom: 20}}
            >
                <VictoryBar
                    style={{
                        data: {
                            fill: "#519DE9"
                        }
                    }}
                    alignment="middle"
                    data={sampleData}
                    labelComponent={
                        <VictoryTooltip
                            constrainToVisibleArea
                            renderInPortal={false}
                            flyoutStyle={{
                                strokeWidth: 1,
                                fill: "#000"

                            }}
                            cornerRadius={0}
                            style={{
                                fontFamily: "overpass",
                                fontSize: 10,
                                borderRadius: 0,
                                fill: "#fff"
                            }}
                            text={({datum}) => `${datum.label}: ${datum.y}`}
                        />
                    }
                />
                <VictoryAxis
                    tickValues={[]}
                    tickFormat={() => ""}
                />
                <VictoryAxis
                    label="Recommendations Number"
                    dependentAxis={true}
                    tickCount={8}
                    style={{
                        tickLabels: {
                            fontSize: 10,
                            fontFamily: "overpass"
                        },
                        axisLabel: {
                            fontFamily: "overpass",
                            fontSize: 10,
                            padding: 30
                        }
                    }}
                />
            </VictoryChart>
        </div>
    );
};

export default RecommendationDistributionChart;