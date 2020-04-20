import React from 'react';
import {Grid, GridItem, Title} from "@patternfly/react-core";
import './Outcome.scss';
import outcomeList from "../Mocks/outcome-list-mock";

type itemObject = {
    label: string,
    value?: string | number,
    children?: { [key: string]: itemObject },
    list?: { [key: string]: object }[],
    impact?: boolean | number,
    highlighted?: boolean
}

const OutcomeListTitle = (props:{item:{[index: string]:itemObject}, titleProperty:string}) => {
    const {item, titleProperty} = props;
    return (
        <Title headingLevel="h5" size="lg" className={"outcome-list__item__title"}>
            {item[titleProperty].value}
        </Title>
    )
};
const OutcomeListAttributes = (props:{item:{[index: string]:itemObject}, titleProperty:string, allAttributes:boolean}) => {
    const { item, titleProperty, allAttributes } = props;
    let attributes = [];
    for (let key in item) {
        if (key !== titleProperty && item.hasOwnProperty(key)) {
            attributes.push(item[key]);
        }
    }
    if (!allAttributes) {
        attributes = attributes.filter((item) => {
            return item.highlighted
        });
    }
    return (
        <Grid>
            {attributes.map((item, index) => {
                return (
                    <React.Fragment key={`fragment-${index}`}>
                        <GridItem span={4} key={`item-label-${index}`}>{item.label}</GridItem>
                        <GridItem span={8} key={`item-value-${index}`}>{item.value}</GridItem>
                    </React.Fragment>
                  )
                }
            )}
        </Grid>
    )

};
const OutcomeList = (props:{ allAttributes: boolean }) => {
    const { allAttributes } = props || true;
    const actualList: {[index: string]:itemObject}[] = outcomeList.data.recommendedLoanProducts;
    const mainProperty = "product";
    return (
        <div className={"outcome-list"}>
            <Title headingLevel="h4" size="xl" className={"outcome-list__title"}>
                {actualList.length} Recommended Loan Products
            </Title>

            <ul className={"outcome-list__items"}>
                {
                    actualList.map((item, index) => (
                        <li className={"outcome-list__item"} key={`block-${index}`}>
                            <OutcomeListTitle item={item} titleProperty={mainProperty} />
                            <OutcomeListAttributes item={item} titleProperty={mainProperty} allAttributes={allAttributes}/>
                        </li>
                    ))
                }
            </ul>
        </div>
    );
};

export default OutcomeList;