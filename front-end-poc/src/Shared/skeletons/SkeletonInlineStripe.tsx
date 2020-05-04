import React from 'react';

type ownProps = {
    customStyle?: React.CSSProperties,
    dark?: boolean
}

const SkeletonInlineStripe = (props: ownProps) => {
    const { customStyle, dark = false } = props;
    const stripeDefaultStyle = {
        width: 200
    };
    const stripeStyle = customStyle ? Object.assign(customStyle, stripeDefaultStyle) : stripeDefaultStyle;
    let cssClasses = "skeleton__stripe skeleton__stripe--inline";
    if (dark) {
        cssClasses += " skeleton--lighter";
    }

    return (
        <span className={cssClasses} style={stripeStyle} />
    );
};

export default SkeletonInlineStripe;