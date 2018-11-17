
package com.taxitool.model.routing;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "start",
    "end",
    "length",
    "travelTime",
    "maneuver",
    "link",
    "boundingBox",
    "shape",
    "firstPoint",
    "lastPoint",
    "trafficTime",
    "baseTime",
    "summary"
})
public class Leg {

    @JsonProperty("start")
    private Start start;
    @JsonProperty("end")
    private End end;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("travelTime")
    private Integer travelTime;
    @JsonProperty("maneuver")
    private List<Maneuver> maneuver = null;
    @JsonProperty("link")
    private List<Link> link = null;
    @JsonProperty("boundingBox")
    private BoundingBox__ boundingBox;
    @JsonProperty("shape")
    private List<String> shape = null;
    @JsonProperty("firstPoint")
    private Integer firstPoint;
    @JsonProperty("lastPoint")
    private Integer lastPoint;
    @JsonProperty("trafficTime")
    private Integer trafficTime;
    @JsonProperty("baseTime")
    private Integer baseTime;
    @JsonProperty("summary")
    private Summary summary;

    @JsonProperty("start")
    public Start getStart() {
        return start;
    }

    @JsonProperty("start")
    public void setStart(Start start) {
        this.start = start;
    }

    @JsonProperty("end")
    public End getEnd() {
        return end;
    }

    @JsonProperty("end")
    public void setEnd(End end) {
        this.end = end;
    }

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(Integer length) {
        this.length = length;
    }

    @JsonProperty("travelTime")
    public Integer getTravelTime() {
        return travelTime;
    }

    @JsonProperty("travelTime")
    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    @JsonProperty("maneuver")
    public List<Maneuver> getManeuver() {
        return maneuver;
    }

    @JsonProperty("maneuver")
    public void setManeuver(List<Maneuver> maneuver) {
        this.maneuver = maneuver;
    }

    @JsonProperty("link")
    public List<Link> getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(List<Link> link) {
        this.link = link;
    }

    @JsonProperty("boundingBox")
    public BoundingBox__ getBoundingBox() {
        return boundingBox;
    }

    @JsonProperty("boundingBox")
    public void setBoundingBox(BoundingBox__ boundingBox) {
        this.boundingBox = boundingBox;
    }

    @JsonProperty("shape")
    public List<String> getShape() {
        return shape;
    }

    @JsonProperty("shape")
    public void setShape(List<String> shape) {
        this.shape = shape;
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

    @JsonProperty("summary")
    public Summary getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

}
