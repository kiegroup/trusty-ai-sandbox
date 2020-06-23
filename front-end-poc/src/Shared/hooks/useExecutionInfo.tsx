import { useEffect, useState } from "react";
import { ExecutionType, getExecution } from "../api/audit.api";
import { RemoteData } from "../types";
import { IExecution } from "../../Audit/types";

const useExecutionInfo = (executionType: ExecutionType, executionId: string) => {
  const [execution, setExecution] = useState<RemoteData<Error, IExecution>>({
    status: "NOT_ASKED",
  });

  useEffect(() => {
    let isMounted = true;
    setExecution({ status: "LOADING" });
    getExecution(executionType, executionId)
      .then((response) => {
        if (isMounted) {
          setExecution({ status: "SUCCESS", data: response.data });
        }
      })
      .catch((error) => {
        setExecution({ status: "FAILURE", error: error });
      });
    return () => {
      isMounted = false;
    };
  }, [executionType, executionId]);

  return execution;
};

export default useExecutionInfo;
