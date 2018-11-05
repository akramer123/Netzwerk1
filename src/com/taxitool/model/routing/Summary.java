
package com.taxitool.model.routing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "distance",
    "trafficTime",
    "baseTime",
    "flags",
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
    @JsonProperty("flags")
    private List<String> flags = null;
    @JsonProperty("text")
    private String text;
    @JsonProperty("travelTime")
    private Integer travelTime;
    @JsonProperty("_type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("flags")
    public List<String> getFlags() {
        return flags;
    }

    @JsonProperty("flags")
    public void setFlags(List<String> flags) {
        this.flags = flags;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
