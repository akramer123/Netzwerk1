
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "trafficSpeed",
    "trafficTime",
    "baseSpeed",
    "baseTime",
    "jamFactor"
})
public class DynamicSpeedInfo {

    @JsonProperty("trafficSpeed")
    private Double trafficSpeed;
    @JsonProperty("trafficTime")
    private Integer trafficTime;
    @JsonProperty("baseSpeed")
    private Double baseSpeed;
    @JsonProperty("baseTime")
    private Integer baseTime;
    @JsonProperty("jamFactor")
    private Integer jamFactor;

    @JsonProperty("trafficSpeed")
    public Double getTrafficSpeed() {
        return trafficSpeed;
    }

    @JsonProperty("trafficSpeed")
    public void setTrafficSpeed(Double trafficSpeed) {
        this.trafficSpeed = trafficSpeed;
    }

    @JsonProperty("trafficTime")
    public Integer getTrafficTime() {
        return trafficTime;
    }

    @JsonProperty("trafficTime")
    public void setTrafficTime(Integer trafficTime) {
        this.trafficTime = trafficTime;
    }

    @JsonProperty("baseSpeed")
    public Double getBaseSpeed() {
        return baseSpeed;
    }

    @JsonProperty("baseSpeed")
    public void setBaseSpeed(Double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    @JsonProperty("baseTime")
    public Integer getBaseTime() {
        return baseTime;
    }

    @JsonProperty("baseTime")
    public void setBaseTime(Integer baseTime) {
        this.baseTime = baseTime;
    }

    @JsonProperty("jamFactor")
    public Integer getJamFactor() {
        return jamFactor;
    }

    @JsonProperty("jamFactor")
    public void setJamFactor(Integer jamFactor) {
        this.jamFactor = jamFactor;
    }

}
