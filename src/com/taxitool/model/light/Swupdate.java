package com.taxitool.model.light;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "state",
    "lastinstall"
})
public class Swupdate {

    @JsonProperty("state")
    private String state;
    @JsonProperty("lastinstall")
    private String lastinstall;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("lastinstall")
    public String getLastinstall() {
        return lastinstall;
    }

    @JsonProperty("lastinstall")
    public void setLastinstall(String lastinstall) {
        this.lastinstall = lastinstall;
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
