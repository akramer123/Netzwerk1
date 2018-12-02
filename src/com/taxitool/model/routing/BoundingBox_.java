
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "topLeft",
    "bottomRight"
})
public class BoundingBox_ {

    @JsonProperty("topLeft")
    private TopLeft_ topLeft;
    @JsonProperty("bottomRight")
    private BottomRight_ bottomRight;

    @JsonProperty("topLeft")
    public TopLeft_ getTopLeft() {
        return topLeft;
    }

    @JsonProperty("topLeft")
    public void setTopLeft(TopLeft_ topLeft) {
        this.topLeft = topLeft;
    }

    @JsonProperty("bottomRight")
    public BottomRight_ getBottomRight() {
        return bottomRight;
    }

    @JsonProperty("bottomRight")
    public void setBottomRight(BottomRight_ bottomRight) {
        this.bottomRight = bottomRight;
    }

}
