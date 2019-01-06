
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "from",
    "until"
})
public class ValidityPeriod {

    @JsonProperty("from")
    private String from;
    @JsonProperty("until")
    private String until;

    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    @JsonProperty("from")
    public void setFrom(String from) {
        this.from = from;
    }

    @JsonProperty("until")
    public String getUntil() {
        return until;
    }

    @JsonProperty("until")
    public void setUntil(String until) {
        this.until = until;
    }

}
