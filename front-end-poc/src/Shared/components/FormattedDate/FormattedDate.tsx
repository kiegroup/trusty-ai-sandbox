import React from "react";
import { differenceInDays, format, formatDistanceToNowStrict } from "date-fns";
import { Tooltip } from "@patternfly/react-core";
import { TooltipProps } from "@patternfly/react-core/dist/js/components/Tooltip/Tooltip";

type FormattedDateProps = {
  date: string;
  preposition?: boolean;
  position?: TooltipProps["position"];
};

const FormattedDate = (props: FormattedDateProps) => {
  const { date, preposition = false, position = "auto" } = props;
  const difference = differenceInDays(new Date(date), new Date());
  const fullFormattedDate = format(new Date(date), "PPpp");
  let formattedDate;

  if (difference === 0) {
    formattedDate = `${formatDistanceToNowStrict(new Date(date))} ago`;
  } else {
    const prefix = preposition ? "on " : "";
    formattedDate = `${prefix}${format(new Date(date), "PP")}`;
  }

  return (
    <Tooltip content={fullFormattedDate} entryDelay={200} exitDelay={100} distance={5} position={position}>
      <span>{formattedDate}</span>
    </Tooltip>
  );
};

export default FormattedDate;
