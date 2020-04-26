import React, {useEffect, useState} from 'react';
import {IOutcome, IOutcomeResult, isIOutcomeResultArray, isOutcomeResultMultiArray} from "../types";
import {Grid, GridItem, Title} from "@patternfly/react-core";
import FormattedValue from "../../Shared/components/FormattedValue";

const OutcomeSubListItem = (props: { subListItem: IOutcomeResult[]}) => {
    const { subListItem } = props;
    const [title, setTitle] = useState<IOutcomeResult | null>(null);
    const [otherProperties, setOtherProperties] = useState<IOutcomeResult[]>();

    useEffect(() => {
        // assuming that the first item in the array is the name of the recommendation...
        setTitle(subListItem[0]);
        setOtherProperties(subListItem.slice(1, 3));
    }, [subListItem]);

    return (
        <div className="outcome-list__item">
            {title && (
                <Title headingLevel="h5" size="lg" className={"outcome-list__item__title"}>
                    {title.name}: {title.value}
                </Title>
            )}
            {otherProperties && (
                <Grid>
                    {otherProperties.map((item, index) => (
                            <React.Fragment key={`fragment-${index}`}>
                                <GridItem span={6} key={`item-label-${index}`}>{item.name}</GridItem>
                                <GridItem span={6} key={`item-value-${index}`}>
                                    <FormattedValue value={item.value} />
                                </GridItem>
                            </React.Fragment>
                        )
                    )}
                </Grid>
            )}
        </div>
    )
}

const OutcomeSubList = (props: { subList: IOutcomeResult}) => {
    const { subList } = props;
    return (
        <div className={"outcome-list"} key={subList.name}>
            <Title headingLevel="h4" size="xl" className={"outcome-list__title"}>
                {<>{subList.components.length} {subList.name}</>}
            </Title>
            <ul className={"outcome-list__items"}>
                {subList.components.map((item, index) => (
                    <OutcomeSubListItem subListItem={item as IOutcomeResult[]} key={`recommendation-${index}`}
                    />
                ))
                }
            </ul>
        </div>
    )
}

const OutcomeComposed = (props: { outcome: IOutcomeResult }) => {
    const { outcome } = props;
    let renderItems:JSX.Element[] = [];

    renderItems.push(<Title
        headingLevel="h4"
        size="xl"
        className={"outcome__title"}
        key={outcome.name}>
            {outcome.name}
    </Title>);
    for (let subItem of outcome.components as IOutcomeResult[]) {
        renderItems.push(<div className="outcome-item" key={subItem.name}>{renderOutcome(subItem)}</div>)
    }
    return (
        <div className="outcome" key={outcome.name}>
            {renderItems.map(item => item)}
        </div>
    )
}

const OutcomeProperty = (props: { property: IOutcomeResult }) => {
    const { property } = props;

    return (
        <Grid key={Math.floor(Math.random() * 10000)} className="outcome__property">
            <GridItem span={6} key={`item-label`} className="outcome-property__name">{property.name}</GridItem>
            <GridItem span={6} key={`item-value`}><FormattedValue value={property.value} /></GridItem>
        </Grid>
    )
}

const renderOutcome = (outcomeData: IOutcomeResult) => {
    let renderItems: JSX.Element[] = [];

    if (outcomeData.value !== null) {
        return (
            <OutcomeProperty property={outcomeData} key={outcomeData.name} />
        )
    }
    if (outcomeData.components.length) {
        if (isIOutcomeResultArray(outcomeData.components)) {
            renderItems.push(<OutcomeComposed outcome={outcomeData} key={outcomeData.name}/>)

        } else if (isOutcomeResultMultiArray(outcomeData.components)) {
            renderItems.push(<OutcomeSubList subList={outcomeData} key={outcomeData.name}/>)
        }
    }

    return (
        <React.Fragment key={Math.floor(Math.random() * 10000)}>
            {renderItems.map((item: JSX.Element) => item)}
        </React.Fragment>
    );
};

const OutcomePreview = (props: {outcomeData: IOutcome[] | null}) => {
    const {outcomeData} = props;

    return (
        <div className="outcomes-preview">
            {outcomeData && outcomeData.map(item => renderOutcome(item.outcomeResults))}
        </div>
    );
};

export default OutcomePreview;