import React from "react";
import { Flex, FlexItem } from "@patternfly/react-core";

type ownProps = {
  stripesNumber: number;
  stripesWidth: number;
  stripesHeight: number;
  isPadded?: boolean;
};

const SkeletonStripes = (props: ownProps) => {
  const { stripesNumber, stripesWidth, stripesHeight, isPadded = true } = props;
  const stripes = [];
  const stripeStyle = {
    width: stripesWidth,
    height: stripesHeight + "em",
  };
  const className = isPadded ? "skeleton skeleton--padded" : "skeleton";
  for (let i = 0; i < stripesNumber; i++) {
    stripes.push(
      <FlexItem key={`skeleton-${i}`}>
        <span className="skeleton__stripe" style={stripeStyle} />
      </FlexItem>
    );
  }
  return <Flex className={className}>{stripes}</Flex>;
};

export default SkeletonStripes;
