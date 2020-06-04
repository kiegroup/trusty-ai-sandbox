import React from "react";
import FromFilter from "./FromFilter";
import { mount, shallow } from "enzyme";

const defaultProps = {
  fromDate: "2020-01-01",
  maxDate: "2020-02-29",
  onFromDateUpdate: jest.fn(),
};

describe("From filter", () => {
  test("renders", () => {
    const wrapper = shallow(<FromFilter {...defaultProps} />);
    expect(wrapper).toMatchSnapshot();
  });

  test("handles date update", () => {
    const wrapper = mount(<FromFilter {...defaultProps} />);
    const fromDate = "2020-01-20";
    wrapper.props().onFromDateUpdate(fromDate);

    expect(defaultProps.onFromDateUpdate).toHaveBeenCalledTimes(1);
    expect(defaultProps.onFromDateUpdate).toHaveBeenCalledWith(fromDate);
  });
});
