package com.taxitool.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxitool.ParameterStringBuilder;
import com.taxitool.facade.GeoCodingFacade;
import com.taxitool.model.geocoding.GeoModel;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GeoCodingService {

    @Resource
    private GeoCodingFacade geoCodingFacade;

    private String EXAMPLE_CALL = "/6.2/geocode.json\n" +
            "  ?app_id=F7iYpiXSc8wnCRDYfMUQ\n" +
            "  &app_code=jpDlJdgGk5ms7QQH-NvpUQ\n" +
            "  &searchtext=425+W+Randolph+Chicago";

    private final String BASEURL_GEOCODE = "https://geocoder.api.here.com";
    private final String GEOCODE_VERSION = "6.2";
    private final String GEOCODE_RETURNFILE = "geocode.json";

    public Double getGeoCode(String searchText) {
        URL url = null;
        try {
            url = new URL(BASEURL_GEOCODE + "/" + GEOCODE_VERSION + "/" + GEOCODE_RETURNFILE);

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setRequestMethod("GET");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("app_id", "F7iYpiXSc8wnCRDYfMUQ");
            parameters.put("app_code", "jpDlJdgGk5ms7QQH");
            parameters.put("searchtext", searchText);

            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            int status = con.getResponseCode();

            BufferedReader in = new BufferedReader(
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

            //call getGeoPosition von GeoCodingFacade
            return 0.0;

        } catch (ProtocolException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
