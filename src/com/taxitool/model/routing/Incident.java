
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "validityPeriod",
    "text",
    "type",
    "criticality",
    "firstPoint",
    "lastPoint"
})
public class Incident {

    @JsonProperty("validityPeriod")
    private ValidityPeriod validityPeriod;
    @JsonProperty("text")
    private String text;
    @JsonProperty("type")
    private String type;
    @JsonProperty("criticality")
    private Integer criticality;
    @JsonProperty("firstPoint")
    private Integer firstPoint;
    @JsonProperty("lastPoint")
    private Integer lastPoint;

    @JsonProperty("validityPeriod")
    public ValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    @JsonProperty("validityPeriod")
    public void setValidityPeriod(ValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("criticality")
    public Integer getCriticality() {
        return criticality;
    }

    @JsonProperty("criticality")
    public void setCriticality(Integer criticality) {
        this.criticality = criticality;
    }

    @JsonProperty("firstPoint")
    public Integer getFirstPoint() {
        return firstPoint;
    }

    @JsonProperty("firstPoint")
    public void setFirstPoint(Integer firstPoint) {
        this.firstPoint = firstPoint;
    }

    @JsonProperty("lastPoint")
    public Integer getLastPoint() {
        return lastPoint;
    }

    @JsonProperty("lastPoint")
    public void setLastPoint(Integer lastPoint) {
        this.lastPoint = lastPoint;
    }

}
