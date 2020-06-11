import React from "react";
import { Alert, AlertActionCloseButton } from "@patternfly/react-core";

type ErrorNotificationProps = {
  closeToast: any;
  message: string;
  details: string;
};

const ErrorNotification = (props: ErrorNotificationProps) => {
  const { closeToast, message, details } = props;

  return (
    <Alert variant="danger" title={message} actionClose={<AlertActionCloseButton onClose={closeToast} />}>
      {details}
    </Alert>
  );
};

export default ErrorNotification;
