package com.redhat.developer.counterfactual.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class ScoreBreakdown {

  @JsonProperty public Map<String, String> constraints = new HashMap<>();
}
