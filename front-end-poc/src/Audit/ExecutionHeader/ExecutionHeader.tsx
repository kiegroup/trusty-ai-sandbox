import React from "react";
import { Flex, FlexItem, Title, Tooltip } from "@patternfly/react-core";
import SkeletonInlineStripe from "../../Shared/skeletons/SkeletonInlineStripe";
import ExecutionStatus from "../ExecutionStatus/ExecutionStatus";
import FormattedDate from "../../Shared/components/FormattedDate/FormattedDate";
import { UserIcon, HashtagIcon } from "@patternfly/react-icons";
import { IExecution } from "../types";
import { RemoteData } from "../../Shared/types";
import "./ExecutionHeader.scss";

type ExecutionHeaderProps = {
  execution: RemoteData<Error, IExecution>;
  title: string;
};

const ExecutionHeader = (props: ExecutionHeaderProps) => {
  const { execution, title } = props;

  return (
    <section className="execution-header">
      <Flex>
        <Flex grow={{ default: "grow" }}>
          <FlexItem>
            <Title size="4xl" headingLevel="h1">
              <span>
                <span>{title}</span>
              </span>
            </Title>
          </FlexItem>
        </Flex>
        <Flex>
          <FlexItem className="execution-header__property">
            {execution.status === "LOADING" && (
              <SkeletonInlineStripe customStyle={{ height: "1.5em", verticalAlign: "baseline" }} />
            )}
            {execution.status === "SUCCESS" && (
              <Title size="lg" headingLevel="h4">
                <Tooltip content="Execution Id" entryDelay={23} exitDelay={23} distance={5} position="bottom">
                  <span className="execution-header__uuid">
                    <HashtagIcon className="execution-header__icon" />
                    {execution.data.executionId}
                  </span>
                </Tooltip>
              </Title>
            )}
          </FlexItem>
          <FlexItem className="execution-header__property">
            {execution.status === "LOADING" && (
              <SkeletonInlineStripe customStyle={{ height: "1.5em", verticalAlign: "baseline" }} />
            )}
            {execution.status === "SUCCESS" && (
              <Title size="lg" headingLevel="h4">
                <ExecutionStatus result={execution.data.executionSucceeded} />
                <span> </span>
                <FormattedDate date={execution.data.executionDate} preposition={true} />
              </Title>
            )}
          </FlexItem>
          <FlexItem className="execution-header__property">
            {execution.status === "LOADING" && (
              <SkeletonInlineStripe customStyle={{ height: "1.5em", verticalAlign: "baseline" }} />
            )}

            {execution.status === "SUCCESS" && (
              <Title size="lg" headingLevel="h4">
                <span>
                  <UserIcon className="execution-header__icon" />
                  Executed by {execution.data.executorName}
                </span>
              </Title>
            )}
          </FlexItem>
        </Flex>
      </Flex>
    </section>
  );
};

export default ExecutionHeader;
