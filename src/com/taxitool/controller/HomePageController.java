package com.taxitool.controller;

import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.model.geocoding.NavigationPosition;
import com.taxitool.model.routing.Leg;
import com.taxitool.model.routing.Route;
import com.taxitool.service.DatabaseService;
import com.taxitool.service.GeoCodingService;
import com.taxitool.service.RoutingService;
import com.taxitool.service.TaxiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.xml.crypto.Data;

@Controller
@SessionAttributes("taxi")
public class HomePageController {

    @Resource
    private GeoCodingService geoCodingService;
    @Resource
    private RoutingService routingService;
    @Resource
    private TaxiService taxiService;


    @GetMapping("/taxi/{id}")
    public String getTaxi(@PathVariable("id") String id, @ModelAttribute("taxi") TaxiModel taxi, Model model) {


        TaxiModel sessionTaxi = DatabaseService.getTaxi(Integer.parseInt(id));
        if (sessionTaxi != null) {
            taxi = sessionTaxi;
        }
        model.addAttribute("taxi", taxi);
        if (taxi.getRoute() != null) {
            Leg routeInfo = taxi.getRoute().getResponse().getRoute().get(0).getLeg().get(0);
            model.addAttribute("routeInfo", routeInfo);
        }
        return "taxis";
    }


    @PostMapping("/route")
    public RedirectView route(@ModelAttribute("taxi") TaxiModel taxiModel, RedirectAttributes redirectAttributes) {

        taxiModel = taxiService.updateAddress(taxiModel, taxiModel.getAddress());
        //routingService.calculateRoute();
        redirectAttributes.addFlashAttribute("taxi", taxiModel);
        DatabaseService.addTaxi(taxiModel);
        return new RedirectView("/taxi/" + taxiModel.getId());
    }

    @PostMapping("/calcRoute")
    public RedirectView calcRoute(@ModelAttribute("taxi") TaxiModel taxiModel, RedirectAttributes redirectAttributes) {

        NavigationPosition geoCode = geoCodingService.getGeoCode(taxiModel.getEndPoint());
        if (geoCode == null) {
            taxiModel.setAddress("Address not found");
        } else {
            Route route = routingService.calculateRoute(taxiModel, geoCode.getLatitude(), geoCode.getLongitude());
            taxiModel.setRoute(route);
            taxiModel.setStatus(taxiService.onTime(taxiModel));
        }

        DatabaseService.addTaxi(taxiModel);
        redirectAttributes.addFlashAttribute("taxi", taxiModel);
        return new RedirectView("/taxi/" + taxiModel.getId());
    }

    @GetMapping("/sync")
    public RedirectView syncRoute(@ModelAttribute("taxi") TaxiModel taxiModel, RedirectAttributes redirectAttributes) {

        Route route = routingService.syncRoute(taxiModel);
        taxiModel.setStatus(TaxiStatus.ONTIME);

        redirectAttributes.addFlashAttribute("taxi", taxiModel);
        return new RedirectView("/taxi/" + taxiModel.getId());
    }


    @ModelAttribute("taxi")
    public TaxiModel taxi() {
        return new TaxiModel();
    }

}
