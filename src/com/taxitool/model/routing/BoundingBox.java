
package com.taxitool.model.routing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "topLeft",
    "bottomRight"
})
public class BoundingBox {

    @JsonProperty("topLeft")
    private TopLeft topLeft;
    @JsonProperty("bottomRight")
    private BottomRight bottomRight;

    @JsonProperty("topLeft")
    public TopLeft getTopLeft() {
        return topLeft;
    }

    @JsonProperty("topLeft")
    public void setTopLeft(TopLeft topLeft) {
        this.topLeft = topLeft;
    }

    @JsonProperty("bottomRight")
    public BottomRight getBottomRight() {
        return bottomRight;
    }

    @JsonProperty("bottomRight")
    public void setBottomRight(BottomRight bottomRight) {
        this.bottomRight = bottomRight;
    }

}
