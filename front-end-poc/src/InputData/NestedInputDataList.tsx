import React, {useState} from 'react';
import {
    Card,
    CardBody,
    CardHeader,
    DataList,
    DataListCell,
    DataListItem,
    DataListItemCells,
    DataListItemRow, Flex, FlexItem, FlexModifiers,
    Switch,
    Title
} from "@patternfly/react-core";
import { RebalanceIcon } from '@patternfly/react-icons';
import { StickyContainer, Sticky } from 'react-sticky';
import loanProductsInput from '../Mocks/loan-products-nested-input-data';

const ItemsSubList = (props:{itemsList: { [key: string]: itemObject }}) => {
    const {itemsList} = props;
    let elements = [];
    for (let element in itemsList) {
        if (itemsList.hasOwnProperty(element)) {
            elements.push(itemsList[element]);
        }
    }
    return (
      <DataListItem aria-labelledby={""} className={"category__sublist"}>
          <DataList aria-label={""} className={"category__sublist__item"}>
              {elements.map(item => <InputValue inputLabel={item.label} inputValue={item.value} hasEffect={item.impact} key={Math.floor(Math.random() * 10000)}/>)
              }
          </DataList>
      </DataListItem>
    )
};
const CategoryLine = (props:any) => {
    const {categoryLabel} = props;
    const categoryKey = categoryLabel.replace(' ', '').toLocaleLowerCase();
    return (
        <DataListItem aria-labelledby={""} key={"category-" + categoryKey} className="category__heading">
            <DataListItemRow>
                <DataListItemCells dataListCells={[
                    <DataListCell key="primary content"><span>{categoryLabel}</span></DataListCell>
                ]}>
                </DataListItemCells>
            </DataListItemRow>
        </DataListItem>
    )
};
const InputValue = (props:any) => {
    const {inputValue, inputLabel, category, hasEffect} = props;
    const effectItemClass = (hasEffect === true) ? "input-data--affecting" : "input-data--ignored";
    const effectIconClass = (hasEffect === true) ? "input-data__icons__effect" : "input-data__icons__no-effect";
    const effectTitle = (hasEffect === true) ? "Impacting Feature" : "Not Impacting Feature";
    return (
        <DataListItem aria-labelledby={"Input columns"} key={`input-item-heading`} className={`input-data__item ${effectItemClass}`}>
            <DataListItemRow>
                <DataListItemCells dataListCells={[
                    <DataListCell key="primary content" className="input-data__wrap">
                        <span>{inputLabel}</span><span className="input-data__wrap__desc">{category}</span>
                    </DataListCell>,
                    <DataListCell key="secondary content"><span>{inputValue}</span></DataListCell>,
                    <DataListCell isIcon alignRight key="chart content" className="input-data__icons">
                        <RebalanceIcon title={effectTitle} className={effectIconClass} />
                    </DataListCell>
                ]}>
                </DataListItemCells>
            </DataListItemRow>
        </DataListItem>
    )
};

const inputData: { [key: string]: itemObject } = loanProductsInput.data;
type itemObject = {
    label: string,
    value?: string | number,
    children?: { [key: string]: itemObject },
    list?: { [key: string]: object }[],
    impact?: boolean | number
}
let itemCategory = "";
const renderItem = (item:itemObject, category?:string) => {
    if (item.hasOwnProperty('value')) {
        let key = Math.floor(Math.random() * 10000);
        return <InputValue inputLabel={item.label} inputValue={item.value} hasEffect={item.impact} key={key} />

    }
    if (item.hasOwnProperty('children')) {
        // console.table(item);
        itemCategory = (category) ? `${itemCategory} / ${category}` : item.label;
        let categoryLabel = (itemCategory.length > 0) ? `${itemCategory}` : item.label;
        let children = [];
        for (let child in item.children) {
            if (item.children.hasOwnProperty(child)) {
                children.push(item.children[child]);
            }
        }
        return (
            <StickyContainer key={Math.floor(Math.random() * 10000)}>
                <Sticky>
                    {({ style, isSticky }) => (
                        <div style={style} className={(isSticky ? 'category--sticky' : 'category')}>
                            <CategoryLine categoryLabel={categoryLabel} />
                        </div>
                    )}
                </Sticky>
                {children.map(item => renderItem(item, item.label))}
            </StickyContainer>
        )
    }
    if (item.hasOwnProperty('list')) {
        itemCategory = (category) ? `${itemCategory} / ${category}` : item.label;
        let categoryLabel = (itemCategory.length > 0) ? `${itemCategory}` : item.label;
        let listItems:any[] = [];
        if (item.list) {
            item.list.forEach((object) => {
                let itemFeatures = [];
                for (let property in object) {
                    if (object.hasOwnProperty(property)) {
                        itemFeatures.push(object[property]);
                    }
                }
                listItems.push(itemFeatures);
            });
        }

        return (
            <StickyContainer key={Math.floor(Math.random() * 10000)}>
                <Sticky>
                    {({ style, isSticky }) => (
                        <div style={style} className={(isSticky ? 'category--sticky' : 'category')}><CategoryLine categoryLabel={categoryLabel} key={Math.random()} /></div>
                    )}
                </Sticky>
                {listItems && listItems.map((item) => (
                    <ItemsSubList itemsList={item} key={Math.floor(Math.random() * 10000)} />
                    )
                )}
            </StickyContainer>
        )
    }
};
const NestedInputDataList = (props:{showOnlyAffecting?: boolean}) => {
    const {showOnlyAffecting} = props || false;
    const [showAffectingInput, setShowAffectingInput] = useState(showOnlyAffecting);

    const affectingInputChange = (isChecked:boolean) => {
        setShowAffectingInput(isChecked);
    };
    const filterClass = (showAffectingInput) ? "js-show-affecting-only" : "";
    const items = [];
    for (let item in inputData) {
        if (inputData.hasOwnProperty(item)) {
            items.push(inputData[item]);
        }
    }
    return (
            <Card>
                <CardHeader>
                    <Title headingLevel="h3" size="2xl">
                        Top Features List
                    </Title>
                </CardHeader>
                <CardBody>
                    {
                        !showOnlyAffecting &&
                        <Flex
                            breakpointMods={[
                                {modifier: FlexModifiers["align-items-flex-start"]},
                                {modifier: FlexModifiers["space-items-sm"]}]}
                        >
                            <FlexItem>
                                <RebalanceIcon style={{fontSize: 18 / 16 + "em"}}/>
                            </FlexItem>
                            <FlexItem>
                                <span>Show only features affecting the outcome</span>
                            </FlexItem>
                            <FlexItem>
                                <Switch
                                    id="affecting-features"
                                    className="input-data__affecting-switch"
                                    isChecked={showAffectingInput}
                                    onChange={affectingInputChange}
                                />
                            </FlexItem>
                        </Flex>
                    }
                    <DataList aria-label="Simple data list example" className={filterClass}>
                        {
                            items.map(item => {
                                return renderItem(item);
                            })
                        }
                    </DataList>
                </CardBody>
            </Card>
        );
};

export default NestedInputDataList;
