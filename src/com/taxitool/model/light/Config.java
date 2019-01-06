
package com.taxitool.model.light;


import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "archetype",
    "function",
    "direction"
})
public class Config {

    @JsonProperty("archetype")
    private String archetype;
    @JsonProperty("function")
    private String function;
    @JsonProperty("direction")
    private String direction;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("archetype")
    public String getArchetype() {
        return archetype;
    }

    @JsonProperty("archetype")
    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    @JsonProperty("function")
    public String getFunction() {
        return function;
    }

    @JsonProperty("function")
    public void setFunction(String function) {
        this.function = function;
    }

    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }

    @JsonProperty("direction")
    public void setDirection(String direction) {
        this.direction = direction;
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
