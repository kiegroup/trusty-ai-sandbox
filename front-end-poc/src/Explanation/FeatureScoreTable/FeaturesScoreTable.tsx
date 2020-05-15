import React from "react";
import { IFeatureScores } from "../ExplanationView/ExplanationView";
import {
  DataList,
  DataListCell,
  DataListItem,
  DataListItemCells,
  DataListItemRow,
  Title,
} from "@patternfly/react-core";
import "./FeaturesScoreTable.scss";

type PScoreTable = {
  name: string;
  featuresScore: IFeatureScores[];
};

const ScoreTable = (props: PScoreTable) => {
  const { name, featuresScore } = props;
  return (
    <DataList aria-label="Features Scores" className="pf-m-compact score-table">
      <DataListItem aria-labelledby="scores" className="score-table__heading">
        <DataListItemRow>
          <DataListItemCells
            dataListCells={[
              <DataListCell key="primary heading" width={2}>
                <Title headingLevel="h6" size="md" id="scores">
                  {name}
                </Title>
              </DataListCell>,
              <DataListCell key="secondary heading">
                <Title headingLevel="h6" size="md" id="scores">
                  Scores
                </Title>
              </DataListCell>,
            ]}
          />
        </DataListItemRow>
      </DataListItem>
      {featuresScore.map((item) => (
        <DataListItem key={item.featureName} aria-labelledby={`feature-${item.featureName.replace(" ", "-")}`}>
          <DataListItemRow>
            <DataListItemCells
              dataListCells={[
                <DataListCell key="feature-name" width={2}>
                  <span id="simple-item2">{item.featureName}</span>
                </DataListCell>,
                <DataListCell key="feature-score">{item.featureScore.toFixed(2)}</DataListCell>,
              ]}
            />
          </DataListItemRow>
        </DataListItem>
      ))}
    </DataList>
  );
};

type PFeatureScoreTable = {
  featuresScore: IFeatureScores[];
};

const FeaturesScoreTable = (props: PFeatureScoreTable) => {
  const { featuresScore } = props;
  const positiveScores = featuresScore.filter((item) => item.featureScore >= 0).reverse();
  const negativeScores = featuresScore.filter((item) => item.featureScore < 0).reverse();

  return (
    <section className="feature-score-table">
      {positiveScores && <ScoreTable name="Features with Positive Weight" featuresScore={positiveScores} />}
      {negativeScores && <ScoreTable name="Features with Negative Weight" featuresScore={negativeScores} />}
    </section>
  );
};

export default FeaturesScoreTable;
