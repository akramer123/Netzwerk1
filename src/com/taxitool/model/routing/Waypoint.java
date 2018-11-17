
package com.taxitool.model.routing;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "linkId",
    "mappedPosition",
    "originalPosition",
    "type",
    "spot",
    "sideOfStreet",
    "mappedRoadName",
    "label",
    "shapeIndex"
})
public class Waypoint {

    @JsonProperty("linkId")
    private String linkId;
    @JsonProperty("mappedPosition")
    private MappedPosition mappedPosition;
    @JsonProperty("originalPosition")
    private OriginalPosition originalPosition;
    @JsonProperty("type")
    private String type;
    @JsonProperty("spot")
    private Integer spot;
    @JsonProperty("sideOfStreet")
    private String sideOfStreet;
    @JsonProperty("mappedRoadName")
    private String mappedRoadName;
    @JsonProperty("label")
    private String label;
    @JsonProperty("shapeIndex")
    private Integer shapeIndex;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("linkId")
    public String getLinkId() {
        return linkId;
    }

    @JsonProperty("linkId")
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @JsonProperty("mappedPosition")
    public MappedPosition getMappedPosition() {
        return mappedPosition;
    }

    @JsonProperty("mappedPosition")
    public void setMappedPosition(MappedPosition mappedPosition) {
        this.mappedPosition = mappedPosition;
    }

    @JsonProperty("originalPosition")
    public OriginalPosition getOriginalPosition() {
        return originalPosition;
    }

    @JsonProperty("originalPosition")
    public void setOriginalPosition(OriginalPosition originalPosition) {
        this.originalPosition = originalPosition;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("spot")
    public Integer getSpot() {
        return spot;
    }

    @JsonProperty("spot")
    public void setSpot(Integer spot) {
        this.spot = spot;
    }

    @JsonProperty("sideOfStreet")
    public String getSideOfStreet() {
        return sideOfStreet;
    }

    @JsonProperty("sideOfStreet")
    public void setSideOfStreet(String sideOfStreet) {
        this.sideOfStreet = sideOfStreet;
    }

    @JsonProperty("mappedRoadName")
    public String getMappedRoadName() {
        return mappedRoadName;
    }

    @JsonProperty("mappedRoadName")
    public void setMappedRoadName(String mappedRoadName) {
        this.mappedRoadName = mappedRoadName;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("shapeIndex")
    public Integer getShapeIndex() {
        return shapeIndex;
    }

    @JsonProperty("shapeIndex")
    public void setShapeIndex(Integer shapeIndex) {
        this.shapeIndex = shapeIndex;
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
