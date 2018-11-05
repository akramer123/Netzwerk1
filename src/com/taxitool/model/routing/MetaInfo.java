
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
    "timestamp",
    "mapVersion",
    "moduleVersion",
    "interfaceVersion",
    "availableMapVersion"
})
public class MetaInfo {

    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("mapVersion")
    private String mapVersion;
    @JsonProperty("moduleVersion")
    private String moduleVersion;
    @JsonProperty("interfaceVersion")
    private String interfaceVersion;
    @JsonProperty("availableMapVersion")
    private List<String> availableMapVersion = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("mapVersion")
    public String getMapVersion() {
        return mapVersion;
    }

    @JsonProperty("mapVersion")
    public void setMapVersion(String mapVersion) {
        this.mapVersion = mapVersion;
    }

    @JsonProperty("moduleVersion")
    public String getModuleVersion() {
        return moduleVersion;
    }

    @JsonProperty("moduleVersion")
    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    @JsonProperty("interfaceVersion")
    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    @JsonProperty("interfaceVersion")
    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    @JsonProperty("availableMapVersion")
    public List<String> getAvailableMapVersion() {
        return availableMapVersion;
    }

    @JsonProperty("availableMapVersion")
    public void setAvailableMapVersion(List<String> availableMapVersion) {
        this.availableMapVersion = availableMapVersion;
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
