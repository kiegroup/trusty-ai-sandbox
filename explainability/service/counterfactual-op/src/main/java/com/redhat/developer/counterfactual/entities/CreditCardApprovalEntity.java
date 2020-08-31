package com.redhat.developer.counterfactual.entities;

import com.redhat.developer.counterfactual.Measures;
import com.redhat.developer.model.Feature;
import com.redhat.developer.model.FeatureFactory;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
public class CreditCardApprovalEntity {

  private Integer age;

  private Integer income;

  private Integer children;

  private Integer daysEmployed;

  private Boolean ownRealty;

  private Boolean workPhone;

  private Boolean ownCar;

  public CreditCardApprovalEntity() {}

  public CreditCardApprovalEntity(
      Integer age,
      Integer income,
      Integer children,
      Integer daysEmployed,
      Boolean ownRealty,
      Boolean workPhone,
      Boolean ownCar) {
    this.age = age;
    this.income = income;
    this.children = children;
    this.daysEmployed = daysEmployed;
    this.ownRealty = ownRealty;
    this.workPhone = workPhone;
    this.ownCar = ownCar;
  }

  @PlanningVariable(valueRangeProviderRefs = {"ageRange"})
  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  @PlanningVariable(valueRangeProviderRefs = {"incomeRange"})
  public Integer getIncome() {
    return income;
  }

  public void setIncome(Integer income) {
    this.income = income;
  }

  @PlanningVariable(valueRangeProviderRefs = {"childrenRange"})
  public Integer getChildren() {
    return children;
  }

  public void setChildren(Integer children) {
    this.children = children;
  }

  @PlanningVariable(valueRangeProviderRefs = {"daysEmployedRange"})
  public Integer getDaysEmployed() {
    return daysEmployed;
  }

  public void setDaysEmployed(Integer daysEmployed) {
    this.daysEmployed = daysEmployed;
  }

  @PlanningVariable(valueRangeProviderRefs = {"ownRealtyRange"})
  public Boolean getOwnRealty() {
    return ownRealty;
  }

  public void setOwnRealty(Boolean ownRealty) {
    this.ownRealty = ownRealty;
  }

  @PlanningVariable(valueRangeProviderRefs = {"workPhoneRange"})
  public Boolean getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(Boolean workPhone) {
    this.workPhone = workPhone;
  }

  @PlanningVariable(valueRangeProviderRefs = {"ownCarRange"})
  public Boolean getOwnCar() {
    return ownCar;
  }

  public void setOwnCar(Boolean ownCar) {
    this.ownCar = ownCar;
  }

  @Override
  public String toString() {
    return "CreditCardApprovalEntity{"
        + "age="
        + age
        + ", income="
        + income
        + ", children="
        + children
        + ", daysEmployed="
        + daysEmployed
        + ", ownRealty="
        + ownRealty
        + ", workPhone="
        + workPhone
        + ", ownCar="
        + ownCar
        + '}';
  }

  public List<Feature> buildFeatures() {
    final List<Feature> context = new ArrayList<>();
    final Feature age = FeatureFactory.newNumericalFeature("age", this.getAge());
    final Feature income = FeatureFactory.newNumericalFeature("income", this.getIncome());
    final Feature children = FeatureFactory.newNumericalFeature("children", this.getChildren());
    final Feature daysEmployed =
        FeatureFactory.newNumericalFeature("daysEmployed", this.getDaysEmployed());
    final Feature ownRealty =
        FeatureFactory.newNumericalFeature("ownRealty", this.getOwnRealty() ? 1 : 0);
    final Feature workPhone =
        FeatureFactory.newNumericalFeature("workPhone", this.getWorkPhone() ? 1 : 0);
    final Feature ownCar = FeatureFactory.newNumericalFeature("ownCar", this.getOwnCar() ? 1 : 0);

    context.add(income);
    context.add(children);
    context.add(daysEmployed);
    context.add(age);
    context.add(ownCar);
    context.add(ownRealty);
    context.add(workPhone);
    return context;
  }

  public double[] toArray() {
    return new double[] {
      this.getAge().doubleValue(),
      this.getIncome().doubleValue(),
      this.getChildren().doubleValue(),
      this.getDaysEmployed().doubleValue(),
      this.getOwnRealty() ? 1.0 : 0.0,
      this.getWorkPhone() ? 1.0 : 0.0,
      this.getWorkPhone() ? 1.0 : 0.0
    };
  }

  public double distanceTo(CreditCardApprovalEntity other) {
    return Math.pow(Measures.manhattan(this.toArray(), other.toArray()), 2.0);
  }
}
