import React, { useEffect } from 'react';
import ReactDOM from 'react-dom';
import { LinearRegressionViewer, example3 } from "@manstis/pmml-linear-model-viewer"
import { getModelDetail } from "../Shared/api/audit.api";

interface Props {
  id: string;
}

const ModelDiagram = (props: Props) => {

  function embedDMNEditor(xml: string) {
    const editorUrl = "https://kiegroup.github.io/kogito-online/?file=https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
    const kogitoIframe = () => {
      return { __html: `<iframe src=${editorUrl} data-key="${id}"></iframe>` };
    };
    ReactDOM.render(
      <div className="model-diagram__iframe" dangerouslySetInnerHTML={kogitoIframe()} />,
      document.getElementById("model-holder")
    );
  }

  function embedPMMLEditor(xml: string) {
    ReactDOM.render(
      <LinearRegressionViewer xml={example3} />,
      document.getElementById("model-holder")
    );
  }

  const { id } = props;

  useEffect(() => {
    let didMount = true;
    getModelDetail(id)
      .then(response => {
        if (didMount) {
          const modelType: string = response.data[0].modelType;
          const xml: string = response.data[0].xml;

          switch (modelType) {
            case "DMN":
              embedDMNEditor(xml);
              break;
            case "PMML":
              embedPMMLEditor(xml);
              break;
          }
        }
      })
      .catch(() => { });
    return () => {
      didMount = false;
    };
  }, [id]);

  return (
    <div id="model-holder" className="model-diagram" />
  );
};

export default ModelDiagram;