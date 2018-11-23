package com.taxitool.controller.api;

import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.service.DatabaseService;
import com.taxitool.service.TaxiService;
import com.taxitool.utils.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TaxiStatusController {

    @Resource
    private TaxiService taxiService;

    @RequestMapping(value = "/status", method = GET)
    public String getStatus(@RequestParam(value = "id") String id) {
        TaxiModel taxi = getTaxi(id);
        return taxi.getStatus().name();
    }

    @RequestMapping(value = "/terminate", method = POST)
    public String terminate(@RequestParam(value = "id") String id) {
        TaxiModel taxi = getTaxi(id);
        taxi.setStatus(TaxiStatus.FREE);
        taxi.setRoute(null);
        taxi.setEndPoint(null);
        DatabaseService.addTaxi(taxi);
        return "Taxi" + id + "is now free";
    }

    @RequestMapping(value = "/inactive", method = POST)
    public String setInactive(@RequestParam(value = "id") String id) {
        TaxiModel taxi = getTaxi(id);
        taxi.setStatus(TaxiStatus.INACTIVE);
        taxi.setRoute(null);
        taxi.setEndPoint(null);
        DatabaseService.addTaxi(taxi);
        return "Taxi" + id + "is now inactive";
    }

    @RequestMapping(value = "/detour", method = POST)
    public String detour(@RequestParam(value = "id") String id, @RequestParam(value = "address") String address) {

        TaxiModel taxi = getTaxi(id);
        if (taxi != null) {
            boolean updated = taxiService.updateRoute(taxi, address);
            if (updated) {
                taxi.setEndPoint(address);
                DatabaseService.addTaxi(taxi);
                return "Address updated";
            }
        }
        return "Address not updated";
    }

    @RequestMapping(value = "/getlocation", method = GET)
    public String getLocation(@RequestParam(value = "id") String id) {

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
