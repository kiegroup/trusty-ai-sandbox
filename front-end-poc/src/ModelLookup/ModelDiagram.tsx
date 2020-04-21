import React from 'react';
import { IModelVersion } from "./types"
import { LinearRegressionViewer, example1 } from "@manstis/pmml-linear-model-viewer"

const ModelDiagram = (props: { selectedVersion: IModelVersion }) => {
  const { selectedVersion } = props;
  const editorUrl = "https://kiegroup.github.io/kogito-online/?file=https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
  const kogitoIframe = () => {
    return { __html: `<iframe src=${editorUrl} data-key="${selectedVersion.version}"></iframe>` };
  };

  //This will determine the correct view to return based upon model type (to be in IModelVersion) 
  //but for now it simply shows a mocked DMN editor _and_ a mocked PMML Linear Regression model viewer
  return (
    <div className="model-diagram">
      <div className="model-diagram__iframe" dangerouslySetInnerHTML={kogitoIframe()} />
      <LinearRegressionViewer xml={example1} />
    </div>
  );
};

export default ModelDiagram;