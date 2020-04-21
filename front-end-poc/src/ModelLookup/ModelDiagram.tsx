import React from 'react';
import ReactDOM from 'react-dom'
import { IModelVersion } from "./types"
import { unmarshall, LinearRegressionHandler, PMMLDocument, example1 } from "@manstis/pmml-linear-model-viewer"

const ModelDiagram = (props: { selectedVersion: IModelVersion }) => {
  const { selectedVersion } = props;
  const editorUrl = "https://kiegroup.github.io/kogito-online/?file=https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
  const kogitoIframe = () => {
    return { __html: `<iframe src=${editorUrl} data-key="${selectedVersion.version}"></iframe>` };
  };

  unmarshall(example1).then((doc: PMMLDocument) => {
    if (doc.PMML.RegressionModel !== undefined) {
      if (doc.PMML.RegressionModel[0] !== undefined) {
        ReactDOM.render(
          <React.StrictMode>
            <LinearRegressionHandler dictionary={doc.PMML.DataDictionary} model={doc.PMML.RegressionModel} />
          </React.StrictMode>,
          document.getElementById("holder")
        );
      }
    };
  });

  return (
    <div className="model-diagram">
      <div className="model-diagram__iframe" dangerouslySetInnerHTML={kogitoIframe()} />
      <div id="holder" />
    </div>
  );
};

export default ModelDiagram;