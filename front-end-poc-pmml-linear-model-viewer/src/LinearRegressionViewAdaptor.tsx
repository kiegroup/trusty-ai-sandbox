import React from 'react';
import * as PMML from "./generated/www.dmg.org/PMML-4_4";
import { Line, Range, LinearRegressionView } from './LinearRegressionView';
import { monitorEventLoopDelay } from 'perf_hooks';

type Props = {
  dictionary: PMML.DataDictionaryType
  model: PMML.RegressionModelType[]
}

type State = {
}

class LinearRegressionViewAdaptor extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
  }

  render() {
    const model: PMML.RegressionModelType | undefined = this.getLinearRegressionModel();
    if (model === undefined) {
      return (
        <div>Unsupported</div>
      );
    }

    const table: PMML.RegressionTableType | undefined = this.getRegressionTable(model);
    if (table === undefined) {
      return (
        <div>Unsupported</div>
      );
    }

    const numbericPredictor: PMML.NumericPredictorType | undefined = this.getNumericPredictorType(table);
    if (numbericPredictor === undefined) {
      return (
        <div>Unsupported</div>
      );
    }

    const modelName: string = model.modelName;
    const miningSchema: PMML.MiningSchemaType = model.MiningSchema;
    const dependentAxisTitle = miningSchema.MiningField.filter(mf => mf.usageType === "target")[0].name;
    const lines: Line[] = this.getLines(table, numbericPredictor);

    //Get Ranges from DataDictionary or use reasonable defaults
    let rangeY: Range | undefined = this.getYRange(model);
    if (rangeY === undefined) {
      rangeY = this.getDefaultYRange(lines);
    }
    let rangeX: Range | undefined = this.getXRange(numbericPredictor);
    if (rangeX === undefined) {
      rangeX = this.getDefaultXRange(lines, rangeY);
    }

    return (
      <div>
        <LinearRegressionView modelName={modelName}
          independentAxisTitle={numbericPredictor.name}
          dependentAxisTitle={dependentAxisTitle}
          lines={lines}
          rangeX={rangeX}
          rangeY={rangeY}
        />
      </div>
    );
  }

  private getLinearRegressionModel(): PMML.RegressionModelType | undefined {
    const models: PMML.RegressionModelType[] = this.props.model;
    if (models === undefined || models.length > 1) {
      return undefined;
    }
    const model: PMML.RegressionModelType = models[0];
    if (model.functionName === "regression" && model.algorithmName === "linearRegression") {
      return model;
    }
    return undefined;
  }

  private getRegressionTable(model: PMML.RegressionModelType): PMML.RegressionTableType | undefined {
    const tables: PMML.RegressionTableType[] = model.RegressionTable;
    if (tables === undefined || tables.length > 1) {
      return undefined;
    }
    return tables[0];
  }

  private getNumericPredictorType(table: PMML.RegressionTableType): PMML.NumericPredictorType | undefined {
    const predicators: PMML.NumericPredictorType[] | undefined = table.NumericPredictor;
    if (predicators === undefined || predicators.length > 1) {
      return undefined;
    }
    return predicators[0];
  }

  private getLines(table: PMML.RegressionTableType, numbericPredictor: PMML.NumericPredictorType): Line[] {
    const c: number = table.intercept;
    const line: Line = { m: numbericPredictor.coefficient, c: c, title: "base" };
    const lines: Line[] = new Array<Line>(line);

    //We need to duplicate the line for each CategoricalPredictor
    const categoricalPredictors: PMML.CategoricalPredictorType[] | undefined = table.CategoricalPredictor;
    if (categoricalPredictors === undefined) {
      return lines;
    }
    //cxml returns an array of CategoricalPredictorType with one element even if none are defined in the XML.
    //This is a hack to check the array does not contain the rogue element.
    if (categoricalPredictors.length == 1 && categoricalPredictors[0]._exists === false) {
      return lines;
    }
    categoricalPredictors.forEach(cp => {
      lines.push({ m: line.m, c: line.c + cp.coefficient, title: line.title + " (" + cp.value + ")" });
    });

    return lines;
  }

  private getYRange(model: PMML.RegressionModelType): Range | undefined {
    const targetField: PMML.MiningFieldType | undefined = this.getTargetMiningField(model);
    if (targetField === undefined) {
      return undefined;
    }
    const targetFieldIntervals: PMML.IntervalType[] | undefined = this.getMiningFieldIntervals(targetField.name);
    return this.getIntervalsMaximumRange(targetFieldIntervals);
  }

  private getTargetMiningField(model: PMML.RegressionModelType): PMML.MiningFieldType | undefined {
    const targetFields: PMML.MiningFieldType[] = model.MiningSchema.MiningField.filter(mf => mf.usageType === "target");
    if (targetFields === undefined || targetFields.length != 1) {
      return undefined;
    }
    return targetFields[0];
  }

  private getXRange(numbericPredictor: PMML.NumericPredictorType): Range | undefined {
    const predictorFieldIntervals: PMML.IntervalType[] | undefined = this.getMiningFieldIntervals(numbericPredictor.name);
    return this.getIntervalsMaximumRange(predictorFieldIntervals);
  }

  private getMiningFieldIntervals(fieldName: string): PMML.IntervalType[] | undefined {
    const dataFields: PMML.DataFieldType[] = this.props.dictionary.DataField.filter(df => df.name === fieldName && df.optype === "continuous");
    if (dataFields === undefined || dataFields.length != 1) {
      return undefined;
    }
    const intervals: PMML.IntervalType[] | undefined = dataFields[0].Interval;
    if (intervals === undefined) {
      return undefined;
    }
    //cxml returns an array of IntervalType with one element even if none are defined in the XML.
    //This is a hack to check the array does not contain the rogue element.
    if (intervals.length === 1 && intervals[0]._exists === false) {
      return undefined;
    }
    return intervals;
  }

  private getIntervalsMaximumRange(intervals: PMML.IntervalType[] | undefined): Range | undefined {
    if (intervals === undefined) {
      return undefined;
    }
    const min: number = intervals.map(interval => interval.leftMargin).reduce((pv, cv) => Math.min(pv, cv));
    const max: number = intervals.map(interval => interval.rightMargin).reduce((pv, cv) => Math.max(pv, cv));
    return new Range(min, max);
  }

  private getDefaultYRange(lines: Line[]): Range {
    const maxIntersect: number = Math.max(...lines.map(line => line.c));
    const defaultMaxY: number = maxIntersect * 2;
    const defaultMinY: number = -defaultMaxY;
    return new Range(defaultMinY, defaultMaxY);
  }

  private getDefaultXRange(lines: Line[], rangeY: Range): Range {
    const minGradient: number = Math.min(...lines.map(line => line.m));
    const maxIntersect: number = Math.max(...lines.map(line => line.c));
    const defaultMaxX: number = (rangeY.max - maxIntersect) / minGradient;
    const defaultMinX: number = -defaultMaxX;
    return new Range(defaultMinX, defaultMaxX);
  }

}

export { LinearRegressionViewAdaptor };
