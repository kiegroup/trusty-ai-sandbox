import React from "react";
import Outcomes from "./Outcomes";
import { mount } from "enzyme";
import { IOutcome } from "../../Outcome/types";

jest.mock("uuid", () => {
  let value = 0;
  return { v4: () => value++ };
});

const outcomesProps = {
  outcomes: [
    {
      outcomeId: "_12268B68-94A1-4960-B4C8-0B6071AFDE58",
      outcomeName: "Mortgage Approval",
      evaluationStatus: "SUCCEEDED",
      outcomeResult: { name: "Mortgage Approval", typeRef: "boolean", value: true, components: [] },
      messages: [],
      hasErrors: false,
    },
    {
      outcomeId: "_9CFF8C35-4EB3-451E-874C-DB27A5A424C0",
      outcomeName: "Risk Score",
      evaluationStatus: "SUCCEEDED",
      outcomeResult: { name: "Risk Score", typeRef: "number", value: 21.7031851958099, components: [] },
      messages: [],
      hasErrors: false,
    },
  ] as IOutcome[],
  onExplanationClick: jest.fn(),
  listView: true,
};

describe("Outcomes", () => {
  test("renders a list of outcomes as cards", () => {
    const wrapper = mount(<Outcomes {...outcomesProps} />);

    expect(wrapper).toMatchSnapshot();
    expect(wrapper.find("OutcomeCard")).toHaveLength(2);
    expect(wrapper.find("h4.outcome-cards__card__title").at(0).text()).toMatch("Mortgage Approval");
    expect(wrapper.find(".outcome__property__value--bigger").at(0).text()).toMatch("true");
    expect(wrapper.find("h4.outcome-cards__card__title").at(1).text()).toMatch("Risk Score");
    expect(wrapper.find(".outcome__property__value--bigger").at(1).text()).toMatch("21.7031851958099");
  });
  test("handles clicks on the explanation link", () => {
    const wrapper = mount(<Outcomes {...outcomesProps} />);

    wrapper.find(".outcome-cards__card__explanation-link").at(0).simulate("click");
    expect(outcomesProps.onExplanationClick).toHaveBeenCalledTimes(1);
    expect(outcomesProps.onExplanationClick).toHaveBeenCalledWith("_12268B68-94A1-4960-B4C8-0B6071AFDE58");
  });
});
