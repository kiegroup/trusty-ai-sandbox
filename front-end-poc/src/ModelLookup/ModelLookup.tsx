import { Divider, PageSection, PageSectionVariants } from "@patternfly/react-core";
import React from "react";
import { IExecutionModelResponse } from "../Audit/types";
import ModelDiagram from "./ModelDiagram";
import "./ModelLookup.scss";

type ModelLookupProps = {
  model: IExecutionModelResponse
}

const ModelLookup = (props: ModelLookupProps) => {
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
