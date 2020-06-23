import React from "react";
import { PageSection, PageSectionVariants } from "@patternfly/react-core";
import ExecutionHeader from "../../Audit/ExecutionHeader/ExecutionHeader";
import { Link, useParams } from "react-router-dom";
import { LongArrowAltLeftIcon } from "@patternfly/react-icons";

import useExecutionInfo from "../../Shared/hooks/useExecutionInfo";
import { ExecutionType } from "../../Shared/api/audit.api";
import { IOutcomeRouteParams } from "../../Audit/types";
import ExplanationView from "../../Explanation/ExplanationView/ExplanationView";

const OutcomeDetail = () => {
  const { executionId, executionType } = useParams<IOutcomeRouteParams>();
  const execution = useExecutionInfo(executionType as ExecutionType, executionId);

  return (
    <>
      <PageSection variant={PageSectionVariants.light}>
        <ExecutionHeader execution={execution} title="Outcome Detail" />
        <nav style={{ marginTop: "var(--pf-global--spacer--md)" }}>
          <Link to="../outcomes">
            <LongArrowAltLeftIcon /> Back to Outcome
          </Link>
        </nav>
      </PageSection>
      <ExplanationView />
    </>
  );
};

export default OutcomeDetail;
