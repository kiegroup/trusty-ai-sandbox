import React from "react";
import AuditToolbar from "./AuditToolbar";
import { mount, shallow } from "enzyme";

describe("Audit toolbar", () => {
  test("renders", () => {
    const wrapper = renderAuditToolbar("shallow");
    expect(wrapper).toMatchSnapshot();
  });

  test("allows free text search", () => {
    const setSearchString = jest.fn();
    const searchString = "#12345";
    const wrapper = renderAuditToolbar("mount", { setSearchString });
    const searchInput = wrapper.find("input#audit-search-input");
    const searchButton = wrapper.find("button#audit-search");
    // searchInput.at(0).instance().value = "";
    const inputNode = searchInput.getDOMNode<HTMLInputElement>();

    inputNode.value = searchString;
    searchButton.simulate("click");

    expect(setSearchString).toBeCalledTimes(1);
    expect(setSearchString).toBeCalledWith(searchString);
  });
});

const renderAuditToolbar = (method: "shallow" | "mount", props?: object) => {
  const defaultProps = {
    setSearchString() {},
    fromDate: "2020-01-01",
    setFromDate() {},
    toDate: "2020-02-01",
    setToDate() {},
    total: 20,
    page: 1,
    pageSize: 10,
    setPage() {},
    setPageSize() {},
    ...props,
  };
  if (method === "shallow") {
    return shallow(<AuditToolbar {...defaultProps} />);
  } else {
    return mount(<AuditToolbar {...defaultProps} />);
  }
};
