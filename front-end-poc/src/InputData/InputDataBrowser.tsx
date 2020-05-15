import React, { useEffect, useState } from "react";
import {
  Button,
  DataList,
  DataListCell,
  DataListItem,
  DataListItemCells,
  DataListItemRow,
  Grid,
  GridItem,
  Split,
  SplitItem,
} from "@patternfly/react-core";

import "./inputDataBrowser.scss";
import FeatureDistributionBoxPlot from "./FeatureDistributionBoxPlot";
import FeatureDistributionStackedChart from "./FeatureDistributionStackedChart";
import SkeletonStripes from "../Shared/skeletons/SkeletonStripes";
import SkeletonDataList from "../Shared/skeletons/SkeletonDataList";
import { IInputRow, IItemObject, isIItemObjectArray, isIItemObjectMultiArray } from "./types";
import FormattedValue from "../Shared/components/FormattedValue/FormattedValue";
import { v4 as uuid } from "uuid";

const ItemsSubList = (props: { itemsList: IItemObject[] }) => {
  const { itemsList } = props;

  return (
    <DataListItem aria-labelledby="" className={"category__sublist"}>
      <DataList aria-label="" className={"category__sublist__item"}>
        {itemsList.map((item) => (
          <InputValue
            inputLabel={item.name}
            inputValue={item.value}
            hasEffect={item.impact}
            score={item.score}
            key={item.name}
            category={itemCategory}
          />
        ))}
      </DataList>
    </DataListItem>
  );
};
const CategoryLine = (props: { categoryLabel: string }) => {
  const { categoryLabel } = props;
  const categoryKey = categoryLabel.replace(" ", "").toLocaleLowerCase();
  return (
    <DataListItem aria-labelledby={categoryLabel} key={"category-" + categoryKey} className="category__heading">
      <DataListItemRow>
        <DataListItemCells
          dataListCells={[
            <DataListCell key={categoryLabel}>
              <span>{categoryLabel}</span>
            </DataListCell>,
          ]}
        />
      </DataListItemRow>
    </DataListItem>
  );
};
const InputValue = (props: IInputRow) => {
  const { inputValue, inputLabel, category, hasEffect } = props;
  const effectItemClass = hasEffect === true ? "input-data--affecting" : "input-data--ignored";
  //const effectIconClass = (hasEffect === true) ? "input-data__icons__effect" : "input-data__icons__no-effect";
  //const effectTitle = (hasEffect === true) ? "Impacting Feature" : "Not Impacting Feature";
  const dataListCells = [];
  dataListCells.push(
    <DataListCell width={2} key="primary content" className="input-data__wrap">
      <span>{inputLabel}</span>
      <span className="input-data__wrap__desc">{category}</span>
    </DataListCell>
  );
  dataListCells.push(
    <DataListCell width={2} key="secondary content">
      <span>
        <FormattedValue value={inputValue} />
      </span>
    </DataListCell>
  );

  if (typeof inputValue === "number") {
    const mean = Math.floor((inputValue - Math.floor((inputValue / 4) * Math.random())) * 100) / 100;
    const stdMean = Math.floor(Math.floor((inputValue / 12) * Math.random()) * 100) / 100;
    const high = Math.floor(inputValue + Math.floor(inputValue * Math.random()) * 100) / 100;
    const avg = Math.floor((inputValue - Math.floor((inputValue / 2) * Math.random())) * 100) / 100;
    dataListCells.push(
      <DataListCell width={5} key="dist 5" style={{ paddingTop: 0 }}>
        <Grid className="input-browser__distribution">
          <GridItem span={2} className="input-data__wrap">
            <span>{mean}</span>
            <span className="input-data__wrap__desc">Mean</span>
          </GridItem>
          <GridItem span={2} className="input-data__wrap">
            <span>{stdMean}</span>
            <span className="input-data__wrap__desc">Std Mean</span>
          </GridItem>
          <GridItem span={2} className="input-data__wrap">
            <span>{high}</span>
            <span className="input-data__wrap__desc">High</span>
          </GridItem>
          <GridItem span={2} className="input-data__wrap">
            <span>{avg}</span>
            <span className="input-data__wrap__desc">Avg</span>
          </GridItem>
          <GridItem span={4} className="input-data__wrap input-browser__distribution__chart">
            <FeatureDistributionBoxPlot />
          </GridItem>
        </Grid>
      </DataListCell>
    );
  } else if (typeof inputValue === "string") {
    dataListCells.push(
      <DataListCell width={5} key="dist 5" style={{ paddingTop: 0 }}>
        <Grid className="input-browser__distribution">
          <GridItem span={2} className="input-data__wrap">
            <span>15</span>
            <span className="input-data__wrap__desc">Unique</span>
          </GridItem>
          <GridItem span={4} className="input-data__wrap">
            <span>Some Long Value</span>
            <span className="input-data__wrap__desc">Top</span>
          </GridItem>
          <GridItem span={2} className="input-data__wrap">
            <span>154</span>
            <span className="input-data__wrap__desc">Top Freq</span>
          </GridItem>
          <GridItem span={4} className="input-data__wrap input-browser__distribution__chart">
            <FeatureDistributionStackedChart />
          </GridItem>
        </Grid>
      </DataListCell>
    );
  } else {
    dataListCells.push(
      <DataListCell width={5} key="dist 5">
        <span>
          <em>No data available</em>
        </span>
      </DataListCell>
    );
  }
  return (
    <DataListItem
      aria-labelledby={"Input columns"}
      key={`input-item-heading`}
      className={`input-data__item ${effectItemClass}`}>
      <DataListItemRow>
        <DataListItemCells dataListCells={dataListCells} />
      </DataListItemRow>
    </DataListItem>
  );
};

