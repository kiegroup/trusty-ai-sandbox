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

package com.redhat.developer.counterfactual.constraints;

import com.redhat.developer.counterfactual.entities.CreditCardApprovalEntity;
import com.redhat.developer.counterfactual.metrics.Facts;
import com.redhat.developer.counterfactual.models.CreditCardApprovalModel;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.math.BigDecimal;

import static com.redhat.developer.counterfactual.constraints.ApprovalConstraintConfiguration.HARD_LEVELS_SIZE;
import static com.redhat.developer.counterfactual.constraints.ApprovalConstraintConfiguration.SOFT_LEVELS_SIZE;

public class ApprovalContraintsProvider implements ConstraintProvider {

  @Override
  public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
    return new Constraint[] {
      inputDistance(constraintFactory),
      approval(constraintFactory),
      changedAge(constraintFactory),
      changedNumberOfChildren(constraintFactory)
    };
  }

  private Constraint changedAge(ConstraintFactory constraintFactory) {
    return constraintFactory
        .from(CreditCardApprovalEntity.class)
        .filter(entity -> !entity.getAge().equals(Facts.input.getAge()))
        .penalize(
            "Changed age",
            BendableBigDecimalScore.ofHard(
                HARD_LEVELS_SIZE, SOFT_LEVELS_SIZE, 1, BigDecimal.valueOf(1)));
  }

  private Constraint changedNumberOfChildren(ConstraintFactory constraintFactory) {
    return constraintFactory
        .from(CreditCardApprovalEntity.class)
        .filter(entity -> !entity.getChildren().equals(Facts.input.getChildren()))
        .penalize(
            "Changed number of children",
            BendableBigDecimalScore.ofHard(
                HARD_LEVELS_SIZE, SOFT_LEVELS_SIZE, 1, BigDecimal.valueOf(1)));
  }

  private Constraint approval(ConstraintFactory constraintFactory) {
    return constraintFactory
        .from(CreditCardApprovalEntity.class)
        .filter(
            entity -> {
              var prediction =
                  (Integer) CreditCardApprovalModel.getModel().predict(entity).get("APPROVED");
              return prediction.equals(0);
            })
        .penalize(
            "Outcome",
            BendableBigDecimalScore.ofHard(
                HARD_LEVELS_SIZE, SOFT_LEVELS_SIZE, 0, BigDecimal.valueOf(1)));
  }

  private Constraint inputDistance(ConstraintFactory constraintFactory) {
    return constraintFactory
        .from(CreditCardApprovalEntity.class)
        .penalizeConfigurableBigDecimal(
            "Distance",
            entity -> {
              return BigDecimal.valueOf(entity.distanceTo(Facts.input));
            });
  }
}
