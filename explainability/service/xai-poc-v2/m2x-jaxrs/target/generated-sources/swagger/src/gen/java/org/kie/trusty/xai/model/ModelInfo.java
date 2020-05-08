package org.kie.trusty.xai.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.kie.trusty.xai.model.DataDistribution;



import javax.validation.constraints.*;





import io.swagger.annotations.*;


@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaResteasyServerCodegen", date = "2020-05-04T14:59:35.742503+02:00[Europe/Prague]")
public class ModelInfo   {

  
  private String uuid = null;
  private String endpoint = null;
  private Integer inputShape = null;
  private Integer outputShape = null;
  private String name = null;
  private String version = null;
  /**
   * Gets or Sets taskType
   */
  public enum TaskTypeEnum {
    
    
    CLASSIFICATION("classification"),

    
    
    REGRESSION("regression");
    
    
    
    private String value;

    TaskTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }

  private TaskTypeEnum taskType = null;
  private DataDistribution dataDistribution = null;

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("uuid")

  public String getUuid() {
    return uuid;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("endpoint")

  public String getEndpoint() {
    return endpoint;
  }
  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("inputShape")

  public Integer getInputShape() {
    return inputShape;
  }
  public void setInputShape(Integer inputShape) {
    this.inputShape = inputShape;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("outputShape")

  public Integer getOutputShape() {
    return outputShape;
  }
  public void setOutputShape(Integer outputShape) {
    this.outputShape = outputShape;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("name")

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("version")

  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("taskType")

  public TaskTypeEnum getTaskType() {
    return taskType;
  }
  public void setTaskType(TaskTypeEnum taskType) {
    this.taskType = taskType;
  }

  
  /**
   
   
   
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("dataDistribution")

  public DataDistribution getDataDistribution() {
    return dataDistribution;
  }
  public void setDataDistribution(DataDistribution dataDistribution) {
    this.dataDistribution = dataDistribution;
  }

  

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelInfo modelInfo = (ModelInfo) o;
    return Objects.equals(uuid, modelInfo.uuid) &&
        Objects.equals(endpoint, modelInfo.endpoint) &&
        Objects.equals(inputShape, modelInfo.inputShape) &&
        Objects.equals(outputShape, modelInfo.outputShape) &&
        Objects.equals(name, modelInfo.name) &&
        Objects.equals(version, modelInfo.version) &&
        Objects.equals(taskType, modelInfo.taskType) &&
        Objects.equals(dataDistribution, modelInfo.dataDistribution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, endpoint, inputShape, outputShape, name, version, taskType, dataDistribution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelInfo {\n");
    
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    inputShape: ").append(toIndentedString(inputShape)).append("\n");
    sb.append("    outputShape: ").append(toIndentedString(outputShape)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    taskType: ").append(toIndentedString(taskType)).append("\n");
    sb.append("    dataDistribution: ").append(toIndentedString(dataDistribution)).append("\n");
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




