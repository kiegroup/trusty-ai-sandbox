import React, { useCallback, useEffect, useState } from "react";
import { PageSection, Stack, StackItem, Title } from "@patternfly/react-core";
import { IExecution, IExecutionModelResponse } from "../Audit/types";
import OutcomeCards from "./OutcomeCards/OutcomeCards";
import { getDecisionOutcome } from "../Shared/api/audit.api";
import { RemoteData } from "../Shared/types";
import { IOutcome } from "../Outcome/types";
import { useHistory } from "react-router-dom";
import "./DecisionDetailAlt.scss";

type DecisionDetailAltProps = {
  executionData: IExecution | null;
  model: IExecutionModelResponse;
};

const DecisionDetailAlt = (props: DecisionDetailAltProps) => {
  const { executionData, model } = props;
  const [outcomeData, setOutcomeData] = useState<RemoteData<Error, IOutcome[]>>({
    status: "NOT_ASKED",
  });
  const history = useHistory();

  console.log(model.name);

  const goToExplanation = useCallback(
    (outcomeId: string) => {
      history.push(`outcomes/${outcomeId}`);
    },
    [history]
  );

  useEffect(() => {
    let isMounted = true;
    setOutcomeData({ status: "LOADING" });
    if (executionData) {
      getDecisionOutcome(executionData.executionId)
        .then((response) => {
          if (isMounted) {
            setOutcomeData({
              status: "SUCCESS",
              data: response.data.outcomes,
            });
          }
        })
        .catch((error) => {
          setOutcomeData({ status: "FAILURE", error: error });
        });
    }
    return () => {
      isMounted = false;
    };
  }, [executionData]);

  return (
    <section className="decision-detail-view">
      <PageSection variant="default" className="decision-detail-view__section">
        <Stack hasGutter>
          <StackItem>
            <Title headingLevel="h3" size="2xl">
              Outcomes
            </Title>
          </StackItem>
          <StackItem>
            <OutcomeCards data={outcomeData} onExplanationClick={goToExplanation} />
          </StackItem>
        </Stack>
      </PageSection>
    </section>
  );
};

export default DecisionDetailAlt;
