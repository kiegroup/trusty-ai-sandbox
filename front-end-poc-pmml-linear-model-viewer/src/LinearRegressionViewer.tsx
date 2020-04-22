import React from "react";
import ReactDOM from "react-dom";
import { document as PMMLDocument } from "./generated/www.dmg.org/PMML-4_4";
import { LinearRegressionViewAdaptor } from "./LinearRegressionViewAdaptor";
import { withCXML as unmarshal } from "./unmarshall/unmarshaller";

type Props = {
  xml: string
}

const LinearRegressionViewer = (props: Props) => {

  unmarshal(props.xml).then((doc: PMMLDocument) => {
    if (doc.PMML.RegressionModel !== undefined) {
      if (doc.PMML.RegressionModel[0] !== undefined) {
        ReactDOM.render(
          <React.StrictMode>
            <LinearRegressionViewAdaptor dictionary={doc.PMML.DataDictionary} model={doc.PMML.RegressionModel} />
          </React.StrictMode>,
          document.getElementById("holder")
        );
      }
    };
  });

  return (
    <div id="holder" />
  );
}

export { LinearRegressionViewer };

