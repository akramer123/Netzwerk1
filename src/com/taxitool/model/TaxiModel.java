/**
 * @author Andreas Kramer
 */
package com.taxitool.model;

import com.taxitool.model.routing.Route;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TaxiModel implements Cloneable{

    private int id;
    private TaxiStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date estimatedTime;
    private String address;
    private double latitude;
    private double longtitude;
    private Route route;
    private String endPoint;

    public TaxiModel() {
        this.status = TaxiStatus.FREE;
    }

    public TaxiModel(TaxiModel copy){
        this.id=copy.getId();
        this.status=copy.getStatus();
        this.estimatedTime=copy.getEstimatedTime();
        this.address=copy.getAddress();
        this.latitude=copy.getLatitude();
        this.longtitude=copy.getLongtitude();
        this.route=copy.getRoute();
        this.endPoint=copy.getEndPoint();
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

    @Override
    public TaxiModel clone() {
        return new TaxiModel(this);
    }
}
