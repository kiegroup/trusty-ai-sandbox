import React from 'react';
import { LinearRegressionViewer } from "./pmml/LinearRegressionViewer";

interface Props {
  executionId?: string;
  modelType?: string;
  xml?: string;
}

function makeUnknownModel(): JSX.Element {
  return <div>Unknown model type</div>
}

function makeDMNEditor(executionId: string): JSX.Element {
  const editorUrl = "https://kiegroup.github.io/kogito-online/?file=https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
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

  const { executionId = undefined, modelType = undefined, xml = undefined } = props;

  switch (modelType) {
    case "DMN":
      if (executionId !== undefined) {
        return makeDMNEditor(executionId as string);
      }
      break;
    case "PMML":
      if (xml !== undefined) {
        return makePMMLEditor(xml as string);
      }
      break;
  }

  return DEFAULT;
};

export default ModelDiagram;