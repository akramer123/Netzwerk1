package com.taxitool.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxitool.TaxiConstants;
import com.taxitool.endpoint.DefaultEndpointService;
import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.model.routing.Route;
import com.taxitool.model.routing.Route_;
import com.taxitool.model.routing.UpdateRoute;
import com.taxitool.model.routing.Waypoint;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutingService {

    private final String APIURL = TaxiConstants.BASEURL + "/" + TaxiConstants.PATH + "/" + TaxiConstants.CALCULATEROUTE;
    private final String SYNCAPIURL = TaxiConstants.BASEURL + "/" + TaxiConstants.PATH + "/" + "getroute.json";

    @Resource
    private DefaultEndpointService endpointService;
    @Resource
    private TaxiService taxiService;

    public Route calculateRoute(TaxiModel taxiModel, double latitude, double longtitude) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("waypoint0", "geo!" + taxiModel.getLatitude() + "," + taxiModel.getLongtitude());
        parameters.put("waypoint1", "geo!" + latitude + "," + longtitude);
        parameters.put("mode", "fastest;car;traffic:enabled");
        parameters.put("representation", "navigation");
        parameters.put("routeattributes", "routeId");

        String content = endpointService.callRESTMethodHERE(APIURL, parameters);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Route route = null;
        try {
            route = mapper.readValue(content, Route.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkEvery5Seconds(taxiModel);
        return route;
    }

    private void checkEvery5Seconds(TaxiModel taxi) {

        Thread t = new Thread(() -> {

            TaxiModel handledTaxi = taxi.clone();
            while (handledTaxi.getStatus() == TaxiStatus.ONTIME) {

                try {
                    Thread.sleep(3000);
                    TaxiModel taxiModel = DatabaseService.getTaxi(handledTaxi.getId());
                    System.out.println("Checking route of taxi " + handledTaxi.getId());
                    syncRoute(handledTaxi);
                    taxiModel.setStatus(taxiService.onTime(taxiModel));
                    DatabaseService.addTaxi(taxiModel);
                    handledTaxi.setStatus(taxiModel.getStatus());
                } catch (InterruptedException e) {
                    System.out.println("Thread is interrupted");
                }
            }
        });
        t.start();
    }


    public void syncRoute(TaxiModel taxiModel) {
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
        UpdateRoute route = null;
        try {
            route = mapper.readValue(content, UpdateRoute.class);
            List<Route_> routes = taxiModel.getRoute().getResponse().getRoute();
            routes.set(0, route.getResponse().getRoute());
            taxiModel.getRoute().getResponse().setRoute(routes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setEndpointService(DefaultEndpointService endpointService) {
        this.endpointService = endpointService;
    }

}
