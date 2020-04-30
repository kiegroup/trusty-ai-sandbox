import React from 'react';

type ownProps = {
    customStyle?: React.CSSProperties
}

const SkeletonInlineStripe = (props: ownProps) => {
    const { customStyle } = props;
    const stripeDefaultStyle = {
        width: 200
    };
    const stripeStyle = customStyle ? Object.assign(customStyle, stripeDefaultStyle) : stripeDefaultStyle;

    return (
        <span className="skeleton__stripe skeleton__stripe--inline" style={stripeStyle} />
    );
};

export default SkeletonInlineStripe;