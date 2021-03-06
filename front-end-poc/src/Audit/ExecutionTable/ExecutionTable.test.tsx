import React from "react";
import ExecutionTable from "./ExecutionTable";
import { shallow } from "enzyme";
import { IExecutions } from "../types";
import { RemoteData } from "../../Shared/types";

describe("Execution table", () => {
  test("renders loading skeletons when the data is not yet fetching", () => {
    const data = { status: "NOT_ASKED" } as RemoteData<Error, IExecutions>;
    const wrapper = shallow(<ExecutionTable data={data} />);
    expect(wrapper).toMatchSnapshot();
  });

  test("renders loading skeletons when the data is loading", () => {
    const data = { status: "LOADING" } as RemoteData<Error, IExecutions>;
    const wrapper = shallow(<ExecutionTable data={data} />);
    expect(wrapper).toMatchSnapshot();
  });

  test("renders a loading error", () => {
    const data = { status: "FAILURE", error: { name: "", message: "" } } as RemoteData<Error, IExecutions>;
    const wrapper = shallow(<ExecutionTable data={data} />);
    expect(wrapper).toMatchSnapshot();
  });

  test("renders a list of executions", () => {
    const data = {
      status: "SUCCESS",
      data: {
        total: 2,
        limit: 10,
        offset: 0,
        headers: [
          {
            executionId: "b2b0ed8d-c1e2-46b5-ad4f-3ac54ff4beae",
            executionDate: "2020-06-01T12:33:57+0000",
            executionSucceeded: true,
            executorName: "testUser",
            executedModelName: "LoanEligibility",
            executionType: "DECISION",
          },
          {
            executionId: "b2b0ed8d-c1e2-46b5-ad4f-3hd83kidi4u74",
            executionDate: "2020-06-01T12:33:57+0000",
            executionSucceeded: true,
            executorName: "testUser",
            executedModelName: "LoanEligibility",
            executionType: "DECISION",
          },
        ],
      },
    };
    const wrapper = shallow(<ExecutionTable data={data as RemoteData<Error, IExecutions>} />);
    expect(wrapper).toMatchSnapshot();
  });

  test("renders no result message if no executions are found", () => {
    const data = {
      status: "SUCCESS",
      data: {
        total: 0,
        limit: 10,
        offset: 0,
        headers: [],
      },
    };
    const wrapper = shallow(<ExecutionTable data={data as RemoteData<Error, IExecutions>} />);
    expect(wrapper).toMatchSnapshot();
  });
});
