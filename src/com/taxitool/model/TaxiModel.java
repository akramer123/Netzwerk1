package com.taxitool.model;

import java.util.Date;

public class TaxiModel {

    private TaxiStatus status;
    private Date estimatedTime;
    private String address;

    public String getAddress() {
        return address;
    }

    public Date getEstimatedTime() {
        return estimatedTime;
    }

    public TaxiStatus getStatus() {
        return status;
    }
}
