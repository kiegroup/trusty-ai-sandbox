/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.developer.counterfactual.solutions;

import com.redhat.developer.counterfactual.constraints.ApprovalConstraintConfiguration;
import com.redhat.developer.counterfactual.entities.CreditCardApprovalEntity;
import org.optaplanner.core.api.domain.constraintweight.ConstraintConfigurationProvider;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@PlanningSolution
@XmlRootElement(name = "Approval")
public class Approval {
  @XmlElement(name = "creditCardApprovals")
  protected final List<CreditCardApprovalEntity> creditCardApprovals = new ArrayList<>();

  private BendableBigDecimalScore score;

  @ConstraintConfigurationProvider private ApprovalConstraintConfiguration constraintConfiguration;

  public Approval() {
    constraintConfiguration = new ApprovalConstraintConfiguration();
    final Random r = new Random();
    CreditCardApprovalEntity approval = new CreditCardApprovalEntity();
    approval.setAge(getAgesList().createRandomIterator(r).next());
    approval.setIncome(getIncomeList().createRandomIterator(r).next());
    approval.setChildren(getChildrenList().createRandomIterator(r).next());
    approval.setDaysEmployed(getDayEmployedList().createRandomIterator(r).next());
    approval.setOwnRealty(getOwnRealtyList().createRandomIterator(r).next());
    approval.setWorkPhone(getWorkPhoneList().createRandomIterator(r).next());
    approval.setOwnCar(getOwnCarList().createRandomIterator(r).next());
    creditCardApprovals.add(approval);
  }

  @PlanningEntityCollectionProperty
  public List<CreditCardApprovalEntity> getApprovalsList() {
    return creditCardApprovals;
  }

  @ValueRangeProvider(id = "ageRange")
  public CountableValueRange<Integer> getAgesList() {
    return ValueRangeFactory.createIntValueRange(18, 100);
  }

  @ValueRangeProvider(id = "incomeRange")
  public CountableValueRange<Integer> getIncomeList() {
    return ValueRangeFactory.createIntValueRange(0, 1000000);
  }

  @ValueRangeProvider(id = "childrenRange")
  public CountableValueRange<Integer> getChildrenList() {
    return ValueRangeFactory.createIntValueRange(0, 20);
  }

  @ValueRangeProvider(id = "daysEmployedRange")
  public CountableValueRange<Integer> getDayEmployedList() {
    return ValueRangeFactory.createIntValueRange(0, 18250);
  }

  @ValueRangeProvider(id = "ownRealtyRange")
  public CountableValueRange<Boolean> getOwnRealtyList() {
    return ValueRangeFactory.createBooleanValueRange();
  }

  @ValueRangeProvider(id = "workPhoneRange")
  public CountableValueRange<Boolean> getWorkPhoneList() {
    return ValueRangeFactory.createBooleanValueRange();
  }

  @ValueRangeProvider(id = "ownCarRange")
  public CountableValueRange<Boolean> getOwnCarList() {
    return ValueRangeFactory.createBooleanValueRange();
  }

  @PlanningScore(bendableHardLevelsSize = 2, bendableSoftLevelsSize = 1)
  public BendableBigDecimalScore getScore() {
    return score;
  }

  public void setScore(BendableBigDecimalScore score) {
    this.score = score;
  }
}
