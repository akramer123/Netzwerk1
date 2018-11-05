
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
    "type",
    "transportModes",
    "trafficMode",
    "feature"
})
public class Mode {

    @JsonProperty("type")
    private String type;
    @JsonProperty("transportModes")
    private List<String> transportModes = null;
    @JsonProperty("trafficMode")
    private String trafficMode;
    @JsonProperty("feature")
    private List<Object> feature = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("transportModes")
    public List<String> getTransportModes() {
        return transportModes;
    }

    @JsonProperty("transportModes")
    public void setTransportModes(List<String> transportModes) {
        this.transportModes = transportModes;
    }

    @JsonProperty("trafficMode")
    public String getTrafficMode() {
        return trafficMode;
    }

    @JsonProperty("trafficMode")
    public void setTrafficMode(String trafficMode) {
        this.trafficMode = trafficMode;
    }

    @JsonProperty("feature")
    public List<Object> getFeature() {
        return feature;
    }

    @JsonProperty("feature")
    public void setFeature(List<Object> feature) {
        this.feature = feature;
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
