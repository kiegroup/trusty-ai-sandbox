import React, { useCallback, useEffect, useState } from "react";
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Divider,
  Grid,
  GridItem,
  Modal,
  PageSection,
  Stack,
  StackItem,
  Title,
  Tooltip,
} from "@patternfly/react-core";
import { HelpIcon } from "@patternfly/react-icons";
import { useParams, useHistory, useLocation } from "react-router-dom";
import { IExecutionRouteParams } from "../../Audit/types";
import { IOutcome } from "../../Outcome/types";
import { getDecisionFeatureScores, getDecisionOutcome, getDecisionOutcomeDetail } from "../../Shared/api/audit.api";
import OutcomePreview from "../../Outcome/OutcomePreview/OutcomePreview";
import InputDataBrowser from "../../InputData/InputDataBrowser/InputDataBrowser";
import SkeletonGrid from "../../Shared/skeletons/SkeletonGrid/SkeletonGrid";
import FeaturesScoreChart from "../FeaturesScoreChart/FeaturesScoreChart";
import { orderBy } from "lodash";
import SkeletonTornadoChart from "../../Shared/skeletons/SkeletonTornadoChart/SkeletonTornadoChart";
import FeaturesScoreTable from "../FeatureScoreTable/FeaturesScoreTable";
import queryString from "query-string";
import "./ExplanationView.scss";
import ExplanationSwitch from "../ExplanationSwitch/ExplanationSwitch";

export interface IFeatureScores {
  featureName: string;
  featureScore: number;
}

