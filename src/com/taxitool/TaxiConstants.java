package com.taxitool;

import org.springframework.beans.factory.annotation.Value;

public final class TaxiConstants {

    @Value("${routeAPI.url}")
    public static String BASEURL;
    @Value("${routeAPI.version}")
    public static String PATH;
    @Value("${routeAPI.calculate}")
    public static String CALCULATEROUTE;

    @Value("${geoAPI.url}")
    public static String BASEURL_GEOCODE;
    @Value("${routeAPI.version}")
    public static String GEOCODE_VERSION;
    @Value("${routeAPI.geocode}")
    public static String GEOCODE_RETURNFILE;
}
