package com.redhat.developer.counterfactual.constraints;

import org.optaplanner.core.api.domain.constraintweight.ConstraintConfiguration;
import org.optaplanner.core.api.domain.constraintweight.ConstraintWeight;
import org.optaplanner.core.api.score.buildin.bendablebigdecimal.BendableBigDecimalScore;

import java.math.BigDecimal;

@ConstraintConfiguration
public class ApprovalConstraintConfiguration {

  public static final int HARD_LEVELS_SIZE = 2;
  public static final int SOFT_LEVELS_SIZE = 1;

  @ConstraintWeight("Distance")
  private BendableBigDecimalScore distance =
      BendableBigDecimalScore.ofSoft(HARD_LEVELS_SIZE, SOFT_LEVELS_SIZE, 0, BigDecimal.valueOf(1));
}
