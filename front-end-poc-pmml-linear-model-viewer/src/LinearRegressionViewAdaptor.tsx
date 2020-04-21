import React from 'react';
import * as PMML from "./generated/www.dmg.org/PMML-4_4";
import { Line, LinearRegressionView } from './LinearRegressionView';

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
    const model: PMML.RegressionModelType | undefined = this.getRegressionModel();
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

    const modelName: string = model.modelName;
    const c: number = table.intercept;

    const miningSchema: PMML.MiningSchemaType = model.MiningSchema;
    const dependentAxisTitle = miningSchema.MiningField.filter(mf => mf.usageType === "target")[0].name;

    //We can only handle one NumericPredictor
    const numbericPredictors: PMML.NumericPredictorType[] | undefined = table.NumericPredictor;
    if (numbericPredictors === undefined) {
      return (
        <div>No NumericPredictorType</div>
      );
    }
    if (numbericPredictors.length > 1) {
      return (
        <div>Too many NumericPredictorTypes</div>
      );
    }

    const numbericPredictor: PMML.NumericPredictorType = numbericPredictors[0];
    const line: Line = { m: numbericPredictor.coefficient, c: c, title: "base" };
    const lines: Line[] = new Array<Line>(line);

    //We need to duplicate the line for each CategoricalPredictor
    const categoricalPredictors: PMML.CategoricalPredictorType[] | undefined = table.CategoricalPredictor;
    if (categoricalPredictors !== undefined) {
      categoricalPredictors.forEach(cp => {
        //cxml returns an array of CategoricalPredictorType with one element even if none are defined in the XML.
        //This is a hack to check the coefficient is defined and is a number to filter the rogue elements.
        if (!isNaN(cp.coefficient)) {
          lines.push({ m: line.m, c: line.c + cp.coefficient, title: line.title + " (" + cp.value + ")" });
        }
      });
    }

    const legendData: any = [];
    lines.forEach(line => {
      legendData.push({ name: line.title });
    });

    const maxIntersect: number = Math.max(...lines.map(line => line.c));
    const maxDomainY: number = maxIntersect * 2;
    const minDomainY: number = -maxDomainY;

    const minGradient: number = Math.min(...lines.map(line => line.m));
    const maxDomainX: number = (maxDomainY - maxIntersect) / minGradient;
    const minDomainX: number = -maxDomainX;

    return (
      <div>
        <LinearRegressionView modelName={modelName}
          independentAxisTitle={numbericPredictor.name}
          dependentAxisTitle={dependentAxisTitle}
          lines={lines}
          rangeX={{ min: minDomainX, max: maxDomainX }}
          rangeY={{ min: minDomainY, max: maxDomainY }}
        />
      </div>
    );
  }

  private getRegressionModel(): PMML.RegressionModelType | undefined {
    const models: PMML.RegressionModelType[] = this.props.model;
    if (models === undefined || models.length > 1) {
      return undefined;
    }
    return models[0];
  }

  private getRegressionTable(model: PMML.RegressionModelType): PMML.RegressionTableType | undefined {
    const tables: PMML.RegressionTableType[] = model.RegressionTable;
    if (tables === undefined || tables.length > 1) {
      return undefined;
    }
    return tables[0];
  }

}

export { LinearRegressionViewAdaptor };
