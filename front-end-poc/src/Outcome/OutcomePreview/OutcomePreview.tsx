import React, { useEffect, useState } from "react";
import { Card, CardBody, CardHeader, Gallery, GalleryItem, Split, SplitItem, Title } from "@patternfly/react-core";
import FormattedValue from "../../Shared/components/FormattedValue/FormattedValue";
import { IOutcome } from "../types";
import { IItemObject, isIItemObjectArray, isIItemObjectMultiArray } from "../../InputData/types";
import { v4 as uuid } from "uuid";
import "./OutcomePreview.scss";

const OutcomeSubListItem = (props: { subListItem: IItemObject[]; compact: boolean }) => {
  const { subListItem, compact } = props;
  const [title, setTitle] = useState<IItemObject | null>(null);
  const [otherProperties, setOtherProperties] = useState<IItemObject[]>();

  useEffect(() => {
    // assuming that the first item in the array is the name of the recommendation...
    setTitle(subListItem[0]);
    if (compact) setOtherProperties(subListItem.slice(1, 3));
    else setOtherProperties(subListItem.slice(1, subListItem.length));
  }, [subListItem, compact]);

  if (compact)
    return (
      <div className="outcome-list__item">
        {title && (
          <Title headingLevel="h5" size="lg" className={"outcome-list__item__title"}>
            {title.name}: {title.value}
          </Title>
        )}
        {otherProperties &&
          otherProperties.map((item) => (
            <Split key={uuid()} className="outcome__property">
              <SplitItem className="outcome__property__name" key="property-name">
                {item.name}:
              </SplitItem>
              <SplitItem className="outcome__property__value" key="property-value">
                <FormattedValue value={item.value} />
              </SplitItem>
            </Split>
          ))}
      </div>
    );
  else
    return (
      <Card className="outcome-list__card" isHoverable={true}>
        <CardHeader>
          {title && (
            <Title headingLevel="h5" size="xl" className="outcome-list__card__title">
              {title.name}:<br />
              {title.value}
            </Title>
          )}
        </CardHeader>
        <CardBody>
          {otherProperties &&
            otherProperties.map((item) => (
              <Split key={uuid()} className="outcome__property">
                <SplitItem className="outcome__property__name" key="property-name">
                  {item.name}:
                </SplitItem>
                <SplitItem className="outcome__property__value" key="property-value">
                  <FormattedValue value={item.value} />
                </SplitItem>
              </Split>
            ))}
        </CardBody>
      </Card>
    );
};

const CardWrapper = (props: { condition: boolean; children: React.ReactNode }) => {
  const { condition, children } = props;
  if (condition)
    return (
      <Card key={uuid()}>
        <CardBody>{children}</CardBody>
      </Card>
    );
  else return <div key={uuid()}>{children}</div>;
};

const OutcomeSubList = (props: { subList: IItemObject; compact: boolean }) => {
  const { subList, compact } = props;

  if (compact)
    return (
      <div className={"outcome-list"} key={subList.name}>
        <Title headingLevel="h5" size="lg" className={"outcome-list__title"}>
          {
            <>
              {subList.components.length} {subList.name}
            </>
          }
        </Title>
        <ul className={"outcome-list__items"}>
          {subList.components.map((item, index) => (
            <OutcomeSubListItem subListItem={item as IItemObject[]} key={`recommendation-${index}`} compact={compact} />
          ))}
        </ul>
      </div>
    );
  else
    return (
      <div className="outcome-list" key={subList.name}>
        <Title headingLevel="h5" size="lg" className="outcome-list__title">
          {
            <>
              {subList.components.length} {subList.name}
            </>
          }
        </Title>
        <Gallery gutter="md" className="outcome-cards">
          {subList.components.map((item, index) => (
            <GalleryItem key={`recommendation-${index}`}>
              <OutcomeSubListItem subListItem={item as IItemObject[]} compact={compact} />
            </GalleryItem>
          ))}
        </Gallery>
      </div>
    );
};

const OutcomeComposed = (props: { outcome: IItemObject; compact: boolean }) => {
  const { outcome, compact } = props;
  let renderItems: JSX.Element[] = [];

  for (let subItem of outcome.components as IItemObject[]) {
    renderItems.push(
      <div className="outcome-item" key={subItem.name}>
        {renderOutcome(subItem, compact)}
      </div>
    );
  }
  return (
    <>
      <div className="outcome__title" key={outcome.name}>
        <span className="outcome__property__name">{outcome.name}</span>
      </div>
      <div className="outcome outcome--struct" key={outcome.name}>
        {renderItems.map((item) => item)}
      </div>
    </>
  );
};

const OutcomeProperty = (props: { property: IItemObject }) => {
  const { property } = props;

  return (
    <>
      <Split key={uuid()} className="outcome__property">
        <SplitItem className="outcome__property__name" key="property-name">
          {property.name}:
        </SplitItem>
        <SplitItem className="outcome__property__value" key="property-value">
          <FormattedValue value={property.value} />
        </SplitItem>
      </Split>
    </>
  );
};

const renderOutcome = (outcomeData: IItemObject, compact = true, rootLevel = false) => {
  let renderItems: JSX.Element[] = [];

  if (outcomeData.value !== null) {
    return (
      <CardWrapper condition={!compact && rootLevel} key={uuid()}>
        <OutcomeProperty property={outcomeData} key={outcomeData.name} />
      </CardWrapper>
    );
  }
  if (outcomeData.components.length) {
    if (isIItemObjectArray(outcomeData.components)) {
      renderItems.push(
        <CardWrapper condition={!compact && rootLevel} key={uuid()}>
          <OutcomeComposed outcome={outcomeData} key={outcomeData.name} compact={compact} />
        </CardWrapper>
      );
    } else if (isIItemObjectMultiArray(outcomeData.components)) {
      renderItems.push(<OutcomeSubList subList={outcomeData} key={outcomeData.name} compact={compact} />);
    }
  }

  return <React.Fragment key={uuid()}>{renderItems.map((item: JSX.Element) => item)}</React.Fragment>;
};

type OutcomePreviewProps = {
  outcomeData: IOutcome[] | null;
  compact?: boolean;
};

const OutcomePreview = (props: OutcomePreviewProps) => {
  const { outcomeData, compact = true } = props;
  const cssClass = compact ? "outcomes-preview" : "outcomes-preview outcomes-preview--extended";

  return (
    <div className={cssClass}>
      {outcomeData &&
        outcomeData.map((item) => (
          <div className="outcome" key={item.outcomeName}>
            {renderOutcome(item.outcomeResult, compact, true)}
          </div>
        ))}
    </div>
  );
};

export default OutcomePreview;
