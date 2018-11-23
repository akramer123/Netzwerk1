package com.taxitool.controller;


import com.taxitool.service.LightService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



@RequestMapping(value="/lights")
@RestController
public class LightController {
    @Resource
    private LightService lightService;

    //looks every 5 seconds for taxi status and updates the lights
    //TODO Wahrscheinlich ist das hier die falsche Stelle fuer die Methode. In welche Klasse muss das eigentlich?

}
