package com.taxitool.facade;

import com.taxitool.model.geocoding.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GeoCodingFacade {

    public Double getGeoPosition(GeoModel geoModel) {
        Double position = -1.0;
        Optional<View> viewModel = geoModel.getResponse().getView().stream().filter(v -> !v.getResult().isEmpty()).findFirst();
        if (viewModel.isPresent()) {
            List<Location> locations = viewModel.get().getResult().stream().map(Result::getLocation).collect(Collectors.toList());
            List navigationPositions = new ArrayList();
            locations.stream().findFirst().map(Location::getNavigationPosition).ifPresent(navigationPositions::add);
            if(!navigationPositions.isEmpty()){
                position=((NavigationPosition)navigationPositions.get(0)).getLatitude();
            }
        }
        return position;
    }
}
