package com.taxitool.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxitool.TaxiConstants;
import com.taxitool.endpoint.DefaultEndpointService;
import com.taxitool.model.TaxiModel;
import com.taxitool.model.routing.Route;
import com.taxitool.model.routing.Waypoint;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RoutingService {

    private final String APIURL = TaxiConstants.BASEURL + "/" + TaxiConstants.PATH + "/" + TaxiConstants.CALCULATEROUTE;
    private final String SYNCAPIURL = TaxiConstants.BASEURL + "/" + TaxiConstants.PATH + "/" + "getRoute.json";

    @Resource
    private DefaultEndpointService endpointService;

    public Route calculateRoute(TaxiModel taxiModel, double latitude, double longtitude) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("waypoint0", "geo!" + taxiModel.getLatitude() + "," + taxiModel.getLongtitude());
        parameters.put("waypoint1", "geo!" + latitude + "," + longtitude);
        parameters.put("mode", "fastest;car;traffic:enabled");
        parameters.put("representation", "navigation");
        parameters.put("routeattributes","routeId");

        String content = endpointService.callRESTMethodHERE(APIURL, parameters);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Route route = null;
        try {
            route = mapper.readValue(content, Route.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return route;
    }


    public Route syncRoute(TaxiModel taxiModel) {
        Waypoint first = taxiModel.getRoute().getResponse().getRoute().get(0).getWaypoint().get(0);
        Waypoint second = taxiModel.getRoute().getResponse().getRoute().get(0).getWaypoint().get(1);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("routeId", taxiModel.getRoute().getResponse().getRoute().get(0).getRouteId());
        parameters.put("waypoint0", "geo!" + first.getOriginalPosition().getLatitude() + "," + first.getOriginalPosition().getLongitude());
        parameters.put("waypoint1", "geo!" + second.getMappedPosition().getLatitude() + "," + second.getMappedPosition().getLongitude());
        parameters.put("mode", "fastest;car;traffic:enabled");
        parameters.put("representation", "navigation");

        String content = endpointService.callRESTMethodHERE(SYNCAPIURL, parameters);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Route route = null;
        try {
            route = mapper.readValue(content, Route.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return route;
    }

    public void setEndpointService(DefaultEndpointService endpointService) {
        this.endpointService = endpointService;
    }

}