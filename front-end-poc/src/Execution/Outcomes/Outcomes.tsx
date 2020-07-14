import React from "react";
import { IOutcome } from "../../Outcome/types";
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
import EvaluationStatus from "../../Explanation/EvaluationStatus/EvaluationStatus";
import { LongArrowAltRightIcon } from "@patternfly/react-icons";
import { IItemObject, isIItemObjectArray, isIItemObjectMultiArray } from "../../InputData/types";
import "./Outcomes.scss";
import FormattedValue from "../../Shared/components/FormattedValue/FormattedValue";

type OutcomesProps = {
  outcomes: IOutcome[];
  onExplanationClick: (outcomeId: string) => void;
  listView?: boolean;
};

const Outcomes = (props: OutcomesProps) => {
  const { outcomes, onExplanationClick, listView = false } = props;

  if (listView) {
    return (
      <>
        {outcomes.length && (
          <Gallery className="outcome-cards" hasGutter>
            {outcomes.map((item) => renderCard(item, onExplanationClick))}
          </Gallery>
        )}
      </>
    );
  }

  return (
    <>
      {outcomes.map((item) => {
        if (
          item.outcomeResult !== null &&
          item.outcomeResult.components !== null &&
          isIItemObjectMultiArray(item.outcomeResult.components)
        ) {
          return item.outcomeResult.components.map((subList) => {
            return (
              <LightCard>
                <OutcomeSubList subListItem={subList} />
              </LightCard>
            );
          });
        }
        return <LightCard>{renderOutcome(item.outcomeResult, item.outcomeName, false, false)}</LightCard>;
      })}
    </>
  );
};

export default Outcomes;

const renderCard = (outcome: IOutcome, onExplanation: (outcomeId: string) => void) => {
  if (outcome.evaluationStatus !== "SUCCEEDED") {
    return (
      <GalleryItem key={uuid()}>
        <OutcomeCard outcome={outcome} onExplanation={onExplanation}>
          <span />
        </OutcomeCard>
      </GalleryItem>
    );
  }
  if (
    outcome.outcomeResult !== null &&
    outcome.outcomeResult.components !== null &&
    isIItemObjectMultiArray(outcome.outcomeResult.components)
  ) {
    return outcome.outcomeResult.components.map((item) => (
      <GalleryItem key={uuid()}>
        <OutcomeCard outcome={outcome} onExplanation={onExplanation}>
          <OutcomeSubList subListItem={item} />
        </OutcomeCard>
      </GalleryItem>
    ));
  }

  return (
    <GalleryItem key={uuid()}>
      <OutcomeCard outcome={outcome} onExplanation={onExplanation}>
        <div>{renderOutcome(outcome.outcomeResult, outcome.outcomeName, true, true)}</div>
      </OutcomeCard>
    </GalleryItem>
  );
};

type OutcomeCardProps = {
  children: React.ReactNode;
  outcome: IOutcome;
  onExplanation: (outcomeId: string) => void;
};

const OutcomeCard = (props: OutcomeCardProps) => {
  const { children, outcome, onExplanation } = props;
  return (
    <Card className="outcome-cards__card" isHoverable>
      <CardHeader>
        <Title headingLevel="h4" size="xl">
          {outcome.outcomeName}
        </Title>
      </CardHeader>
      <CardBody>
        {outcome.evaluationStatus !== undefined && outcome.evaluationStatus !== "SUCCEEDED" && (
          <div>
            <EvaluationStatus status={outcome.evaluationStatus} />
          </div>
        )}
        <div>{children}</div>
      </CardBody>
      <CardFooter>
        {outcome.outcomeId && onExplanation && (
          <Button
            variant="link"
            isInline
            className="outcome-cards__card__explanation-link"
            onClick={() => {
              onExplanation(outcome.outcomeId);
            }}>
            View Details <LongArrowAltRightIcon />
          </Button>
        )}
      </CardFooter>
    </Card>
  );
};

const renderOutcome = (outcomeData: IItemObject, name: string, compact = true, hidePropertyName = false) => {
  let renderItems: JSX.Element[] = [];

  if (outcomeData.value !== null) {
    return <OutcomeProperty property={outcomeData} key={outcomeData.name} hidePropertyName={hidePropertyName} />;
  }
  if (outcomeData.components.length) {
    if (isIItemObjectArray(outcomeData.components)) {
      renderItems.push(<OutcomeComposed outcome={outcomeData} key={outcomeData.name} compact={compact} name={name} />);
    } else if (isIItemObjectMultiArray(outcomeData.components)) {
      outcomeData.components.forEach((item) => {
        renderItems.push(<OutcomeSubList subListItem={item} />);
      });
    }
  }

  return <React.Fragment key={uuid()}>{renderItems.map((item: JSX.Element) => item)}</React.Fragment>;
};

const OutcomeProperty = (props: { property: IItemObject; hidePropertyName: boolean }) => {
  const { property, hidePropertyName } = props;
  const basicTypes = ["string", "number", "boolean"];
  const bigOutcome = hidePropertyName && basicTypes.includes(typeof property.value);

  if (bigOutcome)
    return (
      <div className="outcome__property__value--bigger">
        <FormattedValue value={property.value} />
      </div>
    );
  else
    return (
      <Split key={uuid()} className="outcome__property">
        <SplitItem className="outcome__property__name" key="property-name">
          {hidePropertyName ? "Result" : property.name}:
        </SplitItem>
        <SplitItem className="outcome__property__value" key="property-value">
          <FormattedValue value={property.value} />
        </SplitItem>
      </Split>
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

type OutcomeSubListProps = {
  subListItem: IItemObject[];
};
const OutcomeSubList = (props: OutcomeSubListProps) => {
  const { subListItem } = props;

  return (
    <>
      {subListItem &&
        subListItem.map((item) => (
          <Split key={uuid()} className="outcome__property">
            <SplitItem className="outcome__property__name" key="property-name">
              {item.name}:
            </SplitItem>
            <SplitItem className="outcome__property__value" key="property-value">
              <FormattedValue value={item.value} />
            </SplitItem>
          </Split>
        ))}
    </>
  );
};

type LightCardProps = {
  children: React.ReactNode;
};

const LightCard = (props: LightCardProps) => {
  const { children } = props;
  return (
    <Card key={uuid()}>
      <CardBody>{children}</CardBody>
    </Card>
  );
};
