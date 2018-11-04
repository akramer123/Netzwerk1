package com.taxitool.service;

import com.taxitool.TaxiConstants;

public class RoutingService {

    private final String APIURL = TaxiConstants.BASEURL+TaxiConstants.PATH+TaxiConstants.CALCULATEROUTE;

    private String APPID = "F7iYpiXSc8wnCRDYfMUQ";
    private String APPCODE = "jpDlJdgGk5ms7QQH-NvpUQ";

    private String EXAMPLE_CALL = "https://route.api.here.com/routing/7.2/calculateroute.json\n" +
            "?app_id=F7iYpiXSc8wnCRDYfMUQ\n" +
            "&app_code=jpDlJdgGk5ms7QQH-NvpUQ\n" +
            "&waypoint0=geo!52.5,13.4\n" +
            "&waypoint1=geo!52.5,13.45\n" +
            "&mode=fastest;car;traffic:disabled";



}
