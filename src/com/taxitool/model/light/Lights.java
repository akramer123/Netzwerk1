
package com.taxitool.model.light;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "state",
    "swupdate",
    "type",
    "name",
    "modelid",
    "manufacturername",
    "productname",
    "capabilities",
    "config",
    "uniqueid",
    "swversion"
})
public class Lights {

    @JsonProperty("state")
    private State state;
    @JsonProperty("swupdate")
    private Swupdate swupdate;
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("modelid")
    private String modelid;
    @JsonProperty("manufacturername")
    private String manufacturername;
    @JsonProperty("productname")
    private String productname;
    @JsonProperty("capabilities")
    private Capabilities capabilities;
    @JsonProperty("config")
    private Config config;
    @JsonProperty("uniqueid")
    private String uniqueid;
    @JsonProperty("swversion")
    private String swversion;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("state")
    public State getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(State state) {
        this.state = state;
    }

    @JsonProperty("swupdate")
    public Swupdate getSwupdate() {
        return swupdate;
    }

    @JsonProperty("swupdate")
    public void setSwupdate(Swupdate swupdate) {
        this.swupdate = swupdate;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("modelid")
    public String getModelid() {
        return modelid;
    }

    @JsonProperty("modelid")
    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    @JsonProperty("manufacturername")
    public String getManufacturername() {
        return manufacturername;
    }

    @JsonProperty("manufacturername")
    public void setManufacturername(String manufacturername) {
        this.manufacturername = manufacturername;
    }

    @JsonProperty("productname")
    public String getProductname() {
        return productname;
    }

    @JsonProperty("productname")
    public void setProductname(String productname) {
        this.productname = productname;
    }

    @JsonProperty("capabilities")
    public Capabilities getCapabilities() {
        return capabilities;
    }

    @JsonProperty("capabilities")
    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    @JsonProperty("config")
    public Config getConfig() {
        return config;
    }

    @JsonProperty("config")
    public void setConfig(Config config) {
        this.config = config;
    }

    @JsonProperty("uniqueid")
    public String getUniqueid() {
        return uniqueid;
    }

    @JsonProperty("uniqueid")
    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    @JsonProperty("swversion")
    public String getSwversion() {
        return swversion;
    }

    @JsonProperty("swversion")
    public void setSwversion(String swversion) {
        this.swversion = swversion;
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
