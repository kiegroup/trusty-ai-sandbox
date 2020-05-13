import React from 'react';
import { IFullModelResponse } from "../Audit/types";
import { LinearRegressionViewer } from "./pmml/LinearRegressionViewer";

const DMN1_2: string = "http://www.omg.org/spec/DMN/20151101/dmn.xsd";
const PMML4_4: string = "http://www.dmg.org/PMML-4_4";

interface Props {
  model: IFullModelResponse;
}

function makeUnknownModel(): JSX.Element {
  return <div>Unknown model type</div>
}

function makeDMNEditor(executionId: number): JSX.Element {
  const model1Url: string = "https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
  const model2Url: string = "https://gist.githubusercontent.com/r00ta/c5077ac1f12746e356e1d4f03620ee05/raw/a3d2a865398547a18010e78dd4c1b0d76cc85d99/myMortgage.dmn";
  const editorUrl = `https://kiegroup.github.io/kogito-online/?file=${model1Url}`;
  const kogitoIframe = () => {
    return { __html: `<iframe src=${editorUrl} data-key="${executionId}"></iframe>` };
  };
  return <div className="model-diagram__iframe" dangerouslySetInnerHTML={kogitoIframe()} />;
}

function makePMMLEditor(xml: string): JSX.Element {
  return <LinearRegressionViewer xml={xml} />;
}

const DEFAULT: JSX.Element = makeUnknownModel();

const ModelDiagram = (props: Props) => {

  const { executionId, type, model } = props.model;

  switch (type) {
    case DMN1_2:
      if (executionId !== undefined) {
        return makeDMNEditor(executionId);
      }
      break;
    case PMML4_4:
      if (model !== undefined) {
        return makePMMLEditor(model);
      }
      break;
  }

  return DEFAULT;
};

export default ModelDiagram;