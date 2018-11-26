package com.taxitool.controller;


import com.taxitool.model.TaxiModel;
import com.taxitool.service.DatabaseService;
import com.taxitool.service.LightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Resource
    private LightService lightService;

    @GetMapping("/")
    public String home(Model model) {
        List<TaxiModel> taxiModels = new ArrayList<>();

        for (int index = 1; index < 4; index++) {
            TaxiModel taxi = DatabaseService.getTaxi(index);
            if (taxi == null) {
                taxi = new TaxiModel();
                taxi.setId(index);

                DatabaseService.addTaxi(taxi);
            }
            taxiModels.add(taxi);
        }
        model.addAttribute("taxis", taxiModels);
        return "index";
    }

    @GetMapping("/lights")
    public RedirectView lights(RedirectAttributes redirectAttributes) {

        List<TaxiModel> taxis = new ArrayList<>();
        for (int index = 1; index < DatabaseService.getNumbersOfTaxis()+1; index++) {
            TaxiModel taxi = DatabaseService.getTaxi(index);
            if (taxi != null) {
                taxis.add(taxi);
            }
        }

        lightService.updateLights(taxis);
        return new RedirectView("/");
    }

    public LightService getLightService() {
        return lightService;
    }

    public void setLightService(LightService lightService) {
        this.lightService = lightService;
    }
}
