
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "linkId",
    "shape",
    "firstPoint",
    "lastPoint",
    "length",
    "remainDistance",
    "remainTime",
    "nextLink",
    "maneuver",
    "speedLimit",
    "dynamicSpeedInfo",
    "flags",
    "functionalClass",
    "roadNumber",
    "timezone",
    "roadName",
    "consumption",
    "_type"
})
public class Link {

    @JsonProperty("linkId")
    private String linkId;
    @JsonProperty("shape")
    private List<String> shape = null;
    @JsonProperty("firstPoint")
    private Integer firstPoint;
    @JsonProperty("lastPoint")
    private Integer lastPoint;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("remainDistance")
    private Integer remainDistance;
    @JsonProperty("remainTime")
    private Integer remainTime;
    @JsonProperty("nextLink")
    private String nextLink;
    @JsonProperty("maneuver")
    private String maneuver;
    @JsonProperty("speedLimit")
    private Double speedLimit;
    @JsonProperty("dynamicSpeedInfo")
    private DynamicSpeedInfo dynamicSpeedInfo;
    @JsonProperty("flags")
    private List<String> flags = null;
    @JsonProperty("functionalClass")
    private Integer functionalClass;
    @JsonProperty("roadNumber")
    private String roadNumber;
    @JsonProperty("timezone")
    private String timezone;
    @JsonProperty("roadName")
    private String roadName;
    @JsonProperty("consumption")
    private Integer consumption;
    @JsonProperty("_type")
    private String type;

    @JsonProperty("linkId")
    public String getLinkId() {
        return linkId;
    }

    @JsonProperty("linkId")
    public void setLinkId(String linkId) {
        this.linkId = linkId;
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

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(Integer length) {
        this.length = length;
    }

    @JsonProperty("remainDistance")
    public Integer getRemainDistance() {
        return remainDistance;
    }

    @JsonProperty("remainDistance")
    public void setRemainDistance(Integer remainDistance) {
        this.remainDistance = remainDistance;
    }

    @JsonProperty("remainTime")
    public Integer getRemainTime() {
        return remainTime;
    }

    @JsonProperty("remainTime")
    public void setRemainTime(Integer remainTime) {
        this.remainTime = remainTime;
    }

    @JsonProperty("nextLink")
    public String getNextLink() {
        return nextLink;
    }

    @JsonProperty("nextLink")
    public void setNextLink(String nextLink) {
        this.nextLink = nextLink;
    }

    @JsonProperty("maneuver")
    public String getManeuver() {
        return maneuver;
    }

    @JsonProperty("maneuver")
    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    @JsonProperty("speedLimit")
    public Double getSpeedLimit() {
        return speedLimit;
    }

    @JsonProperty("speedLimit")
    public void setSpeedLimit(Double speedLimit) {
        this.speedLimit = speedLimit;
    }

    @JsonProperty("dynamicSpeedInfo")
    public DynamicSpeedInfo getDynamicSpeedInfo() {
        return dynamicSpeedInfo;
    }

    @JsonProperty("dynamicSpeedInfo")
    public void setDynamicSpeedInfo(DynamicSpeedInfo dynamicSpeedInfo) {
        this.dynamicSpeedInfo = dynamicSpeedInfo;
    }

    @JsonProperty("flags")
    public List<String> getFlags() {
        return flags;
    }

    @JsonProperty("flags")
    public void setFlags(List<String> flags) {
        this.flags = flags;
    }

    @JsonProperty("functionalClass")
    public Integer getFunctionalClass() {
        return functionalClass;
    }

    @JsonProperty("functionalClass")
    public void setFunctionalClass(Integer functionalClass) {
        this.functionalClass = functionalClass;
    }

    @JsonProperty("roadNumber")
    public String getRoadNumber() {
        return roadNumber;
    }

    @JsonProperty("roadNumber")
    public void setRoadNumber(String roadNumber) {
        this.roadNumber = roadNumber;
    }

    @JsonProperty("timezone")
    public String getTimezone() {
        return timezone;
    }

    @JsonProperty("timezone")
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @JsonProperty("roadName")
    public String getRoadName() {
        return roadName;
    }

    @JsonProperty("roadName")
    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    @JsonProperty("consumption")
    public Integer getConsumption() {
        return consumption;
    }

    @JsonProperty("consumption")
    public void setConsumption(Integer consumption) {
        this.consumption = consumption;
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
