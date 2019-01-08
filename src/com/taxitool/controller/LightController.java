package com.taxitool.controller;


import com.taxitool.model.TaxiModel;
import com.taxitool.model.TaxiStatus;
import com.taxitool.service.LightService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;



@RequestMapping(value="/lights")
@RestController

public class LightController {
    private final LightService lightService = new LightService();
    //looks every 5 seconds for taxi status and updates the lights
    //TODO Wahrscheinlich ist das hier die falsche Stelle fuer die Methode. In welche Klasse muss das eigentlich?: Über Front end ansteuern undvariable auf true setzen
    public void updateLights(TaxiModel[] taxiModels) throws IOException, InterruptedException {
        while (true) {
            for (int index = 0; index < 3; index++) {
                if (taxiModels[index].getStatus().equals(TaxiStatus.FREE)) {
                    lightService.changeColor(index + "", "green");
                } else if (taxiModels[index].getStatus().equals(TaxiStatus.DELAY)) {
                    lightService.blink(index + "");
                } else if (taxiModels[index].equals(TaxiStatus.ONTIME)) {
                    lightService.changeColor(index + "", "orange");
                } else {
                    lightService.turnLightOff(index + "");
                }
            }
        }
    }
}
