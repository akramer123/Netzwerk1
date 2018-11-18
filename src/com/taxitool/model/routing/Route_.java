
package com.taxitool.model.routing;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "waypoint",
    "mode",
    "shape",
    "boundingBox",
    "leg",
    "note",
    "maneuverGroup",
    "incident",
    "label",
    "zone"
})
public class Route_ {

    @JsonProperty("waypoint")
    private List<Waypoint> waypoint = null;
    @JsonProperty("mode")
    private Mode mode;
    @JsonProperty("shape")
    private List<String> shape = null;
    @JsonProperty("boundingBox")
    private BoundingBox boundingBox;
    @JsonProperty("leg")
    private List<Leg> leg = null;
    @JsonProperty("note")
    private List<Object> note = null;
    @JsonProperty("maneuverGroup")
    private List<ManeuverGroup> maneuverGroup = null;
    @JsonProperty("incident")
    private List<Incident> incident = null;
    @JsonProperty("label")
    private List<String> label = null;
    @JsonProperty("zone")
    private List<Zone> zone = null;
    @JsonProperty("routeId")
    private String routeId = null;

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

    @JsonProperty("boundingBox")
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @JsonProperty("boundingBox")
    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    @JsonProperty("leg")
    public List<Leg> getLeg() {
        return leg;
    }

    @JsonProperty("leg")
    public void setLeg(List<Leg> leg) {
        this.leg = leg;
    }

    @JsonProperty("note")
    public List<Object> getNote() {
        return note;
    }

    @JsonProperty("note")
    public void setNote(List<Object> note) {
        this.note = note;
    }

    @JsonProperty("maneuverGroup")
    public List<ManeuverGroup> getManeuverGroup() {
        return maneuverGroup;
    }

    @JsonProperty("maneuverGroup")
    public void setManeuverGroup(List<ManeuverGroup> maneuverGroup) {
        this.maneuverGroup = maneuverGroup;
    }

    @JsonProperty("incident")
    public List<Incident> getIncident() {
        return incident;
    }

    @JsonProperty("incident")
    public void setIncident(List<Incident> incident) {
        this.incident = incident;
    }

    @JsonProperty("label")
    public List<String> getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(List<String> label) {
        this.label = label;
    }

    @JsonProperty("zone")
    public List<Zone> getZone() {
        return zone;
    }

    @JsonProperty("zone")
    public void setZone(List<Zone> zone) {
        this.zone = zone;
    }

    @JsonProperty("routeId")
    public String getRouteId() {
        return routeId;
    }

    @JsonProperty("routeId")
    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }
}
