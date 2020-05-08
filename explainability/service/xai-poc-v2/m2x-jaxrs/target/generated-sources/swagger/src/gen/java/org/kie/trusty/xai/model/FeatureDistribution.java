package org.kie.trusty.xai.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;



import javax.validation.constraints.*;





import io.swagger.annotations.*;


@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaResteasyServerCodegen", date = "2020-05-04T14:59:35.742503+02:00[Europe/Prague]")
public class FeatureDistribution   {

  
  private BigDecimal min = null;
  private BigDecimal max = null;
  private BigDecimal mean = null;
  private BigDecimal stdDev = null;

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("min")

  public BigDecimal getMin() {
    return min;
  }
  public void setMin(BigDecimal min) {
    this.min = min;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("max")

  public BigDecimal getMax() {
    return max;
  }
  public void setMax(BigDecimal max) {
    this.max = max;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("mean")

  public BigDecimal getMean() {
    return mean;
  }
  public void setMean(BigDecimal mean) {
    this.mean = mean;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("stdDev")

  public BigDecimal getStdDev() {
    return stdDev;
  }
  public void setStdDev(BigDecimal stdDev) {
    this.stdDev = stdDev;
  }

  

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeatureDistribution featureDistribution = (FeatureDistribution) o;
    return Objects.equals(min, featureDistribution.min) &&
        Objects.equals(max, featureDistribution.max) &&
        Objects.equals(mean, featureDistribution.mean) &&
        Objects.equals(stdDev, featureDistribution.stdDev);
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max, mean, stdDev);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FeatureDistribution {\n");
    
    sb.append("    min: ").append(toIndentedString(min)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
    sb.append("    mean: ").append(toIndentedString(mean)).append("\n");
    sb.append("    stdDev: ").append(toIndentedString(stdDev)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}




