import React from "react";
import PaginationContainer from "./PaginationContainer";
import { mount } from "enzyme";

const defaultProps = {
  total: 10,
  page: 1,
  pageSize: 10,
  paginationId: "pagination-id",
  onSetPage: jest.fn(),
  onSetPageSize: jest.fn(),
};

describe("pagination", () => {
  test("handle page and page size changes", () => {
    const wrapper = mount(<PaginationContainer {...defaultProps} />);
    const page = 2,
      pageSize = 50;

    wrapper.props().onSetPage(page);
    wrapper.props().onSetPageSize(pageSize);

    expect(defaultProps.onSetPage).toHaveBeenCalledTimes(1);
    expect(defaultProps.onSetPage).toHaveBeenCalledWith(page);
    expect(defaultProps.onSetPageSize).toHaveBeenCalledTimes(1);
    expect(defaultProps.onSetPageSize).toHaveBeenCalledWith(pageSize);
  });
});
