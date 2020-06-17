import React from "react";
import { differenceInDays, format, formatDistanceToNowStrict } from "date-fns";
import { Tooltip } from "@patternfly/react-core";

type FormattedDateProps = {
  date: string;
  preposition?: boolean;
};

const FormattedDate = (props: FormattedDateProps) => {
  const { date, preposition = false } = props;
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
    <Tooltip content={fullFormattedDate} entryDelay={200} exitDelay={100} distance={5}>
      <span>{formattedDate}</span>
    </Tooltip>
  );
};

export default FormattedDate;