const ExplanationView = () => {
  const { executionId } = useParams<IExecutionRouteParams>();
  const [outcomeData, setOutcomeData] = useState<IOutcome | null>(null);
  const [outcomesList, setOutcomesList] = useState<IOutcome[] | null>(null);
  const [outcomeId, setOutcomeId] = useState<string | null>(null);
  const [outcomeDetail, setOutcomeDetail] = useState(null);
  const [featuresScores, setFeaturesScores] = useState<IFeatureScores[] | null>(null);
  const [topFeatures, setTopFeatures] = useState<IFeatureScores[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };
  const history = useHistory();
  const location = useLocation();

  const switchExplanation = useCallback(
    (outcomeId: string) => {
      history.push({
        search: `outcomeId=${outcomeId}`,
      });
    },
    [history]
  );

  useEffect(() => {
    let isMounted = true;
    getDecisionOutcome(executionId)
      .then((response) => {
        if (isMounted) {
          if (response.data && response.data.outcomes) {
            let defaultOutcome = response.data.outcomes[0];
            setOutcomesList(response.data.outcomes);
            if (!outcomeId) {
              history.replace({
                search: `outcomeId=${defaultOutcome.outcomeId}`,
              });
            }
          }
        }
      })
      .catch(() => {});

    getDecisionFeatureScores(executionId)
      .then((response) => {
        if (response.data && response.data.featureImportance) {
          const sortedFeatures = orderBy(response.data.featureImportance, (item) => Math.abs(item.featureScore), "asc");
          if (isMounted) {
            setFeaturesScores(sortedFeatures);
            if (sortedFeatures.length > 10) {
              setTopFeatures(sortedFeatures.slice(sortedFeatures.length - 10));
            }
          }
        }
      })
      .catch(() => {});
    return () => {
      isMounted = false;
    };
  }, [executionId, outcomeId, history]);

  useEffect(() => {
    let query = queryString.parse(location.search);
    if (query.outcomeId && query.outcomeId.length) {
      setOutcomeId(query.outcomeId as string);
      if (outcomesList) {
        let outcome = outcomesList.find((item) => item.outcomeId === query.outcomeId);
        setOutcomeData(outcome as IOutcome);
      }
    }
  }, [location.search, outcomesList]);

  useEffect(() => {
    let isMounted = true;
    if (outcomeId !== null) {
      getDecisionOutcomeDetail(executionId, outcomeId)
        .then((response) => {
          if (isMounted) {
            if (response && response.data && response.data.outcomeInputs) {
              setOutcomeDetail(response.data.outcomeInputs);
            }
          }
        })
        .catch(() => {});
    }
    return () => {
      isMounted = false;
    };
  }, [executionId, outcomeId]);

  return (
    <section className="explanation-view">
      <PageSection variant="light" className="explanation-view__section--outcome-selector">
        <Divider className="explanation-view__section--outcome-selector__divider" />
        {outcomeId !== null && outcomesList !== null && outcomesList.length > 1 && (
          <ExplanationSwitch
            currentExplanationId={outcomeId}
            onDecisionSelection={switchExplanation}
            outcomesList={outcomesList}
          />
        )}
      </PageSection>
      <PageSection variant="default" className="explanation-view__section">
        <div className="container">
          <Stack hasGutter>
            <StackItem>
              <Title headingLevel="h3" size="2xl">
                Outcome Details
              </Title>
            </StackItem>
            <StackItem>
              {outcomeData === null ? (
                <SkeletonGrid rowsNumber={4} colsNumber={2} />
              ) : (
                <OutcomePreview outcomeData={[outcomeData]} compact={false} />
              )}
            </StackItem>
          </Stack>
        </div>
      </PageSection>
      <PageSection className="explanation-view__section">
        <div className="container">
          <Stack hasGutter>
            <StackItem>
              <Title headingLevel="h3" size="2xl">
                Explanation
              </Title>
            </StackItem>
            <StackItem>
              <Grid hasGutter>
                <GridItem span={8}>
                  <Card>
                    <CardHeader>
                      {topFeatures ? (
                        <Title headingLevel="h4" size="xl">
                          Top Features Score Chart
                        </Title>
                      ) : (
                        <Title headingLevel="h4" size="xl">
                          Features Score Chart
                        </Title>
                      )}
                    </CardHeader>

                    <CardBody>
                      {featuresScores === null && <SkeletonTornadoChart valuesCount={10} height={400} />}
                      {featuresScores !== null && topFeatures.length === 0 && (
                        <div className="explanation-view__chart">
                          <FeaturesScoreChart featuresScore={featuresScores} />
                        </div>
                      )}
                      {featuresScores !== null && topFeatures.length > 0 && (
                        <>
                          <div className="explanation-view__chart">
                            <FeaturesScoreChart featuresScore={topFeatures} />
                          </div>
                          <Button
                            variant="secondary"
                            type="button"
                            className="all-features-opener"
                            onClick={handleModalToggle}>
                            View Complete Chart
                          </Button>
                          <Modal
                            width={"80%"}
                            title="All Features Score Chart"
                            isOpen={isModalOpen}
                            onClose={handleModalToggle}
                            actions={[
                              <Button key="close" onClick={handleModalToggle}>
                                Close
                              </Button>,
                            ]}>
                            <FeaturesScoreChart featuresScore={featuresScores} large={true} />
                          </Modal>
                        </>
                      )}
                    </CardBody>
                  </Card>
                </GridItem>
                <GridItem span={4}>
                  <Card className="explanation-view__score-table">
                    <CardHeader>
                      <Title headingLevel={"h4"} size={"lg"}>
                        Features Weight
                      </Title>
                    </CardHeader>
                    <CardBody>
                      {featuresScores === null && <SkeletonGrid colsNumber={2} rowsNumber={4} />}
                      {featuresScores !== null && topFeatures.length === 0 && (
                        <FeaturesScoreTable featuresScore={featuresScores} />
                      )}
                      {featuresScores !== null && topFeatures.length > 0 && (
                        <FeaturesScoreTable featuresScore={topFeatures} />
                      )}
                    </CardBody>
                  </Card>
                </GridItem>
              </Grid>
            </StackItem>
          </Stack>
        </div>
      </PageSection>
      <PageSection className="explanation-view__section">
        <div className="container">
          <Stack hasGutter>
            <StackItem>
              <Title headingLevel="h3" size="2xl">
                <span>Outcome Influencing Inputs</span>
                <Tooltip
                  position="auto"
                  content={
                    <div>
                      This section displays all the input that contributed to this specific decision outcome. They can
                      include model inputs (or a subset) or other sub-decisions
                    </div>
                  }>
                  <HelpIcon className="explanation-view__input-help" />
                </Tooltip>
              </Title>
            </StackItem>
            <StackItem>{<InputDataBrowser inputData={outcomeDetail} />}</StackItem>
          </Stack>
        </div>
      </PageSection>
    </section>
  );
};

export default ExplanationView;
