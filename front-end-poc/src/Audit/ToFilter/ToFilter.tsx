import React, { useCallback } from "react";
import DatePicker from "../../Shared/components/DatePicker/DatePicker";

type ToFilterProps = {
  toDate: string;
  minDate: string;
  onToDateUpdate: (date: string) => void;
};

const ToFilter = (props: ToFilterProps) => {
  const { toDate, minDate, onToDateUpdate } = props;
  const updateToDate = useCallback((date: string) => onToDateUpdate(date), [onToDateUpdate]);

  return (
    <DatePicker value={toDate} minDate={minDate} onDateUpdate={updateToDate} id="audit-to-date" label="Ending date" />
  );
};

export default ToFilter;
