import React from 'react';
import {Flex, FlexItem} from "@patternfly/react-core";

type ownProps = {
    stripesNumber: number,
    stripesWidth: number,
    stripesHeight: number
}

const SkeletonStripes = (props: ownProps) => {
    const { stripesNumber, stripesWidth, stripesHeight } = props;
    const stripes = [];
    const stripeStyle = {
        width: stripesWidth,
        height: stripesHeight + 'em'
    }
    for (let i = 0; i < stripesNumber; i ++) {
        stripes.push((
           <FlexItem key={`skeleton-${i}`}>
               <span className="skeleton__stripe" style={stripeStyle} />
           </FlexItem>
        ));
    }
    return (
        <Flex className='skeleton skeleton--padded'>
            {stripes}
        </Flex>
    );
};

export default SkeletonStripes;