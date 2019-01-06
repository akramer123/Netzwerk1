
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "firstPoint",
    "lastPoint"
})
public class ShapeIndex {

    @JsonProperty("firstPoint")
    private Integer firstPoint;
    @JsonProperty("lastPoint")
    private Integer lastPoint;

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
