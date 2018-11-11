package com.taxitool.controller;

import com.taxitool.model.TaxiModel;
import com.taxitool.service.GeoCodingService;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class HomePageController {

    @Resource
    private GeoCodingService geoCodingService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "taxis";
    }


    @PostMapping("/route")
    public String route(@Valid @ModelAttribute("taxi") TaxiModel taxiModel,
                        BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "error";
        }

        Double geoCode = geoCodingService.getGeoCode(taxiModel.getAddress());
        model.addAttribute("taxi", geoCode);
        return "taxis";
    }


}
