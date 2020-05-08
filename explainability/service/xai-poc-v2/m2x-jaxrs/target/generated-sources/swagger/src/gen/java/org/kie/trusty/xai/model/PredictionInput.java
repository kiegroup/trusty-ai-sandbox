package org.kie.trusty.xai.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import org.kie.trusty.xai.model.Feature;



import javax.validation.constraints.*;





import io.swagger.annotations.*;


@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaResteasyServerCodegen", date = "2020-05-04T14:59:35.742503+02:00[Europe/Prague]")
public class PredictionInput   {

  
  private List<Feature> features = new ArrayList<Feature>();

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("features")

  public List<Feature> getFeatures() {
    return features;
  }
  public void setFeatures(List<Feature> features) {
    this.features = features;
  }

  

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PredictionInput predictionInput = (PredictionInput) o;
    return Objects.equals(features, predictionInput.features);
  }

  @Override
  public int hashCode() {
    return Objects.hash(features);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PredictionInput {\n");
    
    sb.append("    features: ").append(toIndentedString(features)).append("\n");
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




