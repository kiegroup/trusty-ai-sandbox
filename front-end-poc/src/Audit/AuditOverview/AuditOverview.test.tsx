import React from "react";
import { mount, shallow } from "enzyme";
import AuditOverview from "./AuditOverview";
import * as api from "../../Shared/api/audit.api";
import { BrowserRouter } from "react-router-dom";
import { act } from "react-dom/test-utils";

const executions = {
  data: {
    total: 28,
    limit: 50,
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
        executionId: "023a0d79-2be6-4ec8-9ef7-99a6796cb319",
        executionDate: "2020-06-01T12:33:57+0000",
        executionSucceeded: true,
        executorName: "testUser",
        executedModelName: "LoanEligibility",
        executionType: "DECISION",
      },
      {
        executionId: "3a5d4a4e-7c5a-4ce7-85de-6024fbf1da39",
        executionDate: "2020-06-01T12:33:56+0000",
        executionSucceeded: true,
        executorName: "testUser",
        executedModelName: "LoanEligibility",
        executionType: "DECISION",
      },
      {
        executionId: "a4e0b8e8-9a6d-4a8e-ad5a-54e5c654a248",
        executionDate: "2020-06-01T12:33:23+0000",
        executionSucceeded: true,
        executorName: "testUser",
        executedModelName: "fraud-scoring",
        executionType: "DECISION",
      },
      {
        executionId: "f08adc80-2c2d-43f4-801c-4f08e10820a0",
        executionDate: "2020-06-01T12:33:18+0000",
        executionSucceeded: true,
        executorName: "testUser",
        executedModelName: "fraud-scoring",
        executionType: "DECISION",
      },
    ],
  },
};
const flushPromises = () => new Promise(setImmediate);
const apiMock = jest.spyOn(api, "getExecutions");
// @ts-ignore
apiMock.mockImplementation(() => Promise.resolve(executions));

describe("Audit overview", () => {
  test("renders", () => {
    const wrapper = shallow(<AuditOverview />);
    expect(wrapper).toMatchSnapshot();
  });

  test("loads a list of executions from the last month", async () => {
    const wrapper = mount(
      <BrowserRouter>
        <AuditOverview />
      </BrowserRouter>
    );
    const oneMonthAgo = new Date();
    oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);
    const fromDate = oneMonthAgo.toISOString().substr(0, 10);
    const toDate = new Date().toISOString().substr(0, 10);

    await act(async () => {
      await flushPromises();
      wrapper.update();
    });

    expect(apiMock).toHaveBeenCalledTimes(1);
    expect(apiMock).toHaveBeenCalledWith("", fromDate, toDate, 10, 0);
    expect(wrapper.find("ExecutionTable").props().data).toStrictEqual({
      status: "SUCCESS",
      data: executions.data.headers,
    });
  });
});
