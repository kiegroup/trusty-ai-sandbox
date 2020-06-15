import React, { useEffect, useState } from "react";
import {
  Button,
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Gallery,
  GalleryItem,
  Split,
  SplitItem,
  Title,
} from "@patternfly/react-core";
import { v4 as uuid } from "uuid";
import FormattedValue from "../../Shared/components/FormattedValue/FormattedValue";
import EvaluationStatus from "../../Explanation/EvaluationStatus/EvaluationStatus";
import { LongArrowAltRightIcon } from "@patternfly/react-icons";
import { RemoteData } from "../../Shared/types";
import { IItemObject, isIItemObjectArray, isIItemObjectMultiArray } from "../../InputData/types";
import { evaluationStatusStrings, IOutcome } from "../../Outcome/types";
import "./OutcomeCards.scss";

type OutcomeCardsProps = {
  data: RemoteData<Error, IOutcome[]>;
  onExplanationClick: (outcomeId: string) => void;
};

const OutcomeCards = (props: OutcomeCardsProps) => {
  const { data, onExplanationClick } = props;

  return (
    <>
      {data.status === "SUCCESS" && (
        <Gallery className="outcome-cards" hasGutter>
          {data.data.map((item) =>
            renderOutcome(
              item.outcomeResult,
              item.outcomeName,
              false,
              true,
              item.evaluationStatus,
              item.outcomeId,
              onExplanationClick
            )
          )}
        </Gallery>
      )}
    </>
  );
};

export default OutcomeCards;

const renderOutcome = (
  outcomeData: IItemObject,
  name: string,
  compact = true,
  rootLevel = false,
  evaluationStatus?: evaluationStatusStrings,
  outcomeId?: string,
  onExplanationClick?: (outcomeId: string) => void
) => {
  let renderItems: JSX.Element[] = [];

  if (rootLevel && evaluationStatus !== "SUCCEEDED") {
    return (
      <CardWrapper
        condition={!compact && rootLevel}
        key={uuid()}
        name={name}
        evaluationStatus={evaluationStatus}
        outcomeId={outcomeId}
        onExplanationClick={onExplanationClick}>
        <span>There were problems</span>
      </CardWrapper>
    );
  }

  if (outcomeData.value !== null) {
    return (
      <CardWrapper
        condition={!compact && rootLevel}
        key={uuid()}
        name={name}
        evaluationStatus={evaluationStatus}
        outcomeId={outcomeId}
        onExplanationClick={onExplanationClick}>
        <OutcomeProperty property={outcomeData} key={outcomeData.name} hidePropertyName={rootLevel} />
      </CardWrapper>
    );
  }
  if (outcomeData.components.length) {
    if (isIItemObjectArray(outcomeData.components)) {
      renderItems.push(
        <CardWrapper
          condition={!compact && rootLevel}
          key={uuid()}
          name={name}
          evaluationStatus={evaluationStatus}
          outcomeId={outcomeId}
          onExplanationClick={onExplanationClick}>
          <OutcomeComposed outcome={outcomeData} key={outcomeData.name} compact={compact} name={name} />
        </CardWrapper>
      );
    } else if (isIItemObjectMultiArray(outcomeData.components)) {
      renderItems.push(<OutcomeSubList subList={outcomeData} key={name} compact={compact} />);
    }
  }

  return <React.Fragment key={uuid()}>{renderItems.map((item: JSX.Element) => item)}</React.Fragment>;
};

const CardWrapper = (props: {
  condition: boolean;
  children: React.ReactNode;
  name?: string;
  evaluationStatus?: evaluationStatusStrings;
  outcomeId?: string;
  onExplanationClick?: (outcomeId: string) => void;
}) => {
  const { condition, children, name, evaluationStatus, outcomeId, onExplanationClick } = props;
  if (condition)
    return (
      <GalleryItem key={uuid()}>
        <Card className="outcome-cards__card" isHoverable>
          <CardHeader>
            <Title headingLevel="h4" size="xl">
              {name}
            </Title>
          </CardHeader>
          <CardBody>{children}</CardBody>
          <CardFooter>
            <Split>
              {evaluationStatus !== undefined && (
                <SplitItem isFilled>
                  <EvaluationStatus status={evaluationStatus} />
                </SplitItem>
              )}
              {outcomeId && onExplanationClick && (
                <SplitItem>
                  <Button
                    variant="link"
                    isInline
                    className="outcome-cards__card__explanation-link"
                    onClick={() => {
                      onExplanationClick(outcomeId);
                    }}>
                    View Details <LongArrowAltRightIcon />
                  </Button>
                </SplitItem>
              )}
            </Split>
          </CardFooter>
        </Card>
      </GalleryItem>
    );
  else return <div key={uuid()}>{children}</div>;
};
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
      <>
        {title !== null && (
          <CardWrapper condition={true} key={uuid()} name={title.value as string}>
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
          </CardWrapper>
        )}
      </>
    );
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
      <>
        {subList.components.map((item) => (
          <OutcomeSubListItem key={uuid()} subListItem={item as IItemObject[]} compact={compact} />
        ))}
      </>
    );
};

const OutcomeComposed = (props: { outcome: IItemObject; compact: boolean; name: string }) => {
  const { outcome, compact, name } = props;
  let renderItems: JSX.Element[] = [];

  for (let subItem of outcome.components as IItemObject[]) {
    renderItems.push(
      <div className="outcome-item" key={subItem.name}>
        {renderOutcome(subItem, name, compact)}
      </div>
    );
  }
  return (
    <div className="outcome outcome--struct" key={outcome.name}>
      {renderItems.map((item) => item)}
    </div>
  );
};

const OutcomeProperty = (props: { property: IItemObject; hidePropertyName?: boolean }) => {
  const { property, hidePropertyName = false } = props;

  return (
    <>
      <Split key={uuid()} className="outcome__property">
        <SplitItem className="outcome__property__name" key="property-name">
          {hidePropertyName ? "Result" : property.name}:
        </SplitItem>
        <SplitItem className="outcome__property__value" key="property-value">
          <FormattedValue value={property.value} />
        </SplitItem>
      </Split>
    </>
  );
};
