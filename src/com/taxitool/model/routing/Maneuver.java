
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
    "position",
    "instruction",
    "travelTime",
    "length",
    "shape",
    "note",
    "id",
    "_type"
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
    @JsonProperty("note")
    private List<Note> note = null;
    @JsonProperty("id")
    private String id;
    @JsonProperty("_type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("note")
    public List<Note> getNote() {
        return note;
    }

    @JsonProperty("note")
    public void setNote(List<Note> note) {
        this.note = note;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
