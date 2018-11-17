
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "firstManeuver",
    "lastManeuver",
    "mode",
    "summaryDescription",
    "arrivalDescription"
})
public class ManeuverGroup {

    @JsonProperty("firstManeuver")
    private String firstManeuver;
    @JsonProperty("lastManeuver")
    private String lastManeuver;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("summaryDescription")
    private String summaryDescription;
    @JsonProperty("arrivalDescription")
    private String arrivalDescription;

    @JsonProperty("firstManeuver")
    public String getFirstManeuver() {
        return firstManeuver;
    }

    @JsonProperty("firstManeuver")
    public void setFirstManeuver(String firstManeuver) {
        this.firstManeuver = firstManeuver;
    }

    @JsonProperty("lastManeuver")
    public String getLastManeuver() {
        return lastManeuver;
    }

    @JsonProperty("lastManeuver")
    public void setLastManeuver(String lastManeuver) {
        this.lastManeuver = lastManeuver;
    }

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("summaryDescription")
    public String getSummaryDescription() {
        return summaryDescription;
    }

    @JsonProperty("summaryDescription")
    public void setSummaryDescription(String summaryDescription) {
        this.summaryDescription = summaryDescription;
    }

    @JsonProperty("arrivalDescription")
    public String getArrivalDescription() {
        return arrivalDescription;
    }

    @JsonProperty("arrivalDescription")
    public void setArrivalDescription(String arrivalDescription) {
        this.arrivalDescription = arrivalDescription;
    }

}
