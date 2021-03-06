
package com.taxitool.model.light;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mindimlevel",
    "maxlumen",
    "colorgamuttype",
    "colorgamut"
})
public class Control {

    @JsonProperty("mindimlevel")
    private Integer mindimlevel;
    @JsonProperty("maxlumen")
    private Integer maxlumen;
    @JsonProperty("colorgamuttype")
    private String colorgamuttype;
    @JsonProperty("colorgamut")
    private List<List<Double>> colorgamut = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("mindimlevel")
    public Integer getMindimlevel() {
        return mindimlevel;
    }

    @JsonProperty("mindimlevel")
    public void setMindimlevel(Integer mindimlevel) {
        this.mindimlevel = mindimlevel;
    }

    @JsonProperty("maxlumen")
    public Integer getMaxlumen() {
        return maxlumen;
    }

    @JsonProperty("maxlumen")
    public void setMaxlumen(Integer maxlumen) {
        this.maxlumen = maxlumen;
    }

    @JsonProperty("colorgamuttype")
    public String getColorgamuttype() {
        return colorgamuttype;
    }

    @JsonProperty("colorgamuttype")
    public void setColorgamuttype(String colorgamuttype) {
        this.colorgamuttype = colorgamuttype;
    }

    @JsonProperty("colorgamut")
    public List<List<Double>> getColorgamut() {
        return colorgamut;
    }

    @JsonProperty("colorgamut")
    public void setColorgamut(List<List<Double>> colorgamut) {
        this.colorgamut = colorgamut;
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
