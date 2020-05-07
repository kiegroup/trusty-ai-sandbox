import React from "react";

type ownProps = {
  customStyle?: React.CSSProperties;
};

const SkeletonInlineStripe = (props: ownProps) => {
  const { customStyle } = props;
  const stripeDefaultStyle = {};
  const stripeStyle = customStyle ? Object.assign(customStyle, stripeDefaultStyle) : stripeDefaultStyle;
  let cssClasses = "skeleton__stripe skeleton__stripe--inline";

  return <span className={cssClasses} style={stripeStyle} />;
};

export default SkeletonInlineStripe;
