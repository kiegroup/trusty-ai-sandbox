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
    const inputNode = searchInput.getDOMNode<HTMLInputElement>();

    inputNode.value = searchString;
    searchButton.simulate("click");

    expect(setSearchString).toBeCalledTimes(1);
    expect(setSearchString).toBeCalledWith(searchString);
  });

  test("handles from date filter", () => {
    const setFromDate = jest.fn();
    const fromDate = "2020-02-01";
    const wrapper = renderAuditToolbar("mount", { setFromDate });

    wrapper.props().setFromDate(fromDate);

    expect(setFromDate).toBeCalledTimes(1);
    expect(setFromDate).toBeCalledWith(fromDate);
  });

  test("handles to date filter", () => {
    const setToDate = jest.fn();
    const toDate = "2020-04-01";
    const wrapper = renderAuditToolbar("mount", { setToDate });

    wrapper.props().setToDate(toDate);

    expect(setToDate).toBeCalledTimes(1);
    expect(setToDate).toBeCalledWith(toDate);
  });

  test("handles pagination", () => {
    const setPage = jest.fn();
    const setPageSize = jest.fn();
    const page = 2;
    const pageSize = 50;
    const wrapper = renderAuditToolbar("mount", { setPage, setPageSize });

    wrapper.props().setPage(page);
    wrapper.props().setPageSize(pageSize);

    expect(setPage).toBeCalledTimes(1);
    expect(setPage).toBeCalledWith(page);
    expect(setPageSize).toBeCalledTimes(1);
    expect(setPageSize).toBeCalledWith(pageSize);
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
