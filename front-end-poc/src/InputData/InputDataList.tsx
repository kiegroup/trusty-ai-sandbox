import React from "react";
import {
  Card,
  CardBody,
  CardHeader,
  DataList,
  DataListCell,
  DataListItem,
  DataListItemCells,
  DataListItemRow,
  Title,
} from "@patternfly/react-core";
import FeatureDistribution from "./FeatureDistribution";
import FeatureDistributionBoxPlot from "./FeatureDistributionBoxPlot";

type inputDataProps = {
  inputData: inputElement[];
};
type inputElement = {
  inputName: string;
  inputValue: string | number | boolean;
};

const InputDataList = (props: inputDataProps) => {
  const inputList = props.inputData;
  return (
    <>
      <Card>
        <CardHeader>
          <Title headingLevel="h3" size="2xl">
            Input Data
          </Title>
        </CardHeader>
        <CardBody>
          <DataList aria-label="Simple data list example">
            <DataListItem aria-labelledby={"Input columns"} key={`input-item-heading`} className="input-list__headings">
              <DataListItemRow>
                <DataListItemCells
                  dataListCells={[
                    <DataListCell key="primary content">
                      <span>Feature</span>
                    </DataListCell>,
                    <DataListCell key="secondary content">
                      <span>Value</span>
                    </DataListCell>,
                    <DataListCell key="chart content">
                      <span>Distribution </span>
                    </DataListCell>,
                  ]}
                />
              </DataListItemRow>
            </DataListItem>
            {inputList.map((input, index) => {
              const rowHasChart = input.inputValue && typeof input.inputValue === "number";
              const rowClass = rowHasChart ? "input-list__has-chart" : "";
              return (
                <DataListItem aria-labelledby="simple-item1" key={`input-item${index}`} className={rowClass}>
                  <DataListItemRow>
                    <DataListItemCells
                      dataListCells={[
                        <DataListCell key="primary content">
                          <span>{input.inputName}</span>
                        </DataListCell>,
                        <DataListCell key="secondary content" alignRight={true}>
                          {input.inputValue}
                        </DataListCell>,
                        <DataListCell key="chart content" className="input-list__chart">
                          {rowHasChart && false && <FeatureDistribution />}
                          {rowHasChart && <FeatureDistributionBoxPlot />}
                        </DataListCell>,
                      ]}
                    />
                  </DataListItemRow>
                </DataListItem>
              );
            })}
          </DataList>
        </CardBody>
      </Card>
    </>
  );
};

export default InputDataList;
