package com.taxitool.service;

import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.model.geocoding.NavigationPosition;
import com.taxitool.model.routing.MappedPosition;
import com.taxitool.model.routing.Route;
import com.taxitool.model.routing.Waypoint;

import javax.annotation.Resource;
import java.util.List;

public class TaxiService {

    @Resource
    private GeoCodingService geoCodingService;

    @Resource
    private RoutingService routingService;


    public TaxiModel updateAddress(TaxiModel taxiModel, String address) {
        NavigationPosition geoCode = geoCodingService.getGeoCode(address);
        if (geoCode == null) {
            taxiModel.setEndPoint("Address not found");
            taxiModel.setEstimatedTime(null);
        } else {
            taxiModel.setLatitude(geoCode.getLatitude());
            taxiModel.setLongtitude(geoCode.getLongitude());
        }
        return taxiModel;
    }

    public boolean updateRoute(TaxiModel taxiModel, String address) {
        if (taxiModel.getRoute() != null) {
            updateAddress(taxiModel, address);
            List<Waypoint> waypoints = taxiModel.getRoute().getResponse().getRoute().get(0).getWaypoint();
            if (!waypoints.isEmpty()) {
                MappedPosition position = waypoints.get(0).getMappedPosition();
                Route route = routingService.calculateRoute(taxiModel, position.getLatitude(), position.getLongitude());
                taxiModel.setRoute(route);
                taxiModel.setStatus(onTime(taxiModel));
                return true;
            }
        }
        return false;
    }

    public TaxiStatus onTime(TaxiModel taxiModel) {
        if(taxiModel.getRoute()!=null) {
            int travelTime = taxiModel.getRoute().getResponse().getRoute().get(0).getLeg().get(0).getSummary().getTravelTime();
            long arrivalMillis = System.currentTimeMillis() + (travelTime * 1000);

            if (taxiModel.getEstimatedTime() != null) {
                boolean onTime = taxiModel.getEstimatedTime().getTime() > arrivalMillis;
                if (onTime) {
                    return TaxiStatus.ONTIME;
                } else {
                    return TaxiStatus.DELAY;
                }
            }
        }
        return TaxiStatus.FREE;
    }

    public void setGeoCodingService(GeoCodingService geoCodingService) {
        this.geoCodingService = geoCodingService;
    }

    public void setRoutingService(RoutingService routingService) {
        this.routingService = routingService;
    }
}
