
package com.taxitool.model.light;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "renderer",
    "proxy"
})
public class Streaming {

    @JsonProperty("renderer")
    private Boolean renderer;
    @JsonProperty("proxy")
    private Boolean proxy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("renderer")
    public Boolean getRenderer() {
        return renderer;
    }

    @JsonProperty("renderer")
    public void setRenderer(Boolean renderer) {
        this.renderer = renderer;
    }

    @JsonProperty("proxy")
    public Boolean getProxy() {
        return proxy;
    }

    @JsonProperty("proxy")
    public void setProxy(Boolean proxy) {
        this.proxy = proxy;
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
