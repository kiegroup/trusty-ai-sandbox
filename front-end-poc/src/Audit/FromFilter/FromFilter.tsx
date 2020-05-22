import React, { useCallback } from "react";
import DatePicker from "../../Shared/components/DatePicker/DatePicker";

type FromFilterProps = {
  fromDate: string;
  maxDate: string;
  onFromDateUpdate: (date: string) => void;
};

const FromFilter = (props: FromFilterProps) => {
  const { fromDate, maxDate, onFromDateUpdate } = props;
  const updateFromDate = useCallback((date: string) => onFromDateUpdate(date), [onFromDateUpdate]);

  return (
    <DatePicker
      fromDate={fromDate}
      maxDate={maxDate}
      value={fromDate}
      label="Starting date"
      id="audit-from-date"
      onDateUpdate={updateFromDate}
    />
  );
};

export default FromFilter;
