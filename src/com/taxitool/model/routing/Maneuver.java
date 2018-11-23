
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "position",
    "instruction",
    "travelTime",
    "length",
    "shape",
    "firstPoint",
    "lastPoint",
    "note",
    "nextManeuver",
    "toLink",
    "boundingBox",
    "shapeQuality",
    "direction",
    "action",
    "roadName",
    "nextRoadName",
    "roadNumber",
    "nextRoadNumber",
    "trafficTime",
    "baseTime",
    "startAngle",
    "id",
    "_type",
    "signPost"
})
public class Maneuver {

    @JsonProperty("position")
    private Position position;
    @JsonProperty("instruction")
    private String instruction;
    @JsonProperty("travelTime")
    private Integer travelTime;
    @JsonProperty("length")
    private Integer length;
    @JsonProperty("shape")
    private List<String> shape = null;
    @JsonProperty("firstPoint")
    private Integer firstPoint;
    @JsonProperty("lastPoint")
    private Integer lastPoint;
    @JsonProperty("note")
    private List<Note> note = null;
    @JsonProperty("nextManeuver")
    private String nextManeuver;
    @JsonProperty("toLink")
    private String toLink;
    @JsonProperty("boundingBox")
    private BoundingBox_ boundingBox;
    @JsonProperty("shapeQuality")
    private String shapeQuality;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("action")
    private String action;
    @JsonProperty("roadName")
    private String roadName;
    @JsonProperty("nextRoadName")
    private String nextRoadName;
    @JsonProperty("roadNumber")
    private String roadNumber;
    @JsonProperty("nextRoadNumber")
    private String nextRoadNumber;
    @JsonProperty("trafficTime")
    private Integer trafficTime;
    @JsonProperty("baseTime")
    private Integer baseTime;
    @JsonProperty("startAngle")
    private Integer startAngle;
    @JsonProperty("id")
    private String id;
    @JsonProperty("_type")
    private String type;
    @JsonProperty("signPost")
    private String signPost;

    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    @JsonProperty("position")
    public void setPosition(Position position) {
        this.position = position;
    }

    @JsonProperty("instruction")
    public String getInstruction() {
        return instruction;
    }

    @JsonProperty("instruction")
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    @JsonProperty("travelTime")
    public Integer getTravelTime() {
        return travelTime;
    }

    @JsonProperty("travelTime")
    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    @JsonProperty("length")
    public Integer getLength() {
        return length;
    }

    @JsonProperty("length")
    public void setLength(Integer length) {
        this.length = length;
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

    @JsonProperty("note")
    public List<Note> getNote() {
        return note;
    }

    @JsonProperty("note")
    public void setNote(List<Note> note) {
        this.note = note;
    }

    @JsonProperty("nextManeuver")
    public String getNextManeuver() {
        return nextManeuver;
    }

    @JsonProperty("nextManeuver")
    public void setNextManeuver(String nextManeuver) {
        this.nextManeuver = nextManeuver;
    }

    @JsonProperty("toLink")
    public String getToLink() {
        return toLink;
    }

    @JsonProperty("toLink")
    public void setToLink(String toLink) {
        this.toLink = toLink;
    }

    @JsonProperty("boundingBox")
    public BoundingBox_ getBoundingBox() {
        return boundingBox;
    }

    @JsonProperty("boundingBox")
    public void setBoundingBox(BoundingBox_ boundingBox) {
        this.boundingBox = boundingBox;
    }

    @JsonProperty("shapeQuality")
    public String getShapeQuality() {
        return shapeQuality;
    }

    @JsonProperty("shapeQuality")
    public void setShapeQuality(String shapeQuality) {
        this.shapeQuality = shapeQuality;
    }

    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }

    @JsonProperty("direction")
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }

    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("roadName")
    public String getRoadName() {
        return roadName;
    }

    @JsonProperty("roadName")
    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    @JsonProperty("nextRoadName")
    public String getNextRoadName() {
        return nextRoadName;
    }

    @JsonProperty("nextRoadName")
    public void setNextRoadName(String nextRoadName) {
        this.nextRoadName = nextRoadName;
    }

    @JsonProperty("roadNumber")
    public String getRoadNumber() {
        return roadNumber;
    }

    @JsonProperty("roadNumber")
    public void setRoadNumber(String roadNumber) {
        this.roadNumber = roadNumber;
    }

    @JsonProperty("nextRoadNumber")
    public String getNextRoadNumber() {
        return nextRoadNumber;
    }

    @JsonProperty("nextRoadNumber")
    public void setNextRoadNumber(String nextRoadNumber) {
        this.nextRoadNumber = nextRoadNumber;
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

    @JsonProperty("startAngle")
    public Integer getStartAngle() {
        return startAngle;
    }

    @JsonProperty("startAngle")
    public void setStartAngle(Integer startAngle) {
        this.startAngle = startAngle;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("_type")
    public String getType() {
        return type;
    }

    @JsonProperty("_type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("signPost")
    public String getSignPost() {
        return signPost;
    }

    @JsonProperty("signPost")
    public void setSignPost(String signPost) {
        this.signPost = signPost;
    }

}
