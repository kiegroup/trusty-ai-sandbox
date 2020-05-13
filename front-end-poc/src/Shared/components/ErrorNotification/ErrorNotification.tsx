import React from "react";
import { Alert, AlertActionCloseButton } from "@patternfly/react-core";

type TOwnProps = {
  closeToast: any;
  message: string;
  details: string;
};

const ErrorNotification = (props: TOwnProps) => {
  const { closeToast, message, details } = props;

  return (
    <Alert variant="danger" title={message} action={<AlertActionCloseButton onClose={closeToast} />}>
      {details}
    </Alert>
  );
};

export default ErrorNotification;
