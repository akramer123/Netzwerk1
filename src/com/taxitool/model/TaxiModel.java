package com.taxitool.model;

import java.util.Date;

public class TaxiModel {

    private int id;
    private TaxiStatus status;
    private Date estimatedTime;
    private String address;
    private double latitude;
    private double longtitude;

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
}
