package com.redhat.developer.counterfactual.scoring;

public abstract class AbstractCreditCardApprovalScoreCalculator {
  private Integer age;
  private Double income;
  private Integer children;
  private Double daysEmployed;
  private Integer ownRealty;
  private Integer workPhone;
  private Integer ownCar;

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Double getIncome() {
    return income;
  }

  public void setIncome(Double income) {
    this.income = income;
  }

  public Integer getChildren() {
    return children;
  }

  public void setChildren(Integer children) {
    this.children = children;
  }

  public Double getDaysEmployed() {
    return daysEmployed;
  }

  public void setDaysEmployed(Double daysEmployed) {
    this.daysEmployed = daysEmployed;
  }

  public Integer getOwnRealty() {
    return ownRealty;
  }

  public void setOwnRealty(Integer ownRealty) {
    this.ownRealty = ownRealty;
  }

  public Integer getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(Integer workPhone) {
    this.workPhone = workPhone;
  }

  public Integer getOwnCar() {
    return ownCar;
  }

  public void setOwnCar(Integer ownCar) {
    this.ownCar = ownCar;
  }
}
