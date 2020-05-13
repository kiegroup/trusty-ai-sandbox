import React from 'react';
import { IExecutionModelResponse } from "../Audit/types";
import { LinearRegressionViewer } from "./pmml/LinearRegressionViewer";

const DMN1_2: string = "http://www.omg.org/spec/DMN/20151101/dmn.xsd";
const PMML4_4: string = "http://www.dmg.org/PMML-4_4";

interface Props {
  model: IExecutionModelResponse;
}

function makeUnknownModel(): JSX.Element {
  return <div>Unknown model type</div>
}

function makeDMNEditor(model: IExecutionModelResponse): JSX.Element {
  const modelUrl: string = "https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
  const editorUrl = `https://kiegroup.github.io/kogito-online/?file=${modelUrl}`;
  const kogitoIframe = () => {
    return { __html: `<iframe src=${editorUrl}"></iframe>` };
  };
  return <div className="model-diagram__iframe" dangerouslySetInnerHTML={kogitoIframe()} />;
}

function makePMMLEditor(model: IExecutionModelResponse): JSX.Element {
  return <LinearRegressionViewer xml={model.model} />;
}

const DEFAULT: JSX.Element = makeUnknownModel();

const ModelDiagram = (props: Props) => {

  const { model } = props;
  const type: string = model.type;

  switch (type) {
    case DMN1_2:
      return makeDMNEditor(model);
    case PMML4_4:
      return makePMMLEditor(model);
  }

  return DEFAULT;
};

export default ModelDiagram;