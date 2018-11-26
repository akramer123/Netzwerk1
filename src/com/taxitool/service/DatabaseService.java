package com.taxitool.service;

import com.taxitool.model.TaxiModel;

import java.util.*;

public class DatabaseService {

    private static Map<Integer, TaxiModel> taxiModelList = new HashMap<>();

    public static void addTaxi(TaxiModel taxiModel) {
        taxiModelList.put(taxiModel.getId(), taxiModel);
    }

    public static TaxiModel getTaxi(int id) {
        return taxiModelList.get(id).clone();
    }

    public static int getNumbersOfTaxis(){
        return taxiModelList.size();
    }


}
