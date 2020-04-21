import React from 'react';
import { Chart, ChartLabel, ChartAxis, ChartGroup, ChartLine, ChartVoronoiContainer } from '@patternfly/react-charts';

class Line {
    //y=mx+c
    readonly m: number;
    readonly c: number;
    readonly title: string;

    constructor(m: number, c: number, title: string) {
        this.m = m;
        this.c = c;
        this.title = title;
    }
}

class Range {
    readonly min: number;
    readonly max: number;

    constructor(min: number, max: number) {
        this.min = min;
        this.max = max;
    }

}

type Props = {
    modelName: string
    independentAxisTitle: string
    dependentAxisTitle: string
    width?: number
    height?: number
    lines: Line[],
    rangeX: Range,
    rangeY: Range
}

type State = {
}

class LinearRegressionView extends React.Component<Props, State> {
    constructor(props: Props) {
        super(props);
    }

    private roundedToFixed(_float: number, _digits: number): string {
        var rounded = Math.pow(10, _digits);
        return (Math.round(_float * rounded) / rounded).toFixed(_digits);
    }

    private getTicks(range: Range, count: number): number[] {
        const start: number = range.min;
        const end: number = range.max;
        const step: number = (end - start) / count;
        const ticks: number[] = new Array<number>();
        var v: number = start;
        while (v <= end) {
            ticks.push(v);
            v = v + step;
        }
        if (ticks[ticks.length - 1] !== end) {
            ticks.push(end);
        }
        return ticks;
    }

    render() {
        const modelName: string = this.props.modelName;
        const width: number = this.props.width === undefined ? 500 : this.props.width;
        const height: number = this.props.height === undefined ? 500 : this.props.height;

        const legendData: any = [];
        this.props.lines.forEach(line => {
            legendData.push({ name: line.title });
        });

        const minDomainX: number = this.props.rangeX.min;
        const maxDomainX: number = this.props.rangeX.max;
        const minDomainY: number = this.props.rangeY.min;
        const maxDomainY: number = this.props.rangeY.max;

        return (
            <div style={{ height: height, width: width }}>
                <Chart
                    ariaTitle={modelName}
                    containerComponent={
                        <ChartVoronoiContainer
                            labels={({ datum }) => `${this.roundedToFixed(datum._x, 2)}, ${this.roundedToFixed(datum._y, 2)}`}
                            constrainToVisibleArea
                        />}
                    legendData={legendData}
                    legendOrientation="horizontal"
                    legendPosition="bottom"
                    padding={{
                        bottom: 100,
                        left: 50,
                        right: 50,
                        top: 50
                    }}
                    height={height}
                    width={width}
                >
                    <ChartLabel text={modelName} x={width / 2} y={30} textAnchor="middle" />
                    <ChartAxis
                        label={this.props.independentAxisTitle} showGrid
                        tickValues={this.getTicks(this.props.rangeX, 8)}
                        tickFormat={(x) => this.roundedToFixed(x, 2)}
                    />
                    <ChartAxis
                        label={this.props.dependentAxisTitle} dependentAxis showGrid
                        tickValues={this.getTicks(this.props.rangeY, 8)}
                        tickFormat={(x) => this.roundedToFixed(x, 2)}
                    />
                    <ChartGroup>
                        {this.props.lines.map((line) => {
                            return <ChartLine
                                samples={100}
                                domain={{
                                    x: [
                                        this.props.rangeX.min,
                                        this.props.rangeX.max
                                    ],
                                    y: [
                                        this.props.rangeY.min,
                                        this.props.rangeY.max
                                    ]
                                }}
                                y={(datum: any) => line.m * datum.x + line.c}
                            />
                        })}
                    </ChartGroup>
                </Chart>
            </div>
        )
    }

}

export { LinearRegressionView, Line, Range }