import React from "react";
import "./DataWithLabel.scss";

type DataWithLabelProps = {
  label: string;
  children: React.ReactNode;
};

const DataWithLabel = (props: DataWithLabelProps) => {
  const { label, children } = props;
  return (
    <div className="data-with-label">
      <label className={"data-with-label__label"}>{label}</label>
      <span>{children}</span>
    </div>
  );
};

export default DataWithLabel;
