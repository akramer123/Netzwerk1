package com.taxitool.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxitool.TaxiConstants;
import com.taxitool.endpoint.DefaultEndpointService;
import com.taxitool.facade.GeoCodingFacade;
import com.taxitool.model.geocoding.GeoModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GeoCodingService {

    @Resource
    private GeoCodingFacade geoCodingFacade;
    @Resource
    private DefaultEndpointService endpointService;

    public Double getGeoCode(String searchText) {

        //TODO: add Threads

        String apiUrlString = TaxiConstants.BASEURL_GEOCODE + "/" + TaxiConstants.GEOCODE_VERSION + "/" + TaxiConstants.GEOCODE_RETURNFILE;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("searchtext", searchText);

        HttpsURLConnection con = endpointService.callRESTMethodHERE(apiUrlString, parameters);

        BufferedReader in;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            ObjectMapper mapper = new ObjectMapper();
            GeoModel geoModel = mapper.readValue(content.toString(), GeoModel.class);

            in.close();
            con.disconnect();

            return geoCodingFacade.getGeoPosition(geoModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }


    public void setGeoCodingFacade(GeoCodingFacade geoCodingFacade) {
        this.geoCodingFacade = geoCodingFacade;
    }

    public void setEndpointService(DefaultEndpointService endpointService) {
        this.endpointService = endpointService;
    }
}
