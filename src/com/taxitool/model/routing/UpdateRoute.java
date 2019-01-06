
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "response"
})
public class UpdateRoute {

    @JsonProperty("response")
    private UpdateResponse response;

    @JsonProperty("response")
    public UpdateResponse getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(UpdateResponse response) {
        this.response = response;
    }

}
