package com.taxitool.facade;

import com.taxitool.model.geocoding.*;
import com.taxitool.model.light.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LightFacade {


    public String getColorMode(State state) {
        return state.getColormode();
    }

    public void setState(State state) {
        state.setOn(false);
    }
}
