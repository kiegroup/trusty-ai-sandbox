import { Divider, PageSection, PageSectionVariants } from "@patternfly/react-core";
import React from "react";
import { IExecutionModelResponse } from "../Audit/types";
import ModelDiagram from "./ModelDiagram";
import "./ModelLookup.scss";

interface Props {
  model: IExecutionModelResponse;
}

const ModelLookup = (props: Props) => {
  return (
    <>
      <PageSection variant={PageSectionVariants.light} style={{ paddingTop: 0, paddingBottom: 0 }}>
        <Divider />
      </PageSection>
      <PageSection variant={"light"}>
        <ModelDiagram model={props.model} />
      </PageSection>
    </>
  );
};

export default ModelLookup;
