import React from 'react';
import { Chart, ChartLabel, ChartAxis, ChartGroup, ChartLine, ChartVoronoiContainer } from '@patternfly/react-charts';

class Line {
    //y=mx+c
    m: number = 0;
    c: number = 0;
    title: string = "";
}

type Props = {
    modelName: string
    independentAxisTitle: string
    dependentAxisTitle: string
    width?: number
    height?: number
    lines: Line[]
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

    private roundedTo50(_float: number): number {
        return _float;
        //        return Math.round(_float / 50) * 50;
    }

    private getTicks(start: number, end: number, count: number): number[] {
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

        const maxIntersect: number = Math.max(...this.props.lines.map(line => line.c));
        const maxDomainY: number = this.roundedTo50(maxIntersect * 2);
        const minDomainY: number = -maxDomainY;

        const minGradient: number = Math.min(...this.props.lines.map(line => line.m));
        const maxDomainX: number = this.roundedTo50((maxDomainY - maxIntersect) / minGradient);
        const minDomainX: number = -maxDomainX;

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
                        tickValues={this.getTicks(minDomainX, maxDomainX, 8)}
                        tickFormat={(x) => this.roundedToFixed(x, 2)}
                    />
                    <ChartAxis
                        label={this.props.dependentAxisTitle} dependentAxis showGrid
                        tickValues={this.getTicks(minDomainY, maxDomainY, 8)}
                        tickFormat={(x) => this.roundedToFixed(x, 2)}
                    />
                    <ChartGroup>
                        {this.props.lines.map((line) => {
                            return <ChartLine
                                samples={100}
                                domain={{ x: [minDomainX, maxDomainX], y: [minDomainY, maxDomainY] }}
                                y={(datum: any) => line.m * datum.x + line.c}
                            />
                        })}
                    </ChartGroup>
                </Chart>
            </div>
        )
    }

}

export { LinearRegressionView, Line }