package com.taxitool.facade;

import com.taxitool.model.light.State;

public class LightFacade {


    public String getColorMode(State state) {
        return state.getColormode();
    }

    public void setState(State state) {
        state.setOn(false);
    }
}
