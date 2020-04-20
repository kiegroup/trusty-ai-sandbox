import React from 'react';
import ReactDOM from 'react-dom'
import { IModelVersion } from "./types"
import {example1, unmarshall, LinearRegressionHandler} from "@manstis/pmml-linear-model-viewer"
import * as PMML from "@manstis/pmml-linear-model-viewer/src/generated/www.dmg.org/PMML-4_4"

const ModelDiagram = (props: { selectedVersion: IModelVersion }) => {
    const { selectedVersion } = props;
    const editorUrl = "https://kiegroup.github.io/kogito-online/?file=https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
    const kogitoIframe = () => {
        return { __html: `<iframe src=${editorUrl} data-key="${selectedVersion.version}"></iframe>` };
    };

    unmarshall(example1).then((doc: PMML.document) => {
        if (doc.PMML.RegressionModel !== undefined) {
          if (doc.PMML.RegressionModel[0] !== undefined) {
            ReactDOM.render(
              <React.StrictMode>
                <LinearRegressionHandler model={doc.PMML.RegressionModel[0]} />
              </React.StrictMode>,
              document.getElementById("holder")
            );
          }
        };
      });

    return (
        <div className="model-diagram">
            <div className="model-diagram__iframe" dangerouslySetInnerHTML={kogitoIframe()} />
            <div id="holder"/>
        </div>
    );
};

export default ModelDiagram;