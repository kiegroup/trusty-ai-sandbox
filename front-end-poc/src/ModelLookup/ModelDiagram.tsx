import React, { useEffect, useState } from 'react';
import { LinearRegressionViewer, example3 } from "@manstis/pmml-linear-model-viewer"
import { getModelDetail } from "../Shared/api/audit.api";
import { Spinner } from '@patternfly/react-core';

interface Props {
  executionId: string;
}

const ModelDiagram = (props: Props) => {

  function makeDMNEditor(xml: string): JSX.Element {
    const editorUrl = "https://kiegroup.github.io/kogito-online/?file=https://raw.githubusercontent.com/kiegroup/kogito-tooling/master/packages/online-editor/static/samples/sample.dmn#/editor/dmn";
    const kogitoIframe = () => {
      return { __html: `<iframe src=${editorUrl} data-key="${executionId}"></iframe>` };
    };
    return <><div className="model-diagram__iframe" dangerouslySetInnerHTML={kogitoIframe()} /></>;
  }

  function makePMMLEditor(xml: string): JSX.Element {
    return <><LinearRegressionViewer xml={example3} /></>;
  }

  const { executionId } = props;

  const [viewer, setViewer] = useState(<><Spinner size="xl" /></>);

  useEffect(() => {
    let didMount = true;
    getModelDetail(executionId)
      .then(response => {
        if (didMount) {
          const modelType: string = response.data[0].modelType;
          const xml: string = response.data[0].xml;

          switch (modelType) {
            case "DMN":
              setViewer(makeDMNEditor(xml));
              break;
            case "PMML":
              setViewer(makePMMLEditor(xml));
              break;
          }
        }
      })
      .catch(() => { });
    return () => {
      didMount = false;
    };
  }, [executionId]);

  return viewer;
};

export default ModelDiagram;