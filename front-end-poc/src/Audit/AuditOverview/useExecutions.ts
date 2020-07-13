import { useCallback, useEffect, useState } from "react";
import { getExecutions } from "../../Shared/api/audit.api";
import { RemoteData } from "../../Shared/types";
import { IExecutions } from "../types";
import { isCancelledRequest } from "../../Shared/api/httpClient";

const useExecutions = (searchString: string, from: string, to: string, limit: number, offset: number) => {
  const [executions, setExecutions] = useState<RemoteData<Error, IExecutions>>({
    status: "NOT_ASKED",
  });

  const loadExecutions = useCallback(() => {
    let isMounted = true;
    setExecutions({ status: "LOADING" });
    getExecutions(searchString, from, to, limit, offset)
      .then((response) => {
        if (isMounted) {
          setExecutions({ status: "SUCCESS", data: response.data });
        }
      })
      .catch((error) => {
        if (!isCancelledRequest(error)) {
          setExecutions({ status: "FAILURE", error: error });
        }
      });
    return () => {
      isMounted = false;
    };
  }, [searchString, from, to, limit, offset]);

  useEffect(() => {
    loadExecutions();
  }, [searchString, from, to, limit, offset, loadExecutions]);

  return { loadExecutions, executions };
};

export default useExecutions;
