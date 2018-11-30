package com.taxitool.controller.api;

import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.service.DatabaseService;
import com.taxitool.service.TaxiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TaxiStatusController {

    @Resource
    private TaxiService taxiService;

    @RequestMapping(value = "/status", method = GET)
    public String getStatus(@RequestParam(value = "id") String id) {
        TaxiModel taxi = getTaxi(id);
        if(taxi!=null) {
            return taxi.getStatus().name();
        }
        return "Taxi not found";
    }

    @RequestMapping(value = "/terminate", method = POST)
    public String terminate(@RequestParam(value = "id") String id) {
        //TODO: RETURN NOT 200 Code on error
        TaxiModel taxi = getTaxi(id);
        if(taxi != null) {
            taxi.setStatus(TaxiStatus.FREE);
            taxi.setRoute(null);
            taxi.setEndPoint(null);
            taxi.setEstimatedTime(null);
            DatabaseService.addTaxi(taxi);
            return "Taxi" + id + " is now free";
        }
        return "Taxi not found";
    }

    @RequestMapping(value = "/inactive", method = POST)
    public String setInactive(@RequestParam(value = "id") String id) {
        TaxiModel taxi = getTaxi(id);
        if(taxi != null) {
            taxi.setStatus(TaxiStatus.INACTIVE);
            taxi.setRoute(null);
            taxi.setEndPoint(null);
            taxi.setEstimatedTime(null);
            DatabaseService.addTaxi(taxi);
            return "Taxi" + id + "is now inactive";
        }
        return "Taxi not found";
    }

    @RequestMapping(value = "/detour", method = POST)
    public String detour(@RequestParam(value = "id") String id, @RequestParam(value = "address") String address) {

        TaxiModel taxi = getTaxi(id);
        if (taxi != null) {
            taxi.setEndPoint(address);
            boolean updated = taxiService.updateRoute(taxi, address);
            if (updated) {
                DatabaseService.addTaxi(taxi);
                return "Address updated";
            }
        }
        return "Address not updated because taxi was not found";
    }

    @RequestMapping(value = "/getlocation", method = GET)
    public String getLocation(@RequestParam(value = "id") String id) {

        //TODO: check encoding
        TaxiModel taxi = getTaxi(id);
        if (taxi != null) {
            return taxi.getAddress();
        }
        return "No taxi with this id found";
    }

    private TaxiModel getTaxi(String id) {
        return DatabaseService.getTaxi(Integer.parseInt(id));
    }
}
