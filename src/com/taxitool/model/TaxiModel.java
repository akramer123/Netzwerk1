package com.taxitool.model;

import com.taxitool.model.routing.Route;

import java.util.Date;

public class TaxiModel {

    private int id;
    private TaxiStatus status;
    private Date estimatedTime;
    private String address;
    private double latitude;
    private double longtitude;
    private Route route;
    private String endPoint;

    public TaxiModel(){
        this.status=TaxiStatus.FREE;
    }

    public String getAddress() {
        return address;
    }

    public Date getEstimatedTime() {
        return estimatedTime;
    }

    public TaxiStatus getStatus() {
        return status;
    }

    public void setStatus(TaxiStatus status) {
        this.status = status;
    }

    public void setEstimatedTime(Date estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
