
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "metaInfo",
    "route",
    "language"
})
public class Response {

    @JsonProperty("metaInfo")
    private MetaInfo metaInfo;
    @JsonProperty("route")
    private List<Route_> route = null;
    @JsonProperty("language")
    private String language;

    @JsonProperty("metaInfo")
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    @JsonProperty("metaInfo")
    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    @JsonProperty("route")
    public List<Route_> getRoute() {
        return route;
    }

    @JsonProperty("route")
    public void setRoute(List<Route_> route) {
        this.route = route;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

}
