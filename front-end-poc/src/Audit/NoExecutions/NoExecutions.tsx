import React from "react";
import { Bullseye, EmptyState, EmptyStateBody, EmptyStateIcon, Title } from "@patternfly/react-core";
import { SearchIcon } from "@patternfly/react-icons";

const NoExecutions = (colSpan: number) => {
  return [
    {
      heightAuto: true,
      decisionKey: "no-results",
      cells: [
        {
          props: { colSpan },
          title: (
            <Bullseye>
              <EmptyState>
                <EmptyStateIcon icon={SearchIcon} />
                <Title size="lg">No executions found</Title>
                <EmptyStateBody>No results match the filter criteria. Try removing all filters.</EmptyStateBody>
              </EmptyState>
            </Bullseye>
          ),
        },
      ],
    },
  ];
};

export default NoExecutions;
