import React from 'react';
import { LinearRegressionView, Line } from './LinearRegressionView'
import * as PMML from "./generated/www.dmg.org/PMML-4_4";

type Props = {
  model: PMML.RegressionModelType;
}

type State = {
}

class LinearRegressionHandler extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
  }

  render() {
    const modelName: string = this.props.model.modelName;
    const regressionTable: PMML.RegressionTableType = this.getRegressionTable();
    const c: number = regressionTable.intercept;

    const miningSchema: PMML.MiningSchemaType = this.props.model.MiningSchema;
    const dependentAxisTitle = miningSchema.MiningField.filter(mf => mf.usageType === "target")[0].name;

    //We can only handle one NumericPredictor
    const numbericPredictors: PMML.NumericPredictorType[] | undefined = regressionTable.NumericPredictor;
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
    const categoricalPredictors: PMML.CategoricalPredictorType[] | undefined = regressionTable.CategoricalPredictor;
    if (categoricalPredictors !== undefined) {
      categoricalPredictors.forEach(cp => {
        //cxml returns an array of CategoricalPredictorType with one element even if none are defined in the XML.
        //This is a hack to check the coefficient is defined and is a number to filter the rogue elements.
        if (!isNaN(cp.coefficient)) {
          lines.push({ m: line.m, c: line.c + cp.coefficient, title: line.title + " (" + cp.value + ")" });
        }
      });
    }

    return (
      <div>
        <LinearRegressionView modelName={modelName}
          independentAxisTitle={numbericPredictor.name}
          dependentAxisTitle={dependentAxisTitle}
          lines={lines} />
      </div>
    );
  }

  private getRegressionTable() {
    const tables: PMML.RegressionTableType[] = this.props.model.RegressionTable;
    const table: PMML.RegressionTableType = tables[0];
    return table;
  }

}

export { LinearRegressionHandler };
