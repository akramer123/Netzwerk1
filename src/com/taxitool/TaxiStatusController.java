package com.taxitool;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping(value="/taxi")
@RestController
public class TaxiStatusController {


    @RequestMapping(value = "/status", method = GET)
    public String getStatus(@RequestParam(value = "id") String id) {
        return "test";
    }

    @RequestMapping(value = "/terminate", method = POST)
    public String terminate(@RequestParam(value = "id") String id) {
        return "test";
    }

    @RequestMapping(value = "/detour", method = POST)
    public String detour(@RequestParam(value = "id") String id) {
        return "test";
    }
}
