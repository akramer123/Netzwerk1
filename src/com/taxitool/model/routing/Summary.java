
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "distance",
    "trafficTime",
    "baseTime",
    "text",
    "travelTime",
    "_type"
})
public class Summary {

    @JsonProperty("distance")
    private Integer distance;
    @JsonProperty("trafficTime")
    private Integer trafficTime;
    @JsonProperty("baseTime")
    private Integer baseTime;
    @JsonProperty("text")
    private String text;
    @JsonProperty("travelTime")
    private Integer travelTime;
    @JsonProperty("_type")
    private String type;

    @JsonProperty("distance")
    public Integer getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @JsonProperty("trafficTime")
    public Integer getTrafficTime() {
        return trafficTime;
    }

    @JsonProperty("trafficTime")
    public void setTrafficTime(Integer trafficTime) {
        this.trafficTime = trafficTime;
    }

    @JsonProperty("baseTime")
    public Integer getBaseTime() {
        return baseTime;
    }

    @JsonProperty("baseTime")
    public void setBaseTime(Integer baseTime) {
        this.baseTime = baseTime;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("travelTime")
    public Integer getTravelTime() {
        return travelTime;
    }

    @JsonProperty("travelTime")
    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    @JsonProperty("_type")
    public String getType() {
        return type;
    }

    @JsonProperty("_type")
    public void setType(String type) {
        this.type = type;
    }

}
