package com.taxitool.controller;

import com.taxitool.model.TaxiModel;
import com.taxitool.model.geocoding.NavigationPosition;
import com.taxitool.model.routing.Route;
import com.taxitool.service.GeoCodingService;
import com.taxitool.service.RoutingService;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@SessionAttributes("taxi")
public class HomePageController {

    @Resource
    private GeoCodingService geoCodingService;
    @Resource
    private RoutingService routingService;

    @GetMapping("/taxi/{id}")
    public String getTaxi(@PathVariable("id")String id, @ModelAttribute("taxi") TaxiModel taxi, Model model) {
        taxi.setId(Integer.parseInt(id));
        model.addAttribute("taxi", taxi);
        return "taxis";
    }


    @PostMapping("/route")
    public RedirectView route(@ModelAttribute("taxi") TaxiModel taxiModel, RedirectAttributes redirectAttributes) {

        NavigationPosition geoCode = geoCodingService.getGeoCode(taxiModel.getAddress());
        if(geoCode==null){
            taxiModel.setAddress("Address not found");
        } else {
            taxiModel.setLatitude(geoCode.getLatitude());
            taxiModel.setLongtitude(geoCode.getLongitude());
        }

        //routingService.calculateRoute();
        redirectAttributes.addFlashAttribute("taxi", taxiModel);
        return new RedirectView("/taxi/"+taxiModel.getId());
    }

    @PostMapping("/calcRoute")
    public RedirectView calcRoute(@ModelAttribute("taxi") TaxiModel taxiModel, RedirectAttributes redirectAttributes) {

        NavigationPosition geoCode = geoCodingService.getGeoCode(taxiModel.getEndPoint());
        if(geoCode==null){
            taxiModel.setAddress("Address not found");
        } else {
            Route route = routingService.calculateRoute(taxiModel, geoCode.getLatitude(), geoCode.getLongitude());
            taxiModel.setRoute(route);
        }

        redirectAttributes.addFlashAttribute("taxi", taxiModel);
        return new RedirectView("/taxi/"+taxiModel.getId());
    }


    @ModelAttribute("taxi")
    public TaxiModel taxi() {
        return new TaxiModel();
    }

}
