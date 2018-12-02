
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "topLeft",
    "bottomRight"
})
public class BoundingBox__ {

    @JsonProperty("topLeft")
    private TopLeft__ topLeft;
    @JsonProperty("bottomRight")
    private BottomRight__ bottomRight;

    @JsonProperty("topLeft")
    public TopLeft__ getTopLeft() {
        return topLeft;
    }

    @JsonProperty("topLeft")
    public void setTopLeft(TopLeft__ topLeft) {
        this.topLeft = topLeft;
    }

    @JsonProperty("bottomRight")
    public BottomRight__ getBottomRight() {
        return bottomRight;
    }

    @JsonProperty("bottomRight")
    public void setBottomRight(BottomRight__ bottomRight) {
        this.bottomRight = bottomRight;
    }

}
