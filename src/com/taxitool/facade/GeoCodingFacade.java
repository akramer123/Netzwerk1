/**
 * @author Andreas Kramer
 */
package com.taxitool.facade;

import com.taxitool.model.geocoding.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GeoCodingFacade {

    public NavigationPosition getGeoPosition(GeoModel geoModel) {
        List<NavigationPosition> navigationPositions = new ArrayList();
        Optional<View> viewModel = geoModel.getResponse().getView().stream().filter(v -> !v.getResult().isEmpty()).findFirst();
        if (viewModel.isPresent()) {
            List<Location> locations = viewModel.get().getResult().stream().map(Result::getLocation).collect(Collectors.toList());

            for(Location loc : locations){
                navigationPositions.addAll(loc.getNavigationPosition());
            }
        }
        if(!navigationPositions.isEmpty()) {
            return navigationPositions.get(0);
        }
        return null;
    }
}
