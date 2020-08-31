package com.redhat.developer.counterfactual.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.developer.counterfactual.entities.CreditCardApprovalEntity;

public class Facts {

  @JsonProperty public static CreditCardApprovalEntity counterfactual;

  @JsonProperty public static CreditCardApprovalEntity input;
}
