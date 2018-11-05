package com.taxitool.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxitool.endpoint.DefaultEndpointService;
import com.taxitool.facade.GeoCodingFacade;
import com.taxitool.model.geocoding.GeoModel;

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

    private String EXAMPLE_CALL = "/6.2/geocode.json\n" +
            "  ?app_id=F7iYpiXSc8wnCRDYfMUQ\n" +
            "  &app_code=jpDlJdgGk5ms7QQH-NvpUQ\n" +
            "  &searchtext=425+W+Randolph+Chicago";

    //TODO: auslagern in project.properties
    private final String BASEURL_GEOCODE = "https://geocoder.api.here.com";
    private final String GEOCODE_VERSION = "6.2";
    private final String GEOCODE_RETURNFILE = "geocode.json";

    public Double getGeoCode(String searchText) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("searchtext", searchText);

        HttpsURLConnection con = endpointService.callRESTMethodHERE(parameters);

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
