import React from "react";
import { evaluationStatus, evaluationStatusStrings } from "../../Outcome/types";
import {
  CheckCircleIcon,
  HourglassHalfIcon,
  MinusCircleIcon,
  ErrorCircleOIcon,
  FastForwardIcon,
} from "@patternfly/react-icons";
import "./EvaluationStatus.scss";

type EvaluationStatusProps = {
  status: evaluationStatusStrings;
};

const EvaluationStatus = (props: EvaluationStatusProps) => {
  const { status } = props;
  const label = evaluationStatus[status];
  let icon;
  switch (status) {
    case "EVALUATING":
      icon = <HourglassHalfIcon className="evaluation-status-badge--pending" />;
      break;
    case "FAILED":
      icon = <ErrorCircleOIcon className="evaluation-status-badge--failure" />;
      break;
    case "SKIPPED":
      icon = <FastForwardIcon className="evaluation-status-badge--failure" />;
      break;
    case "NOT_EVALUATED":
      icon = <MinusCircleIcon className="evaluation-status-badge--failure" />;
      break;
    case "SUCCEEDED":
      icon = <CheckCircleIcon className="evaluation-status-badge--success" />;
      break;
  }
  return (
    <span>
      {icon}
      <span>{label}</span>
    </span>
  );
};

export default EvaluationStatus;
