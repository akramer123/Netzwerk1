
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
    "waypoint",
    "mode",
    "shape",
    "leg"
})
public class Route_ {

    @JsonProperty("waypoint")
    private List<Waypoint> waypoint = null;
    @JsonProperty("mode")
    private Mode mode;
    @JsonProperty("shape")
    private List<String> shape = null;
    @JsonProperty("leg")
    private List<Leg> leg = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("waypoint")
    public List<Waypoint> getWaypoint() {
        return waypoint;
    }

    @JsonProperty("waypoint")
    public void setWaypoint(List<Waypoint> waypoint) {
        this.waypoint = waypoint;
    }

    @JsonProperty("mode")
    public Mode getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @JsonProperty("shape")
    public List<String> getShape() {
        return shape;
    }

    @JsonProperty("shape")
    public void setShape(List<String> shape) {
        this.shape = shape;
    }

    @JsonProperty("leg")
    public List<Leg> getLeg() {
        return leg;
    }

    @JsonProperty("leg")
    public void setLeg(List<Leg> leg) {
        this.leg = leg;
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
