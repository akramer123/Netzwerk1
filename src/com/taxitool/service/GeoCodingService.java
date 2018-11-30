package com.taxitool.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxitool.TaxiConstants;
import com.taxitool.endpoint.DefaultEndpointService;
import com.taxitool.facade.GeoCodingFacade;
import com.taxitool.model.geocoding.GeoModel;
import com.taxitool.model.geocoding.NavigationPosition;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GeoCodingService {

    @Resource
    private GeoCodingFacade geoCodingFacade;
    @Resource
    private DefaultEndpointService endpointService;

    public NavigationPosition getGeoCode(String searchText) {

        String apiUrlString = TaxiConstants.BASEURL_GEOCODE + "/" + TaxiConstants.GEOCODE_VERSION + "/" + TaxiConstants.GEOCODE_RETURNFILE;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("searchtext", searchText);

        String content = endpointService.callRESTMethodHERE(apiUrlString, parameters);

        ObjectMapper mapper = new ObjectMapper();
        GeoModel geoModel = null;
        try {
            geoModel = mapper.readValue(content, GeoModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return geoCodingFacade.getGeoPosition(geoModel);
    }


    public void setGeoCodingFacade(GeoCodingFacade geoCodingFacade) {
        this.geoCodingFacade = geoCodingFacade;
    }

    public void setEndpointService(DefaultEndpointService endpointService) {
        this.endpointService = endpointService;
    }
}
