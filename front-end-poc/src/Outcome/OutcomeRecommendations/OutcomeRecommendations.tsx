import React, {useEffect, useState} from 'react';
import {Grid, GridItem, Title} from "@patternfly/react-core";
import {IOutcomeResult, isOutcomeResultMultiArray} from "../types";

const OutcomeListTitle = (props: {mainProperty: IOutcomeResult}) => {
    const { mainProperty } = props;
    return (
        <Title headingLevel="h5" size="lg" className={"outcome-list__item__title"}>
            {mainProperty.name}: {mainProperty.value}
        </Title>
    )
};

const OutcomeListAttributes = (props: {itemList: IOutcomeResult[] }) => {
    const { itemList } = props;
    return (
        <Grid>
            {itemList.map((item, index) => (
                    <React.Fragment key={`fragment-${index}`}>
                        <GridItem span={4} key={`item-label-${index}`}>{item.name}</GridItem>
                        <GridItem span={8} key={`item-value-${index}`}>{item.value}</GridItem>
                    </React.Fragment>
                )
            )}
        </Grid>
    )

};

const OutcomeRecommendation = (props: { outcome: IOutcomeResult[] }) => {
    const { outcome } = props;
    const [title, setTitle] = useState<IOutcomeResult | null>(null);
    const [otherProperties, setOtherProperties] = useState<IOutcomeResult[]>();

    useEffect(() => {
        // assuming that first property is the name of the recommendation...
        setTitle(outcome[0]);
        setOtherProperties(outcome.slice(1, 3));
    }, [outcome])

    return (
        <div className="outcome-list__item">
            {title && <OutcomeListTitle mainProperty={title} />}
            {otherProperties &&  <OutcomeListAttributes itemList={otherProperties}/>}
        </div>
    )
}

const OutcomeRecommendations = (props: {topDecision: IOutcomeResult}) => {
    const {topDecision} = props;
    const [recommendations, setRecommendations] = useState<IOutcomeResult[][] | null>();

    useEffect(() => {
        if (topDecision && isOutcomeResultMultiArray(topDecision.components)) {
            setRecommendations(topDecision.components);
        }
    }, [topDecision])

    return (
        <div className={"outcome-list"}>
            <Title headingLevel="h4" size="xl" className={"outcome-list__title"}>
                {topDecision && recommendations && <>{recommendations.length} {topDecision.name}</>}
            </Title>
            <ul className={"outcome-list__items"}>
                {recommendations && recommendations.map((item, index) => (
                        <OutcomeRecommendation outcome={item} key={`recommendation-${index}`}
                        />
                    ))
                }
            </ul>
        </div>
    );
};

export default OutcomeRecommendations;