let itemCategory = "";

const renderItem = (item: IItemObject, category?: string): JSX.Element => {
  let renderItems: JSX.Element[] = [];

  if (item.value !== null) {
    return (
      <InputValue
        inputLabel={item.name}
        inputValue={item.value}
        hasEffect={item.impact}
        score={item.score}
        category={itemCategory}
        key={item.name}
      />
    );
  }

  if (item.components.length) {
    itemCategory = category ? `${itemCategory} / ${category}` : item.name;
    let categoryLabel = itemCategory.length > 0 ? `${itemCategory}` : item.name;

    if (item.components) {
      if (isIItemObjectArray(item.components)) {
        for (let subItem of item.components) {
          renderItems.push(renderItem(subItem, subItem.name));
        }
      } else if (isIItemObjectMultiArray(item.components)) {
        for (let subItem of item.components) {
          renderItems.push(<ItemsSubList itemsList={subItem} key={uuid()} />);
        }
      }
      return (
        <React.Fragment key={categoryLabel}>
          <div className="category">
            <CategoryLine categoryLabel={categoryLabel} key={`category-${categoryLabel}`} />
          </div>
          {renderItems.map((item: JSX.Element) => item)}
        </React.Fragment>
      );
    }
  }
  return <></>;
};

const InputDataBrowser = (props: { inputData: IItemObject[] | null }) => {
  const { inputData } = props;
  const [inputs, setInputs] = useState<IItemObject[] | null>(null);
  const [categories, setCategories] = useState<string[]>([]);
  const [viewSection, setViewSection] = useState<number>(0);

  const handleSectionSwitch = (index: number) => {
    setViewSection(index);
  };

  useEffect(() => {
    if (inputData) {
      const items: IItemObject[] = [];
      const categories = [];
      const rootSection: IItemObject = {
        name: "Root",
        typeRef: "root",
        value: null,
        components: [],
      };
      for (let item of inputData) {
        if (item.value) {
          // collecting inputs with values at root level (not containing components)
          rootSection.components!.push(item);
        } else {
          items.push(item);
          categories.push(item.name);
        }
      }
      if (rootSection.components!.length) {
        // if the root section as something inside it, than add the root section as first one
        items.unshift(rootSection);
        categories.unshift("Root");
      }
      setInputs(items);
      setCategories(categories);
      // open the fist section as default
      setViewSection(0);
    }
  }, [inputData]);

  return (
    <div className="input-browser">
      <div className="input-browser__section-list">
        {!inputData && <SkeletonStripes stripesNumber={6} stripesWidth={100} stripesHeight={1.5} />}
        {inputData && (
          <Split>
            <SplitItem>
              <span className="input-browser__section-list__label">Browse Sections</span>
            </SplitItem>
            <SplitItem>
              {categories.map((item, index) => (
                <Button
                  type={"button"}
                  variant={index === viewSection ? "primary" : "control"}
                  isActive={index === viewSection}
                  key={`section-${index}`}
                  onClick={() => handleSectionSwitch(index)}>
                  {item}
                </Button>
              ))}
            </SplitItem>
          </Split>
        )}
      </div>
      {!inputData && <SkeletonDataList rowsNumber={4} colsNumber={6} hasHeader={true} />}
      {inputData && (
        <DataList aria-label="Input Data">
          <DataListItem aria-labelledby="header" key="header" className="input-browser__header">
            <DataListItemRow>
              <DataListItemCells
                dataListCells={[
                  <DataListCell width={2} key="Input Data">
                    <span>Input Data</span>
                  </DataListCell>,
                  <DataListCell width={2} key="Value">
                    <span>Value</span>
                  </DataListCell>,
                  <DataListCell width={5} key="Distribution">
                    <span>Distribution</span>
                  </DataListCell>,
                ]}
              />
            </DataListItemRow>
          </DataListItem>
          {inputs && renderItem(inputs[viewSection])}
        </DataList>
      )}
    </div>
  );
};

export default InputDataBrowser;
