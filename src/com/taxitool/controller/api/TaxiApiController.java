/**
 * @author Andreas Kramer
 */

package com.taxitool.controller.api;

import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.service.DatabaseService;
import com.taxitool.service.TaxiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TaxiApiController {

    @Resource
    private TaxiService taxiService;

    @RequestMapping(value = "/status", method = GET, produces = "text/plain")
    public String getStatus(@RequestParam(value = "id") String id, HttpServletResponse response) {
        TaxiModel taxi = getTaxi(id);
        if (taxi != null) {
            return taxi.getStatus().name();
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "Taxi not found";
    }

    @RequestMapping(value = "/terminate", method = POST, produces = "text/plain")
    public String terminate(@RequestParam(value = "id") String id, HttpServletResponse response) {
        //TODO: RETURN NOT 200 Code on error
        TaxiModel taxi = getTaxi(id);
        if (taxi != null) {
            taxi.setStatus(TaxiStatus.FREE);
            taxi.setRoute(null);
            taxi.setEndPoint(null);
            taxi.setEstimatedTime(null);
            return "Taxi" + id + " is now free";
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "Taxi not found";
    }

    @RequestMapping(value = "/inactive", method = POST, produces = "text/plain")
    public String setInactive(@RequestParam(value = "id") String id, HttpServletResponse response) {
        TaxiModel taxi = getTaxi(id);
        if (taxi != null) {
            taxi.setStatus(TaxiStatus.INACTIVE);
            taxi.setRoute(null);
            taxi.setEndPoint(null);
            taxi.setEstimatedTime(null);
            DatabaseService.addTaxi(taxi);
            return "Taxi" + id + "is now inactive";
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "Taxi not found";
    }

    @RequestMapping(value = "/detour", method = POST, produces = "text/plain")
    public String detour(@RequestParam(value = "id") String id, @RequestParam(value = "address") String address,
                         HttpServletResponse response) {

        TaxiModel taxi = getTaxi(id);
        if (taxi != null) {
            taxi.setEndPoint(address);
            boolean updated = taxiService.updateRoute(taxi, address);
            if (updated) {
                DatabaseService.addTaxi(taxi);
                return "Address updated";
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "Address not updated because taxi was not found";
    }

    @RequestMapping(value = "/getlocation", method = GET, produces = "text/plain;charset=UTF-8")
    public String getLocation(@RequestParam(value = "id") String id, HttpServletResponse response) {

        TaxiModel taxi = getTaxi(id);
        if (taxi != null) {
            return taxi.getAddress();
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "No taxi with this id found";
    }

    private TaxiModel getTaxi(String id) {
        return DatabaseService.getTaxi(Integer.parseInt(id));
    }

}
