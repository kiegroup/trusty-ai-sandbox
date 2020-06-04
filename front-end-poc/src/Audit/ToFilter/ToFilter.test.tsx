import React from "react";
import { mount, shallow } from "enzyme";
import ToFilter from "./ToFilter";

const defaultProps = {
  toDate: "2020-04-01",
  minDate: "2020-01-01",
  onToDateUpdate: jest.fn(),
};

describe("To filter", () => {
  test("renders", () => {
    const wrapper = shallow(<ToFilter {...defaultProps} />);
    expect(wrapper).toMatchSnapshot();
  });

  test("handles date update", () => {
    const wrapper = mount(<ToFilter {...defaultProps} />);
    const toDate = "2020-05-01";
    wrapper.props().onToDateUpdate(toDate);

    expect(defaultProps.onToDateUpdate).toHaveBeenCalledTimes(1);
    expect(defaultProps.onToDateUpdate).toHaveBeenCalledWith(toDate);
  });
});
