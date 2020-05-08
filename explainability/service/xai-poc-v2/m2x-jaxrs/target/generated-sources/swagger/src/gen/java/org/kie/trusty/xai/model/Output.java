package org.kie.trusty.xai.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;



import javax.validation.constraints.*;





import io.swagger.annotations.*;


@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaResteasyServerCodegen", date = "2020-05-04T14:59:35.742503+02:00[Europe/Prague]")
public class Output   {

  
  private String label = null;
  private BigDecimal score = null;

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("label")

  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("score")

  public BigDecimal getScore() {
    return score;
  }
  public void setScore(BigDecimal score) {
    this.score = score;
  }

  

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Output output = (Output) o;
    return Objects.equals(label, output.label) &&
        Objects.equals(score, output.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, score);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Output {\n");
    
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
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




