package com.taxitool.service;

import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.model.geocoding.NavigationPosition;
import com.taxitool.model.routing.MappedPosition;
import com.taxitool.model.routing.OriginalPosition;
import com.taxitool.model.routing.Route;
import com.taxitool.model.routing.Waypoint;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaxiService {

    @Resource
    private GeoCodingService geoCodingService;

    @Resource
    private RoutingService routingService;


    public TaxiModel updateAddress(TaxiModel taxiModel, String address) {
        NavigationPosition geoCode = geoCodingService.getGeoCode(address);
        if (geoCode == null) {
            taxiModel.setAddress("Address not found");
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
        int travelTime = taxiModel.getRoute().getResponse().getRoute().get(0).getLeg().get(0).getSummary().getTravelTime();
        long arrivalMillis = System.currentTimeMillis()+(travelTime*1000);

        if (taxiModel.getEstimatedTime() != null) {
            boolean onTime = taxiModel.getEstimatedTime().getTime() > arrivalMillis;
            if (onTime) {
                return TaxiStatus.ONTIME;
            } else {
                return TaxiStatus.DELAY;
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
