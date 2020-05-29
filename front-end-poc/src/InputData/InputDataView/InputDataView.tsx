import React, { useEffect, useState } from "react";
import InputDataBrowser from "../InputDataBrowser/InputDataBrowser";
import { PageSection } from "@patternfly/react-core";
import { getDecisionInput } from "../../Shared/api/audit.api";
import { useParams } from "react-router-dom";
import { IExecutionRouteParams } from "../../Audit/types";

const InputDataView = () => {
  const { executionId } = useParams<IExecutionRouteParams>();
  const [inputData, setInputData] = useState(null);

  useEffect(() => {
    let isMounted = true;
    getDecisionInput(executionId)
      .then((response) => {
        if (isMounted) {
          setInputData(response.data.inputs);
        }
      })
      .catch(() => {});
    return () => {
      isMounted = false;
    };
  }, [executionId]);

  return (
    <PageSection>
      <InputDataBrowser inputData={inputData} />
    </PageSection>
  );
};

export default InputDataView;
